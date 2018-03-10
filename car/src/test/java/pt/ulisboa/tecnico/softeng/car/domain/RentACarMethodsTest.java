package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.dataobject.RentingData;


public class RentACarMethodsTest {
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
	public void testGetRenting() {
		Car car = new Car("BA-TM-AN", 2, rentACar);
		String reference = car.rent(drivingLicense, begin, end);
		Renting renting = rentACar.getRenting(reference);
		Assert.assertNotNull(renting);
		Assert.assertEquals("BA-TM-AN", renting.getPlate());
	}

	@Test
	public void testGetRentingNull() {
		Assert.assertNull(rentACar.getRenting(" "));
	}
	
	
	@Test
	public void testGetAllAvailableCarsEmpty() {
		Assert.assertNull(rentACar.getAllAvailableCars());
		
	}

	/**
	 * Adds 3 cars and rents one of them.
	 * Proceeds to check if they're found available or not
	 */
	@Test
	public void testGetAllAvailableCars() {
		Car car1 = new Car("AN-FI-FO", 1, rentACar);
		Car car2 = new Car("ST-EV-EH", 1, rentACar);
		Car car3 = new Car("SY-ST-EM", 1, rentACar);
		car3.rent(drivingLicense, begin2, end2);
		
		Assert.assertTrue(rentACar.getAllAvailableCars(begin, end).contains(car1));
		Assert.assertTrue(rentACar.getAllAvailableCars(begin, end).contains(car2));
		Assert.assertFalse(rentACar.getAllAvailableCars(begin, end).contains(car3));
	}
	
	
	/*
	 * 
	 */
	@Test
	public void testGetAllAvailableCars2() {
		Car car = new Car("AN-FI-FO", 1, rentACar);
		
		String reference = car.rent(drivingLicense, begin, end2);
		Assert.assertFalse(rentACar.getAllAvailableCars(begin, end).contains(car));
		
		Renting renting = rentACar.getRenting(reference);
		renting.checkOut(20);
		Assert.assertTrue(rentACar.getAllAvailableCars(begin, end).contains(car));
		
		car.rent(drivingLicense, begin, end2);
		Assert.assertFalse(rentACar.getAllAvailableCars(begin, end).contains(car));
	}
	
	@Test
	public void testGetAllAvailableMotorcycles() {
		fail("Not yet implemented");
	}

	
	@Test
	public void testGetRentingData() {
		Car car = new Car("BA-TM-AN", 2, rentACar);
		String reference = car.rent(drivingLicense, begin, end);
		RentingData data = rentACar.getRentingData(reference);
	
		Assert.assertEquals(data.getReference(), reference);
	}

}
