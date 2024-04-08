package UI;

import Domain.Inchiriere;
import Domain.Masina;
import Repository.DateExcept;
import Repository.DuplicateEntityExcept;
import Service.InchiriereService;
import Service.MasinaService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class Consola {
    MasinaService masinaservice;
    InchiriereService inchiriereservice;

    public Consola(MasinaService masinaservice, InchiriereService inchiriereservice){
        this.masinaservice = masinaservice;
        this.inchiriereservice = inchiriereservice;
    }
    public void runMenu(){
        //populare();
        while(true){
            printMenu();
            String optiune;
            Scanner scanner = new Scanner(System.in);
            optiune = scanner.next();
            switch (optiune){
                case "1":{
                    adaugare_masina(scanner);
                    break;
                }
                case "2":{
                    adaugare_inchiriere(scanner);
                    break;
                }
                case "m":{
                    Collection<Masina> masini = masinaservice.getAll();
                    for(Masina masina: masini){
                        System.out.println(masina);
                    }
                    break;
                }
                case "i":{
                    Collection<Inchiriere> inchirieri = inchiriereservice.getAll();
                    for(Inchiriere inchiriere: inchirieri){
                        System.out.println(inchiriere);
                    }
                    break;
                }
                case "3":{
                    modificare_masina(scanner);
                    break;
                }
                case "4":{
                    modificare_inchiriere(scanner);
                    break;
                }
                case "5":{
                    stergere_masina(scanner);
                    break;
                }
                case "6":{
                    stergere_inchiriere(scanner);
                    break;
                }
                case "7":{
                    List<String> lista = inchiriereservice.cmdim();
                    for(String s: lista){
                        System.out.println(s);
                    }
                    break;
                }
                case "8":{
                    List<String> lista = inchiriereservice.inchirierilunare();
                    for(String s: lista){
                        System.out.println(s);
                    }
                    break;
                }
                case "9":{
                    List<String> lista = inchiriereservice.mostused();
                    for(String s: lista){
                        System.out.println(s);
                    }
                    break;
                }
                case "x":{
                    return;
                }
                default:{
                    System.out.println("Optiune invalida");
                }
            }
        }
    }
    private void printMenu(){
        System.out.println("1. Adauga masina");
        System.out.println("2. Adauga inchiriere");
        System.out.println("m. Afiseaza masini");
        System.out.println("i. Afiseaza inchirieri");
        System.out.println("3. Modifica masina");
        System.out.println("4. Modifica inchiriere");
        System.out.println("5. Sterge masina");
        System.out.println("6. Sterge inchiriere");
        System.out.println("7. Cele mai inchiriate masini");
        System.out.println("8. Inchirieri lunare");
        System.out.println("9. Cele mai folosite masini");
        System.out.println("x. Exit");
    }
    /*private void populare(){
        try{
            masinaservice.add(1,"Audi","A4");
            masinaservice.add(2,"BMW","Seria 3");
            masinaservice.add(3,"Mercedes","C-Class");
            masinaservice.add(4,"Volkswagen","Passat");
            masinaservice.add(5,"Skoda","Octavia");
            inchiriereservice.add(1,masinaservice.cautare_masina(1), LocalDateTime.of(2021,5,6,12,0),LocalDateTime.of(2021,5,7,12,0));
            inchiriereservice.add(2,masinaservice.cautare_masina(1), LocalDateTime.of(2021,5,4,12,0),LocalDateTime.of(2021,5,6,12,0));
            inchiriereservice.add(3,masinaservice.cautare_masina(3), LocalDateTime.now(), LocalDateTime.now().plusDays(2));
        }catch (DuplicateEntityExcept e){
            System.out.println(e.toString());
        }catch (DateExcept e){
            System.out.println(e.toString());
        }
    }*/

    private void adaugare_masina(Scanner scanner){
        try{
            System.out.println("Dati id-ul: ");
            int id = scanner.nextInt();
            System.out.println("Dati marca: ");
            String marca = scanner.next();
            System.out.println("Dati modelul: ");
            String model = scanner.next();
            masinaservice.add(id,marca,model);
        }catch (DuplicateEntityExcept e){
            System.out.println(e.toString());
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    private void adaugare_inchiriere(Scanner scanner){
        try{
            System.out.println("Dati id-ul: ");
            int id = scanner.nextInt();
            System.out.println("Dati id-ul masinii: ");
            String id_masina = scanner.next();
            Masina masina = masinaservice.cautare_masina(Integer.parseInt(id_masina));
            System.out.println("Dati data de inceput(yyyy-mm-dd hh:mm): ");
            String data_inceput_s = scanner.next();
            LocalDateTime data_inceput = LocalDateTime.parse(data_inceput_s);
            System.out.println("Dati data de sfarsit(yyyy-mm-dd hh:mm): ");
            String data_sfarsit_s = scanner.next();
            LocalDateTime data_sfarsit = LocalDateTime.parse(data_sfarsit_s);
            inchiriereservice.add(id,masina,data_inceput,data_sfarsit);
        }catch (DuplicateEntityExcept e) {
            System.out.println(e.toString());
        }catch (IllegalArgumentException e){
            System.out.println(e.toString());
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    private void modificare_masina(Scanner scanner){
        try{
            System.out.println("Dati id-ul: ");
            int id = scanner.nextInt();
            System.out.println("Dati noua marca: ");
            String marca = scanner.next();
            System.out.println("Dati noul model: ");
            String model = scanner.next();
            masinaservice.update(id,marca,model);
        }catch (IllegalArgumentException e){
            System.out.println(e.toString());
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    private void modificare_inchiriere(Scanner scanner){
        try{
            System.out.println("Dati id-ul: ");
            int id = scanner.nextInt();
            System.out.println("Dati noua masina: ");
            String id_masina = scanner.next();
            Masina masina = masinaservice.cautare_masina(Integer.parseInt(id_masina));
            System.out.println("Dati noua data de inceput(yyyy-mm-ddThh:mm:ss): ");
            String data_inceput_s = scanner.next();
            LocalDateTime data_inceput = LocalDateTime.parse(data_inceput_s);
            System.out.println("Dati noua data de sfarsit(yyyy-mm-ddThh:mm:ss): ");
            String data_sfarsit_s = scanner.next();
            LocalDateTime data_sfarsit = LocalDateTime.parse(data_sfarsit_s);
            inchiriereservice.update(id,masina,data_inceput,data_sfarsit);
        }catch (IllegalArgumentException e){
            System.out.println(e.toString());
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    private void stergere_masina(Scanner scanner){
        try{
            System.out.println("Dati id-ul: ");
            int id = scanner.nextInt();
            masinaservice.remove(id);
        }catch (IllegalArgumentException e){
            System.out.println(e.toString());
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    private void stergere_inchiriere(Scanner scanner){
        try{
            System.out.println("Dati id-ul: ");
            int id = scanner.nextInt();
            inchiriereservice.remove(id);
        }catch (IllegalArgumentException e){
            System.out.println(e.toString());
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

}
