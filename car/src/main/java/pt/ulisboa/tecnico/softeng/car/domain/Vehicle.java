package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.exception.RentingException;

public abstract class Vehicle {
	public static Set<String> plates      = new HashSet<>();
	private final List<Renting> listRents = new ArrayList<>();
	
	private String plate;
	private int kilometers;
	private RentACar rentACar;
	
	
	public Vehicle(String plate, int kilometer, RentACar rentACar) {
		checkArguments(plate, kilometer, rentACar);
		
		this.plate      = plate;
		this.kilometers = kilometer;
		this.rentACar = rentACar;
		
		rentACar.addVehicle(this);
		Vehicle.plates.add(plate);
	}
	
	private boolean checkDates(LocalDate begin, LocalDate end) {
		if (begin == null || end == null || begin.isAfter(end))
			return false;
		return true;
	}
	
	private void checkArguments(String plate, int kilometers, RentACar rentACar) {
		if (plate == null || rentACar == null)
			throw new CarException("Vehicle Exception: Values cannot be null.");
		
		if (kilometers < 0)
			throw new CarException("Vehicle Exception: Kilometers cannot be negative.");
		
		if (!plate.matches("^[a-zA-Z0-9]{2}-[a-zA-Z0-9]{2}-[a-zA-Z0-9]{2}$") || Vehicle.plates.contains(plate))
			throw new CarException("Vehicle Exception: Invalid plate number \"" + plate + "\".");
	}
	
	public boolean isFree(LocalDate begin, LocalDate end) {
		if (!checkDates(begin, end))
			throw new CarException("Vehicle Exception: Invalid dates.");
		return this.listRents.stream().allMatch(rent -> !rent.conflict(begin, end));
	}
	
	public String rent(String drivingLicense, LocalDate begin, LocalDate end) {
		if (!checkDates(begin, end))
			throw new RentingException("Vehicle Exception: Invalid dates.");
		if (!this.isFree(begin, end))
			throw new RentingException("Vehicle Exception: vehicle already rented for that period.");
		if (!drivingLicense.matches("^[a-zA-Z]\\d+"))
			throw new RentingException("Vehicle Exception: Invalid driving license.");
		
		return new Renting(this, drivingLicense, begin, end).getReference();
	}
	
	public RentACar getRentACar() {
		return this.rentACar;
	}
	
	public String getPlate() {
		return this.plate;
	}
	
	public int getKilometers() {
		return this.kilometers;
	}
	
	public List<Renting> getRentings() {
		return this.listRents;
	}
	
	public void addKilometers(int kilometers) {
		this.kilometers += kilometers;
	}
	
	public void addLog(Renting renting) {
		this.listRents.add(renting);
	}
}
