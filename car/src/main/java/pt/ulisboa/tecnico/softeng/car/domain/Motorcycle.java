package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;

public class Motorcycle extends Vehicle {

	public Motorcycle(String _plate, int _kilometer, RentACar _rentACar) {
		super(_plate, _kilometer, _rentACar);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isFree(LocalDate begin, LocalDate end) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void rent(String drivingLicense, LocalDate begin, LocalDate end) {
		// TODO Auto-generated method stub

	}

}