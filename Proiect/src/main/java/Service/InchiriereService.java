package Service;

import Domain.Inchiriere;
import Domain.Masina;
import Repository.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InchiriereService {
    IRepository<Inchiriere> repository;
    IRepository<Masina> repository_masina;

    public InchiriereService(IRepository<Inchiriere> repository, IRepository<Masina> repository_masina){
        this.repository = repository;
        this.repository_masina = repository_masina;
    }
    public void add(int id, Masina masina, LocalDateTime data_inceput, LocalDateTime data_sfarsit) throws DuplicateEntityExcept, DateExcept, RepositoryExcept, SameCarExcept {
        if(repository_masina.find(masina.getId()) == null)
            throw new IllegalArgumentException("Masina cu id-ul dat nu exista!");
        if(data_inceput.isAfter(data_sfarsit))
            throw new DateExcept("Data de inceput trebuie sa fie inainte de data de sfarsit!");
        if(data_inceput.isEqual(data_sfarsit))
            throw new DateExcept("Data de inceput trebuie sa fie diferita de data de sfarsit!");
        try {
            verif_inregistrare(masina, data_inceput, data_sfarsit);
        }catch (SameCarExcept e){
            throw new SameCarExcept(e.getMessage());
        }
        repository.add(new Inchiriere(id,masina,data_inceput,data_sfarsit));
    }

    private void verif_inregistrare(Masina masina, LocalDateTime data_inceput, LocalDateTime data_sfarsit) throws SameCarExcept {
        Collection<Inchiriere> inchirieri = repository.getAll();
        for(Inchiriere inchiriere: inchirieri){
            if(inchiriere.getMasina().getId() == masina.getId())
                if(inchiriere.getDataSfarsit().isAfter(data_inceput) && inchiriere.getDataInceput().isBefore(data_sfarsit))
                    throw new SameCarExcept("Masina cu id-ul dat este deja inchiriata!");
        }
    }

    public void remove(int id) throws RepositoryExcept {
        repository.remove(id);
    }

    public void update(int id, Masina masina, LocalDateTime data_inceput, LocalDateTime data_sfarsit) throws RepositoryExcept, DateExcept, SameCarExcept {
        if(repository.find(id) == null)
            throw new IllegalArgumentException("Inchirierea cu id-ul dat nu exista!");
        if(data_inceput.isAfter(data_sfarsit))
            throw new DateExcept("Data de inceput trebuie sa fie inainte de data de sfarsit!");
        if(data_inceput.isEqual(data_sfarsit))
            throw new DateExcept("Data de inceput trebuie sa fie diferita de data de sfarsit!");
        verif_inregistrare(masina, data_inceput, data_sfarsit);
        repository.update(new Inchiriere(id,masina,data_inceput,data_sfarsit));
    }

    public Collection<Inchiriere> getAll(){
        return repository.getAll();
    }

    public List<String> cmdim(){
        Collection<Inchiriere> inchirieri = repository.getAll();
        List<String> lista= new ArrayList<>();
        Map<Masina, Long> masinicount = inchirieri.stream()
                .collect(Collectors.groupingBy(Inchiriere::getMasina, Collectors.counting()));
        masinicount.entrySet().stream()
                .collect(Collectors.toMap(entry->entry.getKey().getMarca()+", "+entry.getKey().getModel(), Map.Entry::getValue, Long::sum))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(entry-> {
                    String Masina = entry.getKey();
                    Long nrinchirieri = entry.getValue();
                    lista.add(Masina + " - " + nrinchirieri);
                });
        return lista;
    }

    public List<String> inchirierilunare(){
        Collection<Inchiriere> inchirieri = repository.getAll();
        List<String> lista= new ArrayList<>();
        Map<Integer, Long> lunimapa = inchirieri.stream()
                .collect(Collectors.groupingBy(inchiriere -> inchiriere.getDataInceput().getMonthValue() , Collectors.counting()));
        lunimapa.entrySet().stream()
                .collect(Collectors.toMap(entry->entry.getKey(), Map.Entry::getValue, Long::sum))
                .entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .forEach(entry-> {
                    Integer luna = entry.getKey();
                    Long nrinchirieri = entry.getValue();
                    lista.add("In luna: "+ luna + " sunt " + nrinchirieri + " inchirieri");
                });
        return lista;
    }

    public List<String> mostused(){
        Collection<Inchiriere> inchirieri = repository.getAll();
        List<String> lista= new ArrayList<>();
        Map<Masina, Long> masinicount = inchirieri.stream()
                .collect(Collectors.groupingBy(Inchiriere::getMasina, Collectors.summingLong(inchiriere -> Duration.between(inchiriere.getDataInceput(),inchiriere.getDataSfarsit()).toDays())));
        masinicount.entrySet().stream()
                .collect(Collectors.toMap(entry->entry.getKey().getId()+", "+entry.getKey().getMarca()+", "+entry.getKey().getModel(), Map.Entry::getValue, Long::sum))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(entry-> {
                    String Masina = entry.getKey();
                    Long nrinchirieri = entry.getValue();
                    lista.add(Masina + " - " + nrinchirieri);
                });
        return lista;

    }

    public void populate(){
        Collection<Masina> masini = repository_masina.getAll();
        Collection<Inchiriere> inchirieri = generateInchirieri(masini);
        for(Inchiriere inchiriere: inchirieri){
            try {
                repository.add(inchiriere);
            } catch (DuplicateEntityExcept duplicateEntityExcept) {
                duplicateEntityExcept.printStackTrace();
            } catch (RepositoryExcept repositoryExcept) {
                repositoryExcept.printStackTrace();
            }
        }



    }

    public static Collection<Inchiriere> generateInchirieri(Collection<Masina> listamasini) {
        List<Masina> masini = new ArrayList<>(listamasini);
        List<Inchiriere> inchirieri = new ArrayList<>();
        Random random = new Random();
        int uniqueIdCounter = 1;

        for (int i = 0; i < 100; i++) {
            Masina masina;
            LocalDateTime startDateTime;
            LocalDateTime endDateTime;
            do {
                masina = masini.get(random.nextInt(masini.size()));
                startDateTime = LocalDateTime.of(2023,5,1,12,0).plusMonths(random.nextInt(30)); // Random start date within 30 days from now
                endDateTime = startDateTime.plusDays(random.nextInt(1,40)); // End date within 10 days from start
            } while (!isMasinaAvailable(masina, startDateTime, endDateTime, inchirieri) || startDateTime.isAfter(endDateTime));

            Inchiriere inchiriere = new Inchiriere(uniqueIdCounter++, masina, startDateTime, endDateTime);
            inchirieri.add(inchiriere);
        }

        return inchirieri;
    }

    private static boolean isMasinaAvailable(Masina masina, LocalDateTime startDateTime, LocalDateTime endDateTime, List<Inchiriere> listaInchirieri) {
        List<Inchiriere> GENERATED_INCHIRIERI = listaInchirieri;
        for (Inchiriere inchiriere : GENERATED_INCHIRIERI) {
            if (inchiriere.getMasina().equals(masina)) {
                LocalDateTime existingStart = inchiriere.getDataInceput();
                LocalDateTime existingEnd = inchiriere.getDataSfarsit();

                if ((startDateTime.isAfter(existingStart) && startDateTime.isBefore(existingEnd)) ||
                        (endDateTime.isAfter(existingStart) && endDateTime.isBefore(existingEnd)) ||
                        (startDateTime.isBefore(existingStart) && endDateTime.isAfter(existingEnd))) {
                    return false;
                }
            }
        }
        return true;
    }
}
