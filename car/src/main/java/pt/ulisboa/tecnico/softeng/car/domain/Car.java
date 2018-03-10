package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;

public class Car extends Vehicle {
	
	

	public Car(String plate, int kilometer, RentACar rentACar) {
		super(plate, kilometer, rentACar);
	}

	@Override
	public boolean isFree(LocalDate begin, LocalDate end) {
		return false;
	}

	@Override
	public String rent(String drivingLicense, LocalDate begin, LocalDate end) {
		return new Renting(this, drivingLicense, begin, end).getReference();
	}
	
	

}
