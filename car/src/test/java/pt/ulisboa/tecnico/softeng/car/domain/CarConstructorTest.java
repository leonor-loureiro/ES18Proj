package pt.ulisboa.tecnico.softeng.car.domain;

import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class CarConstructorTest {
	private RentACar rentACar;
	private final String plate = "ST-EV-EH";
	
	@Before
	public void setUp() {
		rentACar = new RentACar("Steve");
	}
	
	/*
	 * Untested situations that should be checked:
	 * maxInt and minimum int kilometers
	 * Special Characters on Plate
	 */
	
	
	//Constructor Tests
	@Test
	public void successConstructor() {
		new Car(plate, 1, rentACar);
	}
	
	@Test(expected = CarException.class)
	public void badPlateConstructor() {
		new Car("XX-XX-", 10, rentACar);
	}
	
	@Test(expected = CarException.class)
	public void badPlateConstructor2() {
		new Car(null, 500, rentACar);
	}
	
	@Test(expected = CarException.class)
	public void badPlateConstructor3() {
		new Car("          ", 1, rentACar);
	}

	@Test(expected = CarException.class)
	public void badKilometersConstructor() {
		new Car(plate, -500, rentACar);
	}
	
	@Test(expected = CarException.class)
	public void badKilometersConstructor2() {
		new Car(plate, -1, rentACar);
	}
	
	@Test(expected = CarException.class)
	public void badRentACarCarConstructor() {
		new Car(plate, 1, null);
	}
	
	@Test(expected = CarException.class)
	public void samePlateCarConstructor() {
		new Car(plate, 1, rentACar);
		new Car(plate, 5, rentACar);		
	}
}