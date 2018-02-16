package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACar {
    public static Set<RentACar> rentACars = new HashSet<>();

    private static int counter;
    
    private String name;
    private final String code;
    private final Map<String, Vehicle> vehicles = new HashMap<>();

    public RentACar(String name) {
        checkArguments(name);
        this.name = name;
        this.code = Integer.toString(++RentACar.counter);
        
        rentACars.add(this);
    }
    
    private void checkArguments(String name) {
        if(name == null || name.isEmpty()){
            throw new CarException();
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }
    
    void addVehicle(Vehicle vehicle) {
        if (hasVehicle(vehicle.getPlate())) {
            throw new CarException();
        }
        this.vehicles.put(vehicle.getPlate(), vehicle);
    }

    int getNumberOfVehicles() {
        return this.vehicles.size();
    }

    public boolean hasVehicle(String plate) {
        return vehicles.containsKey(plate);
    }
    
    public Set<Vehicle> getAvailableVehicles(Class<?> cls, LocalDate begin, LocalDate end) {
        Set<Vehicle> vehicles = new HashSet<>();
        for (Vehicle vehicle: this.vehicles.values()) {
            if (cls == vehicle.getClass() && vehicle.isFree(begin, end)){
                vehicles.add(vehicle);
            }
        }
        return vehicles;
    }
    
    public Set<Vehicle> getAvailableMotorcycles(LocalDate begin, LocalDate end) {
        return getAvailableVehicles(Motorcycle.class, begin, end);
    }
    
    public Set<Vehicle> getAvailableCars(LocalDate begin, LocalDate end) {
        return getAvailableVehicles(Car.class, begin, end);
    }
    
    

    private static Set<Vehicle> getAllAvailableVehicles(Class<?> cls, LocalDate begin, LocalDate end) {
        Set<Vehicle> vehicles = new HashSet<>();
        for (RentACar rentACar : rentACars) {
            vehicles.addAll(rentACar.getAvailableVehicles(cls, begin, end)); 
        }
        return vehicles;
    }
    public static Set<Vehicle> getAllAvailableMotorcycles(LocalDate begin, LocalDate end) {
        return RentACar.getAllAvailableVehicles(Motorcycle.class, begin, end);
    }
    
    public static Set<Vehicle> getAllAvailableCars(LocalDate begin, LocalDate end) {
        return getAllAvailableVehicles(Car.class, begin, end);
    }
}
