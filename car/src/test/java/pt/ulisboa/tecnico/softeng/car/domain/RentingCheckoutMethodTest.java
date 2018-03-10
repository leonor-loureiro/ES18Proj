package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.RentingException;


public class RentingCheckoutMethodTest {
	private Renting renting;
	private String drivingLicense = "A123456789";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private final String plate = "ST-EV-EH";
	
	@Before
	public void setUp() {
		renting = new Renting(plate, drivingLicense, begin, end);
	}
	
	@Test
	public void success() {
		renting.checkOut(5);
	}
	
	@Test(expected = RentingException.class)
	public void doubleCheckout() {
		renting.checkOut(0);
		renting.checkOut(200);
	}
	
	@Test(expected = RentingException.class)
	public void NegativeKilometers1() {
		renting.checkOut(-1);
	}
	
	@Test(expected = RentingException.class)
	public void NegativeKilometers2() {
		renting.checkOut(-500);
	}
	
}
