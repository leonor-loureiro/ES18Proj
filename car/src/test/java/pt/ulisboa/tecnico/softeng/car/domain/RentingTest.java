package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RentingTest {
	private RentACar rentACar;
	private String rentACarCode;
	private String drivingLicense = "A123456789";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private final LocalDate begin2 = new LocalDate(2016, 12, 22);
	private final LocalDate end2 = new LocalDate(2016, 12, 24);
	private final String plate = "ST-EV-EH";
	
	@Before
	public void setUp() {
		rentACar = new rentACar("test");
		rentACarCode = rentACar.getCode();
	}
	
	@Test
	public void testConstructorSuccess() {
		new Renting(plate, drivingLicense, rentACarCode, begin, end);
	}
	
	
	// Methods
	@Test
	public void testConflict() {
		Assert.assertTrue(renting.conflict(begin, end));
	}
	
	@Test
	public void testConflict2() {
		Assert.assertTrue(renting.conflict(end, end2));
	}
	
	@Test
	public void testConflict3() {
		Assert.assertFalse(renting.conflict(begin2, end2));
	}
	
	
	@Test
	public void testCheckOutCheckOutSuccess1() {
		renting.checkOut(0);
	}
	
	@Test
	public void testCheckOutSuccess2() {
		renting.checkOut(200);
	}
	
	@Test(expected = RentingException.class)
	public void testCheckOutBadKilometers() {
		renting.checkOut(-1);
	}
	
	@Test(expected = RentingException.class)
	public void testCheckOutBadKilometers2() {
		renting.checkOut(-500);
	}

}
