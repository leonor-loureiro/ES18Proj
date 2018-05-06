package pt.ulisboa.tecnico.softeng.car.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.car.domain.Car;
import pt.ulisboa.tecnico.softeng.car.domain.Motorcycle;
import pt.ulisboa.tecnico.softeng.car.domain.RentACar;

import java.util.List;
import java.util.stream.Collectors;

public class RentACarData {
    private String name;
    private String nif;
    private String iban;
    private String code;
    private List<VehicleData> motorcycles;
    private List<VehicleData> cars;


    public RentACarData() {
    }

    public RentACarData(RentACar rentACar) {
        this.name = rentACar.getName();
        this.nif  = rentACar.getNif();
        this.iban = rentACar.getIban();
        this.code = rentACar.getCode();

        this.motorcycles = rentACar.getVehicleSet().stream()
                .filter(a -> a.getClass() == Motorcycle.class)
                .map(VehicleData::new)
                .collect(Collectors.toList());

        this.cars = rentACar.getVehicleSet().stream()
                .filter(a -> a.getClass() == Car.class)
                .map(VehicleData::new)
                .collect(Collectors.toList());
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<VehicleData> getMotorcycles() {
        return motorcycles;
    }

    public void setMotorcycles(List<VehicleData> motorcycles) {
        this.motorcycles = motorcycles;
    }

    public List<VehicleData> getCars() {
        return cars;
    }

    public void setCars(List<VehicleData> cars) {
        this.cars = cars;
    }
}
