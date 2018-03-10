package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.RentingException;


public class CarRentMethodTest {
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
	public void success() {
		Assert.assertNotNull(car.rent(drivingLicense, begin, end));
	}
	
	@Test
	public void successSameDay() {
		Assert.assertNotNull(car.rent(drivingLicense, begin, begin));
	}	
	
	@Test(expected = RentingException.class)
	public void rentingAlreadRentedCar() {
		Assert.assertNotNull(car.rent(drivingLicense, begin, begin));
		car.rent(drivingLicense, begin, end);
	}
	
	@Test(expected = RentingException.class)
	public void switchedStartWithEnd() {
		car.rent(drivingLicense, end, begin);
	}
	
	@Test(expected = RentingException.class)
	public void nullBeginDate() {
		car.rent(drivingLicense, null, begin);
	}
	
	@Test(expected = RentingException.class)
	public void nullEndDate() {
		car.rent(drivingLicense, end, null);
	}
	
	@Test(expected = RentingException.class)
	public void emptyLicense() {
		car.rent(" ", end, begin);
	}
	
	@Test(expected = RentingException.class)
	public void nullLicense() {
		car.rent(null, end, begin);
	}
		
}