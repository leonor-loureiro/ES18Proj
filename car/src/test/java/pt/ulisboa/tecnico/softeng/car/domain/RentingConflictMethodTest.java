package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.RentingException;
import pt.ulisboa.tecnico.softeng.car.domain.Car;
import pt.ulisboa.tecnico.softeng.car.domain.RentACar;

public class RentingConflictMethodTest {
	private Renting renting;
	private String drivingLicense = "A123456789";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private final LocalDate begin2 = new LocalDate(2016, 12, 22);
	private final LocalDate end2 = new LocalDate(2016, 12, 24);
	
	@Before
	public void setUp() {
		RentACar rentACar = new RentACar("tester");
		Car car = new Car("XX-XX-XX", 10, rentACar);
		renting = new Renting(car, drivingLicense, begin, end);
	}
	
	@Test
	public void success() {
		Assert.assertTrue(renting.conflict(begin, end));
		Assert.assertTrue(renting.conflict(begin, begin));
		Assert.assertTrue(renting.conflict(end, end));
		Assert.assertTrue(renting.conflict(end, end2));
		Assert.assertFalse(renting.conflict(begin2, end2));
	}
	
	@Test
	public void successWithCheckout() {
		Assert.assertTrue(renting.conflict(begin, end));
		renting.checkout(30);
		Assert.assertFalse(renting.conflict(begin, end));
	}
	
	@Test(expected = RentingException.class)
	public void invertedBeginWithEnd() {
		renting.conflict(end, begin);
	}
	
	@Test(expected = RentingException.class)
	public void nullBeginDate() {
		renting.conflict(null, end);
	}
	
	@Test(expected = RentingException.class)
	public void nullEndDate() {
		renting.conflict(begin, null);
	}
}
