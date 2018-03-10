package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.dataobject.RentingData;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACarGetRentingDataMethodTest {
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
		RentingData data = rentACar.getRentingData(reference);
	
		Assert.assertEquals(data.getReference(), reference);
	}
	
	@Test
	public void validButWrongReference() {
		Assert.assertNull(rentACar.getRentingData("123756789"));
	}
	
	@Test(expected = CarException.class)
	public void emptyReference() {
		rentACar.getRentingData("");
	}
	
	@Test(expected = CarException.class)
	public void nullReference() {
		rentACar.getRentingData(null);
	}
	
	@Test(expected = CarException.class)
	public void cleanReference() {
		rentACar.getRentingData("     ");
	}

	
}
