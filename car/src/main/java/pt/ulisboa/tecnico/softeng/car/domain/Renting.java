package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class Renting {
    public static final Map<String, Renting> rentings = new HashMap<>();

    private static int counter;

    private final String reference;
    private final String drivingLicense;
    private LocalDate begin;
    private LocalDate end;
    private int kilometers = -1;
    private final Vehicle vehicle;
    
    public Renting(String drivingLicense, LocalDate begin, LocalDate end,
            Vehicle vehicle) {
    	checkArguments(drivingLicense, begin, end, vehicle);
        this.reference = Integer.toString(++Renting.counter);
        this.drivingLicense = drivingLicense;
        this.begin = begin;
        this.end = end;
        this.vehicle = vehicle;
        
        rentings.put(this.reference, this);
    }

	private void checkArguments(String drivingLicense, LocalDate begin,
								LocalDate end, Vehicle vehicle) {
		if (drivingLicense == null || begin == null || end == null || vehicle == null || end.isBefore(begin))
			throw new CarException();
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
    
    public boolean conflict(LocalDate begin, LocalDate end) {
        if (end.isBefore(begin)) {
            throw new CarException("Error: end date is before begin date.");
        }
        else if ((begin.equals(this.getBegin()) || begin.isAfter(this.getBegin()))
                && (begin.isBefore(this.getEnd()) || begin.equals(this.getEnd()))) {
            return true;
        }
        else if ((end.equals(this.getEnd()) || end.isBefore(this.getEnd()))
                && (end.isAfter(this.getBegin()) || end.isEqual(this.getBegin()))) {
            return true;
        }
        else if ((begin.isBefore(this.getBegin()) && end.isAfter(this.getEnd()))) {
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

    public static Renting getRenting(String reference) {
        return rentings.get(reference);
    }
    
}
