package pt.ulisboa.tecnico.softeng.car.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.car.domain.Vehicle;

public class VehicleData {
    private String rentACarCode;
    private String plate;
    private int kilometers;
    private double price;
    private String isCar;

    public VehicleData() {}

    public VehicleData(Vehicle v) {
        this.plate = v.getPlate();
        this.kilometers = v.getKilometers();
        this.price = v.getPrice();
        this.rentACarCode = v.getRentACar().getCode();
    }

    public String getRentACarCode() {
        return rentACarCode;
    }

    public void setRentACarCode(String rentACarCode) {
        this.rentACarCode = rentACarCode;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getIsCar() {
        return isCar;
    }

    public void setIsCar(String isCar) {
        this.isCar = isCar;
    }
}
