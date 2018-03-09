package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.RentingException;


public class RentingMethodsTest {
	private RentACar rentACar;
	private String rentACarCode;
	private Renting renting;
	private String drivingLicense = "A123456789";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private final LocalDate begin2 = new LocalDate(2016, 12, 22);
	private final LocalDate end2 = new LocalDate(2016, 12, 24);
	private final String plate = "ST-EV-EH";
	
	@Before
	public void setUp() {
		rentACar = new RentACar("test");
		rentACarCode = rentACar.getCode();
		renting = new Renting(plate, drivingLicense, rentACarCode, begin, end);
	}
	
	@Test
	public void testConflict() {
		Assert.assertTrue(renting.conflict(begin, end));
		Assert.assertTrue(renting.conflict(end, end2));
		Assert.assertFalse(renting.conflict(begin2, end2));
	}
	
	@Test
	public void testCheckOutCheckOutSuccess() {
		renting.checkOut(0);
		renting.checkOut(200);
	}
	
	@Test(expected = RentingException.class)
	public void testCheckOutBadKilometers() {
		renting.checkOut(-1);
		renting.checkOut(-500);
	}

}
