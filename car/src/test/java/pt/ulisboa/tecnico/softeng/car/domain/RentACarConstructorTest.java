package pt.ulisboa.tecnico.softeng.car.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACarConstructorTest {
	
	
	@Test
	public void success() {
		RentACar rent = new RentACar("Maven1337");
		Assert.assertEquals("Maven1337", rent.getName());
	}
	
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
	
	@After
	public void tearDown() {
		RentACar.rentACars.clear();
	}
}
