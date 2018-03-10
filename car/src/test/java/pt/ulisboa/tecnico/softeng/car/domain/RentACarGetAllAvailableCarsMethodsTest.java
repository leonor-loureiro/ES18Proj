package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.dataobject.RentingData;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACarGetAllAvailableCarsMethodsTest {
	private RentACar rentACar;
	private String drivingLicense = "A123456789";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 23);
	private final LocalDate begin2 = new LocalDate(2016, 12, 21);
	private final LocalDate end2 = new LocalDate(2016, 12, 24);
	
	
	@Before
	public void setUp() {
		rentACar = new RentACar("The Bugging");
	}

	
	@Test
	public void successEmpty() {
		Assert.assertNull(rentACar.getAllAvailableCars(begin, end));
	}

	/**
	 * Adds 3 cars and rents one of them.
	 * Proceeds to check if they're found available or not
	 */
	@Test
	public void success() {
		Car car1 = new Car("AN-FI-FO", 1, rentACar);
		Car car2 = new Car("ST-EV-EH", 1, rentACar);
		Car car3 = new Car("SY-ST-EM", 1, rentACar);
		car3.rent(drivingLicense, begin2, end2);
		
		Assert.assertTrue(rentACar.getAllAvailableCars(begin, end).contains(car1));
		Assert.assertTrue(rentACar.getAllAvailableCars(begin, end).contains(car2));
		Assert.assertFalse(rentACar.getAllAvailableCars(begin, end).contains(car3));
	}
	
	
	@Test
	public void successWithCheckout() {
		Car car = new Car("AN-FI-FO", 1, rentACar);
		
		String reference = car.rent(drivingLicense, begin, end2);
		Assert.assertFalse(rentACar.getAllAvailableCars(begin, end).contains(car));
		
		Renting renting = rentACar.getRenting(reference);
		renting.checkOut(20);
		Assert.assertTrue(rentACar.getAllAvailableCars(begin, end).contains(car));
		
		car.rent(drivingLicense, begin, end2);
		Assert.assertFalse(rentACar.getAllAvailableCars(begin, end).contains(car));
	}
	
	@Test(expected = CarException.class)
	public void dateEndSwitchedWithBegin() {
		rentACar.getAllAvailableCars(end, begin);
	}
	
	@Test(expected = CarException.class)
	public void nullBeginDate() {
		rentACar.getAllAvailableCars(null, end);
	}
	
	@Test(expected = CarException.class)
	public void nullEndDate() {
		rentACar.getAllAvailableCars(begin, null);
	}
		
}
