package Repository;

import Domain.Masina;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class MasiniDBRepository extends MemoryRepository<Masina> implements IDbRepository<Masina>{
    private String JDBC_URL = "jdbc:sqlite:identifier.sqlite" ;
    private Connection connection = null;

    public MasiniDBRepository(String JDBC_URL){
        this.JDBC_URL = JDBC_URL;
        openConnection();
        createTable();
        DatabaseHasData();
    }

    public MasiniDBRepository(){
        openConnection();
        createTable();
        DatabaseHasData();
    }




    public void DatabaseHasData(){
        Collection<Masina> masini = getAll();
        if(this.data.isEmpty()){
            loadFromDataBase(masini);
        }
    }
    public void loadFromDataBase(Collection<Masina> masini){
        for(Masina masina: masini){
            try {
                super.add(masina);
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
            stm.execute("CREATE TABLE IF NOT EXISTS masini (id int primary key, marca varchar(400), model varchar(400));");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initTable(){
        List<Masina> masini = new ArrayList<>();
        masini.add(new Masina(1,"Audi","A4"));
        masini.add(new Masina(2,"BMW","X5"));
        masini.add(new Masina(3,"Mercedes","C-Class"));
        try(PreparedStatement stm = connection.prepareStatement("INSERT INTO masini VALUES (?,?,?);")){
            for(Masina masina: masini){
                stm.setInt(1,masina.getId());
                stm.setString(2,masina.getMarca());
                stm.setString(3,masina.getModel());
                stm.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Collection<Masina> getAll(){
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
    public void add(Masina masina) throws RepositoryExcept, DuplicateEntityExcept {
        super.add(masina);
        try(PreparedStatement stm = connection.prepareStatement("INSERT INTO masini VALUES (?,?,?);")){
            stm.setInt(1,masina.getId());
            stm.setString(2,masina.getMarca());
            stm.setString(3,masina.getModel());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(int id) throws RepositoryExcept {
        super.remove(id);
        try(PreparedStatement stm = connection.prepareStatement("DELETE FROM masini WHERE id = ?;")){
            stm.setInt(1,id);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Masina masina) throws RepositoryExcept {
        super.update(masina);
        try(PreparedStatement stm = connection.prepareStatement("UPDATE masini SET marca = ?, model = ? WHERE id = ?;")){
            stm.setString(1,masina.getMarca());
            stm.setString(2,masina.getModel());
            stm.setInt(3,masina.getId());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryExcept("Error updating object with id " + masina.getId(), e);
        }
    }

}
