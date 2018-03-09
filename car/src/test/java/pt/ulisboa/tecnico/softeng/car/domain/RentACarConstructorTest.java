package pt.ulisboa.tecnico.softeng.car.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



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
		Assert.assertNotNull(new RentACar("Maven1337"));
	}
}
