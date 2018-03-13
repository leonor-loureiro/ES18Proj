package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.RentingException;

public class Renting {
	private static int counter = 0;
	private String reference;
	private String drivingLicense;
	private Vehicle vehicle;
	private LocalDate begin;
	private LocalDate end;
	private int kilometers;

	public Renting(Vehicle vehicle, String drivingLicense, LocalDate begin, LocalDate end) {
		checkArguments(vehicle, drivingLicense, begin, end);
		
		this.reference      = vehicle.getRentACar().getCode() + Integer.toString(++Renting.counter);
		this.drivingLicense = drivingLicense;
		this.vehicle        = vehicle;
		this.begin          = begin;
		this.end            = end;
		this.kilometers     = -1;
		
		vehicle.addLog(this);
	}
	
	private void checkDates(LocalDate begin, LocalDate end) {
		if (begin == null || end == null || begin.isAfter(end))
			throw new RentingException("Renting Exception: Invalid dates.");
	}
	
	private void checkArguments(Vehicle vehicle, String drivingLicense, LocalDate begin, LocalDate end) {
		checkDates(begin, end);
		if (vehicle == null || drivingLicense == null || !drivingLicense.matches("^[a-zA-Z]\\d+"))
			throw new RentingException("Renting Exception: Invalid arguments.");
	}

	public boolean conflict(LocalDate begin, LocalDate end) {
		checkDates(begin, end);
		return (this.begin.isBefore(end) || this.begin.isEqual(end)) 
				&& (this.end.isAfter(begin) || this.end.isEqual(begin));
	}

	public void checkout(int kilometers) {
		if (kilometers < 0)
			throw new RentingException("Renting Exception: kilometers cannot be negative.");
		if (this.kilometers >= 0)
			throw new RentingException("Renting Exception: checkout already registered.");
		this.kilometers = kilometers;
		this.vehicle.addKilometers(kilometers);
	}

	public String getPlate() {
		return this.vehicle.getPlate();
	}

	public String getReference() {
		return this.reference;
	}

	public String getDrivingLicense() {
		return this.drivingLicense;
	}

	public LocalDate getBegin() {
		return this.begin;
	}

	public LocalDate getEnd() {
		return this.end;
	}

	public int getKilometers() {
		return this.kilometers;
	}

	public String getRentACarCode() {
		return this.vehicle.getRentACar().getCode();
	}
	
	
}
