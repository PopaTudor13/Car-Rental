package Service;

import Domain.Masina;
import Repository.DuplicateEntityExcept;
import Repository.IRepository;
import Repository.RepositoryExcept;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class MasinaService {
    IRepository<Masina> repository;
    public MasinaService(IRepository<Masina> repository){
        this.repository = repository;
    }
    public void add(int id, String marca, String model) throws DuplicateEntityExcept, RepositoryExcept {

        repository.add(new Masina(id,marca,model));
    }

    public void remove(int id) throws RepositoryExcept {
        repository.remove(id);
    }

    public void update(int id, String marca, String model) throws RepositoryExcept {
        repository.update(new Masina(id,marca,model));
    }

    public Collection<Masina> getAll(){
        return repository.getAll();
    }

    public Masina find(int id){
        return repository.find(id);
    }

    public Masina cautare_masina(int id){
        if(repository.find(id)==null)
            throw new IllegalArgumentException("Masina cu id-ul dat nu exista!");
        return repository.find(id);
    }

    public void populate(){
        Collection<Masina> masini = masini_factory();
        for(Masina masina: masini){
            try {
                repository.add(masina);
            } catch (DuplicateEntityExcept duplicateEntityExcept) {
                duplicateEntityExcept.printStackTrace();
            } catch (RepositoryExcept repositoryExcept) {
                repositoryExcept.printStackTrace();
            }
        }
    }

    private static Collection<Masina> masini_factory() {

        List<String> REAL_CAR_MARCAS = List.of("Toyota", "Honda", "Ford", "Chevrolet", "BMW", "Audi", "Mercedes-Benz", "Volkswagen", "Tesla", "Subaru");
        List<String> REAL_CAR_MODELS = List.of("Corolla", "Civic", "F-150", "Silverado", "3 Series", "A4", "C-Class", "Golf", "Model 3", "Outback");


        List<Masina> masini = new ArrayList<>();
        Random random = new Random();
        int uniqueIdCounter = 1;

        for (int i = 0; i < 100; i++) {
            String marca = REAL_CAR_MARCAS.get(random.nextInt(REAL_CAR_MARCAS.size()));
            String model = REAL_CAR_MODELS.get(random.nextInt(REAL_CAR_MODELS.size()));

            String carKey = marca + "_" + model;
            if (countOccurrences(masini, carKey) >= 10) {
                continue;
            }

            masini.add(new Masina(uniqueIdCounter++, marca, model));
        }

        return masini;

    }
    private static int countOccurrences (List <Masina> masini, String carKey){
        int count = 0;
        for (Masina masina : masini) {
            String key = masina.getMarca() + "_" + masina.getModel();
            if (key.equals(carKey)) {
                count++;
            }
        }
        return count;
    }
}
