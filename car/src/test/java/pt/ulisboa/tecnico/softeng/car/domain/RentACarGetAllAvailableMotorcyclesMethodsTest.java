package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACarGetAllAvailableMotorcyclesMethodsTest {
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
		Assert.assertTrue(rentACar.getAllAvailableMotorcycles(begin, end).isEmpty());
	}
	
	@Test
	public void successNonConflictRent() {
		Motorcycle bike = new Motorcycle("AN-FI-FO", 1, rentACar);
		LocalDate begin3 = new LocalDate(2016, 12, 25);
		LocalDate end3 = new LocalDate(2016, 12, 26);
		bike.rent(drivingLicense, begin3, end3);
		
		Assert.assertTrue(rentACar.getAllAvailableMotorcycles(begin, end).contains(bike));
	}

	/**
	 * Adds 3 bikes and rents one of them.
	 * Proceeds to check if they're found available or not
	 */
	@Test
	public void success() {
		Motorcycle bike1 = new Motorcycle("AN-FI-FO", 1, rentACar);
		Motorcycle bike2 = new Motorcycle("ST-EV-EH", 1, rentACar);
		Motorcycle bike3 = new Motorcycle("SY-ST-EM", 1, rentACar);
		bike3.rent(drivingLicense, begin2, end2);
		
		Assert.assertTrue(rentACar.getAllAvailableMotorcycles(begin, end).contains(bike1));
		Assert.assertTrue(rentACar.getAllAvailableMotorcycles(begin, end).contains(bike2));
		Assert.assertFalse(rentACar.getAllAvailableMotorcycles(begin, end).contains(bike3));
	}
	
	@Test(expected = CarException.class)
	public void dateEndSwitchedWithBegin() {
		rentACar.getAllAvailableMotorcycles(end, begin);
	}
	
	@Test(expected = CarException.class)
	public void nullBeginDate() {
		rentACar.getAllAvailableMotorcycles(null, end);
	}
	
	@Test(expected = CarException.class)
	public void nullEndDate() {
		rentACar.getAllAvailableMotorcycles(begin, null);
	}
	
	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
	}
}
