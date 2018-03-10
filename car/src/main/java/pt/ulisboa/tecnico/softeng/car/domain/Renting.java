package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;

public class Renting {
	private static int counter = 0;
	private String reference;
	private String drivingLicense;
	private String rentACarCode;
	private LocalDate begin;
	private LocalDate end;
	private String plate;
	private int kilometers;

	public boolean conflict(LocalDate begin, LocalDate end) {
		return this.begin.isBefore(end) && this.end.isAfter(begin);	
	}

	public int checkOut(int kilometers) {
		return -1;
	}

	public Renting(Vehicle vehicle, String drivingLicense, LocalDate begin, LocalDate end) {
		super();
		this.reference      = vehicle.getRentACar().getCode() + Integer.toString(++Renting.counter);
		this.rentACarCode   = vehicle.getRentACar().getCode();
		this.plate          = vehicle.getPlate();
		this.drivingLicense = drivingLicense;
		this.begin          = begin;
		this.end            = end;
		
		vehicle.addLog(this);
	}

	public String getPlate() {
		return this.plate;
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
		return this.rentACarCode;
	}
	
	
}
