package pt.ulisboa.tecnico.softeng.car.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.car.domain.RentACar;

public class RentACarData {
    private String name;
    private String nif;
    private String iban;

    public RentACarData() {
    }

    public RentACarData(RentACar rentACar) {
        this.name = rentACar.getName();
        this.nif  = rentACar.getNif();
        this.iban = rentACar.getIban();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }
}
