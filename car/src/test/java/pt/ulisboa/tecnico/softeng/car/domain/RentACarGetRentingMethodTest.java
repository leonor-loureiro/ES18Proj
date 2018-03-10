package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACarGetRentingMethodTest {
	private RentACar rentACar;
	private String drivingLicense = "A123456789";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 23);
	
	
	@Before
	public void setUp() {
		rentACar = new RentACar("The Bugging");
	}
	
	@Test
	public void success() {
		Car car = new Car("BA-TM-AN", 2, rentACar);
		String reference = car.rent(drivingLicense, begin, end);
		
		Renting renting = rentACar.getRenting(reference);
		
		Assert.assertNotNull(renting);
		Assert.assertEquals("BA-TM-AN", renting.getPlate());
	}
	
	@Test
	public void wrongButValidReference() {
		Assert.assertNull(rentACar.getRenting("123756789"));
	}
	
	@Test(expected = CarException.class)
	public void emptyReference() {
		rentACar.getRenting("");
	}
	
	@Test(expected = CarException.class)
	public void nullReference() {
		rentACar.getRenting(null);
	}
	
	@Test(expected = CarException.class)
	public void emptyReference2() {
		rentACar.getRenting("           ");
	}
	
}
