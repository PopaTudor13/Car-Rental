package Repository;

import Domain.Inchiriere;
import Domain.Masina;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class InchirieriDBRepository extends MemoryRepository<Inchiriere> implements IDbRepository<Inchiriere> {

    private String JDBC_URL = "jdbc:sqlite:identifier.sqlite";
    private Connection connection = null;

    public InchirieriDBRepository(String JDBC_URL){
        this.JDBC_URL = JDBC_URL;
        openConnection();
        createTable();
        DatabaseHasData();
    }

    public InchirieriDBRepository(){
        openConnection();
        createTable();
        DatabaseHasData();
    }

    public void DatabaseHasData(){
        Collection<Inchiriere> inchirieri = getAll();
        if(this.data.isEmpty()){
            loadFromDataBase(inchirieri);
        }
    }
    public void loadFromDataBase(Collection<Inchiriere> inchirieri){
        for(Inchiriere inchiriere: inchirieri){
            try {
                super.add(inchiriere);
            } catch (DuplicateEntityExcept duplicateEntityExcept) {
                System.out.println(duplicateEntityExcept);;
            } catch (RepositoryExcept repositoryExcept) {
                repositoryExcept.printStackTrace();
            }
        }
    }

    public void openConnection(){
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(JDBC_URL);
        try {
            if(connection == null || connection.isClosed())
            {
                connection = dataSource.getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void closeConnection() {
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void createTable(){
        try(final Statement stm = connection.createStatement()){
            stm.execute("CREATE TABLE IF NOT EXISTS masini (id int primary key, nume varchar(400), model varchar(400));");
            stm.execute("CREATE TABLE IF NOT EXISTS inchirieri (id int primary key, id_masina int , " +
                    "data_inceput datetime , data_sfarsit datetime , foreign key(id_masina) references masini(id));");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Collection<Inchiriere> getAll(){
        ArrayList<Inchiriere> inchirieri = new ArrayList<>();
        Collection<Masina> masini = getMasini();
        try (PreparedStatement stm = connection.prepareStatement("SELECT * from inchirieri;")) {
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {

                int id_masina = rs.getInt(2);
                for(Masina m : masini){
                    if(m.getId() == id_masina){
                        Inchiriere i = new Inchiriere(rs.getInt(1),m, rs.getTimestamp(3).toLocalDateTime(), rs.getTimestamp(4).toLocalDateTime());
                        inchirieri.add(i);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inchirieri;
    }

    public Collection<Masina> getMasini(){

        ArrayList<Masina> masini = new ArrayList<>();
        try (PreparedStatement stm = connection.prepareStatement("SELECT * from masini;")) {
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Masina m = new Masina(rs.getInt(1), rs.getString(2), rs.getString(3));
                masini.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return masini;
    }

    @Override
    public void add(Inchiriere i) throws DuplicateEntityExcept, RepositoryExcept {
        super.add(i);
        try (PreparedStatement stm = connection.prepareStatement("INSERT INTO inchirieri VALUES (?, ?, ?, ?);")) {
            stm.setInt(1, i.getId());
            stm.setInt(2, i.getMasina().getId());
            stm.setTimestamp(3, Timestamp.valueOf(i.getDataInceput()));
            stm.setTimestamp(4, Timestamp.valueOf(i.getDataSfarsit()));
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryExcept("Error adding object of type" + i.getClass() , e);
        }
    }

    @Override
    public void remove(int id) throws RepositoryExcept {
        super.remove(id);
        try (PreparedStatement stm = connection.prepareStatement("DELETE FROM inchirieri WHERE id = ?;")) {
            stm.setInt(1, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryExcept("Error removing object with id " + id, e);
        }
    }

    @Override
    public void update(Inchiriere i) throws RepositoryExcept {
        super.update(i);
        try (PreparedStatement stm = connection.prepareStatement("UPDATE inchirieri SET id_masina = ?, data_inceput = ?, data_sfarsit = ? WHERE id = ?;")) {
            stm.setInt(1, i.getMasina().getId());
            stm.setTimestamp(2, Timestamp.valueOf(i.getDataInceput()));
            stm.setTimestamp(3, Timestamp.valueOf(i.getDataSfarsit()));
            stm.setInt(4, i.getId());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryExcept("Error updating object with id " + i.getId(), e);
        }
    }

}
