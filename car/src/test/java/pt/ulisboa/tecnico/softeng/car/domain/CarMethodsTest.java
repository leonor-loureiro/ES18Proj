package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;


public class CarMethodsTest {
	private Car car;
	private RentACar rentACar;
	private String drivingLicense = "A123456789";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private final String plate = "ST-EV-EH";
	
	@Before
	public void setUp() {
		/*
		 * If this test is receiving false error reports
		 * Check for the need to destroy car after each test
		 */
		rentACar = new RentACar("Steve");
		car = new Car(plate, 1, rentACar);
	}
	
	@Test
	public void testRent() {
		Assert.assertNotNull(car.rent(drivingLicense, begin, end));
		Assert.assertNotNull(car.rent(drivingLicense, begin, begin));
	}
	
	/**
	 * end date < begin date
	 */
	@Test(expected = CarException.class)
	public void badDateRent() {
		car.rent(drivingLicense, end, begin);
	}
	
	
	@Test
	public void testIsFreeTrue() {
		Assert.assertTrue(car.isFree(begin, end));
		
		car.rent(drivingLicense, end, end);
		Assert.assertTrue(car.isFree(begin, begin));
	}

	@Test
	public void testIsFreeFalse() {
		car.rent(drivingLicense, begin, end);
		Assert.assertFalse(car.isFree(begin, end));
	}
	


}
