package Domain;

import java.time.LocalDateTime;

public class InchiriereConverter implements EntityConverter<Inchiriere> {
    @Override
    public String toString(Inchiriere inchiriere) {
        return inchiriere.getId() + "," + inchiriere.getMasina().getId() + "," + inchiriere.getDataInceput() + "," + inchiriere.getDataSfarsit();
    }

    @Override
    public Inchiriere fromString(String string) {
        String[] tokens = string.split(",");
        return new Inchiriere(Integer.parseInt(tokens[0]),new Masina(Integer.parseInt(tokens[1]),"",""), LocalDateTime.parse(tokens[2]),LocalDateTime.parse(tokens[3]));
    }
}
