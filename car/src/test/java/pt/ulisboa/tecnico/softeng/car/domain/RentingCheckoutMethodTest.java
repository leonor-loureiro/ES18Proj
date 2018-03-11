package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.RentingException;
import pt.ulisboa.tecnico.softeng.car.domain.Car;
import pt.ulisboa.tecnico.softeng.car.domain.RentACar;

public class RentingCheckoutMethodTest {
	private Renting renting;
	private String drivingLicense = "A123456789";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private final RentACar rentACar = new RentACar("tester");
	private final Car car = new Car("XX-XX-XX", 10, rentACar);
	
	@Before
	public void setUp() {
		renting = new Renting(car, drivingLicense, begin, end);
	}
	
	@Test
	public void success() {
		renting.checkout(5);
	}
	
	@Test(expected = RentingException.class)
	public void doubleCheckout() {
		renting.checkout(0);
		renting.checkout(200);
	}
	
	@Test(expected = RentingException.class)
	public void NegativeKilometers1() {
		renting.checkout(-1);
	}
	
	@Test(expected = RentingException.class)
	public void NegativeKilometers2() {
		renting.checkout(-500);
	}
	
}
