package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

public abstract class Vehicle {
	private String plate;
	private int kilometer;
	private RentACar rentACar;
	private ArrayList<Renting> listRents;
	
	
	public Vehicle(String plate, int kilometer, RentACar rentACar) {
		super();
		this.plate = plate;
		this.kilometer = kilometer;
		this.rentACar = rentACar;
	}
	public abstract boolean isFree(LocalDate begin, LocalDate end);
	public abstract String rent(String drivingLicense, LocalDate begin, LocalDate end);
	
	
	public RentACar getRentACar() {
		return this.rentACar;
	}
	
	public String getPlate() {
		return this.plate;
	}
	
	public List<Renting> getRentings() {
		return this.listRents;
	}
	
	public void addLog(Renting renting) {
		this.listRents.add(renting);
	}
}
