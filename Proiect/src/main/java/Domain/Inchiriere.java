package Domain;

import Domain.Entity;
import java.time.LocalDateTime;
import java.time.LocalTime;

import Domain.Masina;

public class Inchiriere extends Entity{
    private Masina masina;
    private LocalDateTime data_inceput;
    private LocalDateTime data_sfarsit;

    public Inchiriere(int id,Masina masina,LocalDateTime data_inceput,LocalDateTime data_sfarsit){
        super(id);
        this.masina = masina;
        this.data_inceput = data_inceput;
        this.data_sfarsit = data_sfarsit;
    }

    public Masina getMasina(){
        return masina;
    }

    public LocalDateTime getDataInceput(){
        return data_inceput;
    }

    public LocalDateTime getDataSfarsit(){
        return data_sfarsit;
    }

    public void setMasina(Masina masina){
        this.masina = masina;
    }

    public void setDataInceput(LocalDateTime data_inceput){
        this.data_inceput = data_inceput;
    }

    public void setDataSfarsit(LocalDateTime data_sfarsit){
        this.data_sfarsit = data_sfarsit;
    }

    @Override
    public String toString(){
        return "id=" + getId() + ", masina=" + masina.getId() + ", data_inceput=" + data_inceput.toString() + ", data_sfarsit=" + data_sfarsit.toString();
    }

    @Override
    public boolean  equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass())
            return false;
        if (obj == this)
            return true;
        return this.getId() == ((Inchiriere) obj).getId();
    }

}
