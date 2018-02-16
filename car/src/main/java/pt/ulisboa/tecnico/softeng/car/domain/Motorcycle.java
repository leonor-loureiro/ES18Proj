package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashSet;
import java.util.Set;

public class Motorcycle extends Vehicle{
    public static Set<Motorcycle> motorcycles = new HashSet<>();

    public Motorcycle(String plate, int kilometers, RentACar rentACar) {
        super(plate, kilometers, rentACar);
        motorcycles.add(this);
    }

}
