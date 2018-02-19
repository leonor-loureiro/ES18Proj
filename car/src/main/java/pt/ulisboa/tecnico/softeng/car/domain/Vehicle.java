package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public abstract class Vehicle {
    public static final Set<Vehicle> vehicles = new HashSet<>();

    private final String plate;
    private int kilometers;
    private final RentACar rentACar;
    private final Set<Renting> rentings = new HashSet<>();
    
    public Vehicle(String plate, int kilometers, RentACar rentACar) {
        checkArguments(plate, kilometers, rentACar);
        
        this.plate = plate;
        this.kilometers = kilometers;
        this.rentACar = rentACar;
        
        vehicles.add(this);
    }
    private void checkArguments(String plate, int kilometers, RentACar rentACar) {
        if (plate == null || plate.isEmpty()){
            throw new CarException();
        }
        else if (kilometers < 0){
            throw new CarException();
        }
        else if (rentACar == null){
            throw new CarException();
        }
    }
    /**
     * @return the plate
     */
    public String getPlate() {
        return plate;
    }
    /**
     * @return the kilometers
     */
    public int getKilometers() {
        return kilometers;
    }
    /**
     * @param kilometers the kilometers to set
     */
    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }
    /**
     * @param kilometers the kilometers to set
     */
    public void addKilometers(int kilometers) {
        this.kilometers += kilometers;
    }
    /**
     * @return the rentACar
     */
    public RentACar getRentACar() {
        return rentACar;
    }
    
    public boolean isFree(LocalDate begin, LocalDate end) {
        for (Renting renting : rentings) {
            if (renting.conflict(begin, end)){
                return false;
            }
        }
        return true;
    }
    
    
    
    public Renting rent(String drivingLicense, LocalDate begin, LocalDate end) {
        if (begin == null || end == null || !isFree(begin, end)) {
            throw new CarException();
        }
        
        Renting renting = new Renting(drivingLicense, begin, end, this);
        this.rentings.add(renting);

        return renting;
    }

    public int getNumberOfRentings() {
        return this.rentings.size();
    }
    
}
