package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class Renting {
    public static Set<Renting> rentings = new HashSet<>();

    private static int counter;

    private final String reference;
    private String drivingLicense;
    private LocalDate begin;
    private LocalDate end;
    private int kilometers = -1;
    private Vehicle vehicle;
    
    public Renting(String drivingLicense, LocalDate begin, LocalDate end,
            Vehicle vehicle) {
        super();
        this.reference = Integer.toString(++Renting.counter);
        this.drivingLicense = drivingLicense;
        this.begin = begin;
        this.end = end;
        this.vehicle = vehicle;
        
        rentings.add(this);
    }
    /**
     * @return the reference
     */
    public String getReference() {
        return reference;
    }
    /**
     * @return the drivingLicense
     */
    public String getDrivingLicense() {
        return drivingLicense;
    }
    /**
     * @param drivingLicense the drivingLicense to set
     */
    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }
    /**
     * @return the begin
     */
    public LocalDate getBegin() {
        return begin;
    }
    /**
     * @param begin the begin to set
     */
    public void setBegin(LocalDate begin) {
        this.begin = begin;
    }
    /**
     * @return the end
     */
    public LocalDate getEnd() {
        return end;
    }
    /**
     * @param end the end to set
     */
    public void setEnd(LocalDate end) {
        this.end = end;
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
     * @return the vehicle
     */
    public Vehicle getVehicle() {
        return vehicle;
    }
    /**
     * @param vehicle the vehicle to set
     */
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
    
    public boolean conflict(LocalDate begin, LocalDate end) {
        if (end.isBefore(begin)) {
            throw new CarException();
        }
        else if ((begin.equals(this.begin) || begin.isAfter(this.begin)) && begin.isBefore(this.end)) {
            return true;
        }
        else if ((end.equals(this.end) || end.isBefore(this.end)) && end.isAfter(this.begin)) {
            return true;
        }
        else if ((begin.isBefore(this.begin) && end.isAfter(this.end))) {
            return true;
        }

        return false;
    }
    
    public void checkout(int kilometers){
        if (this.getKilometers() > 0){
            throw new CarException();
        }
        this.setKilometers(kilometers);
        this.vehicle.addKilometers(kilometers);
    }
    
}
