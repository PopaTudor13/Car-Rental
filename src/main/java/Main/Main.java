package Main;

import Domain.*;
import Repository.*;
import Service.InchiriereService;
import Service.MasinaService;
import UI.Consola;
import org.example.laborator4.HelloApplication;

import java.time.LocalDateTime;
import java.util.*;

public class Main {
    public static void main(String[] args) throws RepositoryExcept, DuplicateEntityExcept, DateExcept, SameCarExcept {
        //tests();

        IRepository<Masina> masinarepo = null;
        IRepository<Inchiriere> inchiriererepo = null;

        EntityConverter<Masina> ec = new MasinaConverter();
        EntityConverter<Inchiriere> ec2 = new InchiriereConverter();

        Settings setari = Settings.getInstance();

        if (Objects.equals(setari.getRepoType(), "memory")) {
            masinarepo = new MemoryRepository<>();
        }
        if(Objects.equals(setari.getRepoType2(), "memory")){
            inchiriererepo = new MemoryRepository<>();
        }

        if(Objects.equals(setari.getRepoType2(), "text")){
            try{
                inchiriererepo = new TextFileRepository<>(setari.getRepoFile2(), ec2);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }

        if (Objects.equals(setari.getRepoType(), "text")) {
            try{
                masinarepo = new TextFileRepository<>(setari.getRepoFile(), ec);
            }catch (Exception e){
                throw new RuntimeException(e);
            }

        }
        if (Objects.equals(setari.getRepoType(), "binary")) {
            try{
                masinarepo = new BinaryFileRepository<>(setari.getRepoFile());
            }catch (Exception e){
                throw new RuntimeException(e);
            }

        }
        if(Objects.equals(setari.getRepoType2(), "binary")){
            try{
                inchiriererepo = new BinaryFileRepository<>(setari.getRepoFile2());
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }

        boolean r1 = false;
        boolean r2 = false;

        if(Objects.equals(setari.getRepoType(), "database")){
            masinarepo = new MasiniDBRepository();
            r1 = true;

        }
        if(Objects.equals(setari.getRepoType2(), "database")){
            inchiriererepo = new InchirieriDBRepository();
            r2 = true;
        }


        MasinaService masinaservice = new MasinaService(masinarepo);
        InchiriereService inchiriereservice = new InchiriereService(inchiriererepo, masinarepo);
        if(r1 && masinarepo.getAll().isEmpty())
        {
            masinaservice.populate();
        }
        if(r2 && inchiriererepo.getAll().isEmpty() && ! masinarepo.getAll().isEmpty())
        {
            inchiriereservice.populate();
        }



        if(Objects.equals(setari.getRunType(), "console")){
            Consola consola = new Consola(masinaservice, inchiriereservice);
            consola.runMenu();
        }

        if(Objects.equals(setari.getRunType(), "fx")){
            HelloApplication JavaFXApplication = new HelloApplication();
            JavaFXApplication.main(args);
        }

        if(masinarepo != null && r1)
            masinarepo.closeConnection();
        if(inchiriererepo != null && r2)
            inchiriererepo.closeConnection();
    }
}
