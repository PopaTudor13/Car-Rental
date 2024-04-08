package Domain;

public class MasinaConverter implements EntityConverter<Masina>{

    @Override
    public String toString(Masina masina) {
        return masina.getId() + "," + masina.getMarca() + "," + masina.getModel();
    }

    @Override
    public Masina fromString(String line) {
        String[] tokens = line.split(",");
        return new Masina(Integer.parseInt(tokens[0]),tokens[1],tokens[2]);
    }
}
