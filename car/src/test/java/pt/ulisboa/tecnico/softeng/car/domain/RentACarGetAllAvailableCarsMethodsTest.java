package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
		Assert.assertTrue(rentACar.getAllAvailableCars(begin, end).isEmpty());
	}
	
	@Test
	public void successNonConflictRent() {
		Car car1 = new Car("AN-FI-FO", 1, rentACar);
		LocalDate begin3 = new LocalDate(2016, 12, 25);
		LocalDate end3 = new LocalDate(2016, 12, 26);
		car1.rent(drivingLicense, begin3, end3);
		
		Assert.assertTrue(rentACar.getAllAvailableCars(begin, end).contains(car1));
	}

	
	/**
	 * Adds 3 cars and rents one of them.
	 * Proceeds to check if they're found available or not
	 */
	@Test
	public void successMultples() {
		Car car1 = new Car("AN-FI-FO", 1, rentACar);
		Car car2 = new Car("ST-EV-EH", 1, rentACar);
		Car car3 = new Car("SY-ST-EM", 1, rentACar);
		car3.rent(drivingLicense, begin2, end2);
		
		Assert.assertTrue(rentACar.getAllAvailableCars(begin, end).contains(car1));
		Assert.assertTrue(rentACar.getAllAvailableCars(begin, end).contains(car2));
		Assert.assertFalse(rentACar.getAllAvailableCars(begin, end).contains(car3));
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
		
	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
	}
}
