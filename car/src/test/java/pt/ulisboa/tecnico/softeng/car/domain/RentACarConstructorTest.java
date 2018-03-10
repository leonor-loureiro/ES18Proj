package pt.ulisboa.tecnico.softeng.car.domain;

import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACarConstructorTest {
	
	
	@Test(expected = CarException.class)
	public void nullName() {
		new RentACar(null);
	}

	@Test(expected = CarException.class)
	public void emptyName() {
		new RentACar("");
	}

	@Test(expected = CarException.class)
	public void emptyName2() {
		new RentACar("            ");
	}
	
	@Test
	public void success() {
		new RentACar("Maven1337");
	}
}
