package pt.ulisboa.tecnico.softeng.car;

import org.joda.time.LocalDate;

public abstract class Vehicle {
	private String _plate;
	private int _kilometer;
	private RentACar _rentACar;
	
	
	public Vehicle(String _plate, int _kilometer, RentACar _rentACar) {
		super();
		this._plate = _plate;
		this._kilometer = _kilometer;
		this._rentACar = _rentACar;
	}
	public abstract boolean isFree(LocalDate begin, LocalDate end);
	public abstract void rent(String drivingLicense, LocalDate begin, LocalDate end);
}
