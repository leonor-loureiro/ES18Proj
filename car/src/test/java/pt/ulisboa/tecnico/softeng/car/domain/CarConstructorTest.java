package pt.ulisboa.tecnico.softeng.car.domain;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



public class CarConstructorTest {
	private RentACar rentACar;
	private final String plate = "ST-EV-EH";
	
	@Before
	public void setUp() {
		rentACar = new RentACar("Steve");
		
		//setup renting of Car
	}
	
	/*
	 * Untested situations that should be checked:
	 * maxInt and minimum int kilometers
	 * Special Characters on Plate
	 */
	
	
	//Constructor Tests
	@Test
	public void successConstructor() {
		Car car = new Car(plate, 1, rentACar);
	}
	
	@Test(expected = CarException.class)
	public void badPlateConstructor() {
		Car car = new Car("XX-XX-", 10, rentACar);
	}
	
	@Test(expected = CarException.class)
	public void badPlateConstructor2() {
		Car car = new Car(null, 500, rentACar);
	}
	
	@Test(expected = CarException.class)
	public void badPlateConstructor3() {
		Car car = new Car("          ", 1, rentACar);
	}

	@Test(expected = CarException.class)
	public void badKilometersConstructor() {
		Car car = new Car(plate, -500, rentACar);
	}
	
	@Test(expected = CarException.class)
	public void badKilometersConstructor2() {
		Car car = new Car(plate, -1, rentACar);
	}
	
	@Test(expected = CarException.class)
	public void badCarRentConstructor() {
		Car car = new Car(plate, 1, null);
	}