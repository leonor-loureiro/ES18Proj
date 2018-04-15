package pt.ulisboa.tecnico.softeng.car.domain;

public class Car extends Car_Base {
    public Car(String plate, int kilometers, double price, RentACar rentACar) {
        init(plate, kilometers, price, rentACar);
    }
}
