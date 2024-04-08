package Repository;

import Domain.Entity;

import java.io.*;
import java.util.List;

public class BinaryFileRepository <T extends Entity> extends MemoryRepository<T> {
    private String fileName;

    public BinaryFileRepository(String fileName) {
        this.fileName = fileName;
        try {
            //evitare incarcare a datelor daca fisierul e gol
            if (new File(fileName).length() > 0){
                loadFile();
                System.out.println("S-a incarcat fisierul");
            }
            else
                System.out.println("Fisierul e gol");

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void add(T o) throws RepositoryExcept, DuplicateEntityExcept {
        super.add(o);
        // saveFile se executa doar daca super.add() nu a aruncat exceptie
        try {
            saveFile();
            System.out.println("Saved file");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    public void remove(int id) throws RepositoryExcept {
        super.remove(id);
        try {
            saveFile();
        } catch (IOException e) {
            throw new RepositoryExcept("Error saving object", e);
        }
    }

    @Override
    public void update(T o) throws RepositoryExcept {
        super.update(o);
        try {
            saveFile();
        } catch (IOException e) {
            throw new RepositoryExcept("Error saving object", e);
        }
    }


    private void loadFile() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = null;
        try{
            ois = new ObjectInputStream(new FileInputStream(fileName));
            this.data = (List<T>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Repo starting a new file");
        } finally {
            // finally se executa tot timpul
            if (ois != null)
                ois.close();
        }


    }

    private void saveFile() throws IOException {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
            oos.writeObject(data);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }
}
