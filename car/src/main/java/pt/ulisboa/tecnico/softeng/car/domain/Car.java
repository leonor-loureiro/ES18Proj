package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashSet;
import java.util.Set;

public class Car extends Vehicle{
    public static Set<Car> cars = new HashSet<>();

    public Car(String plate, int kilometers, RentACar rentACar) {
        super(plate, kilometers, rentACar);
        
        cars.add(this);
    }

}
