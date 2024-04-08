package Repository;

import Repository.RepositoryExcept;
import Domain.Entity;
import Domain.EntityConverter;
import java.io.BufferedReader;

import java.io.*;
import java.util.Scanner;

public class TextFileRepository<T extends Entity> extends MemoryRepository<T> {
    private String fileName;
    private EntityConverter<T> converter;
    public TextFileRepository(String fileName, EntityConverter<T> converter ) throws FileNotFoundException {
        this.fileName = fileName;
        this.converter = converter;

        try {
            readFromFile();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(T o) throws DuplicateEntityExcept, RepositoryExcept {
        try {
            super.add(o);
        }catch (DuplicateEntityExcept e){
            System.out.println(e);
            throw new RepositoryExcept("Error adding object of type" + o.getClass() , e);
        }
        try {
            saveFile();
        } catch (IOException e) {
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

    private void saveFile() throws IOException {
        try (FileWriter fw = new FileWriter(fileName)) {
            // file is rewritten at each modification

            for (T object : data) {
                fw.write(converter.toString(object));
                fw.write("\r\n");
            }
        }
    }

    private void readFromFile() throws IOException {
        data.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = br.readLine();
            while (line != null && !line.isEmpty()) {
                data.add(converter.fromString(line));
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
