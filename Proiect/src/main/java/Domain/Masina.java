package Domain;

import Domain.Entity;

public class Masina extends Entity {
    private String marca;
    private String model;

    public Masina(int id, String marca, String model){
        super(id);
        this.marca = marca;
        this.model = model;
    }

    public String getMarca(){
        return marca;
    }
    public String getModel(){
        return model;
    }

    public void setMarca(String marca){
        this.marca = marca;
    }

    public void setModel(String model){
        this.model = model;
    }

    @Override
    public String toString(){
        return "id=" + getId() + ", marca=" + marca + ", model=" + model +" ";
    }

    @Override
    public boolean  equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass())
            return false;
        if (obj == this)
            return true;
        return this.getId() == ((Masina) obj).getId();
    }
    

}
