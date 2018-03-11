package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;


public class CarIsFreeMethodTest {
	private Car car;
	private RentACar rentACar;
	private String drivingLicense = "A123456789";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private final String plate = "ST-EV-EH";
	
	@Before
	public void setUp() {
		rentACar = new RentACar("Steve");
		car = new Car(plate, 1, rentACar);
	}

	@Test
	public void success() {
		Assert.assertTrue(car.isFree(begin, end));
	}
	
	@Test(expected = CarException.class)
	public void nullBegin() {
		car.isFree(null, end);
	}
	
	@Test (expected = CarException.class)
	public void nullEnd() {
		car.isFree(begin, null);
	}
	
	@Test
	public void successSingleDay() {
		Assert.assertTrue(car.isFree(begin, begin));
	}
	
	@Test
	public void successFreeAfterRentDifferentDay() {
		Assert.assertTrue(car.isFree(end, end));
		car.rent(drivingLicense, end, end);
		Assert.assertTrue(car.isFree(begin, begin));
	}

	@Test
	public void successNotFree() {
		car.rent(drivingLicense, begin, end);
		Assert.assertFalse(car.isFree(begin, end));
	}
	
	@Test
	public void successNotFreeAfterRent() {
		Assert.assertTrue(car.isFree(begin, end));
		car.rent(drivingLicense, begin, end);
		Assert.assertFalse(car.isFree(end, end));
	}
	
	@Test
	public void successWithCheckout() {
		String reference = car.rent(drivingLicense, begin, end);
		Assert.assertFalse(car.isFree(begin, end));
		
		Renting renting = rentACar.getRenting(reference);
		renting.checkout(20);
		Assert.assertTrue(car.isFree(begin, begin));
	}
	
	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
	}

}
