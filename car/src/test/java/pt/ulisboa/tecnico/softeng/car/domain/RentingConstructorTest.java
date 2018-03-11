package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.RentingException;

public class RentingConstructorTest {
	private String drivingLicense = "A123456789";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private RentACar rentACar;
	private Car car;
	
	@Before
	public void setUp() {
		rentACar = new RentACar("tester");
		car = new Car("XX-XX-XX", 10, rentACar);	
	}
	
	@Test
	public void success() {
		new Renting(car, drivingLicense, begin, end);
	}
	
	@Test
	public void successSingleDayRenting() {
		new Renting(car, drivingLicense, begin, begin);
		new Renting(car, drivingLicense, end, end);
	}
	
	@Test(expected = RentingException.class)
	public void switchedBeginWithEnd() {
		new Renting(car, drivingLicense, end, begin);
	}
	
	
	@Test( expected = RentingException.class)
	public void nullPlate() {
		new Renting( null, drivingLicense, begin, end);
	}
	
	@Test( expected = RentingException.class)
	public void wrongFormatDrivingLicense1() {
		new Renting(car, "A", begin, end);
	}
	
	@Test( expected = RentingException.class)
	public void wrongFormatDrivingLicense2() {
		new Renting(car, "1238974", begin, end);
	}
	
	@Test( expected = RentingException.class)
	public void emptyDrivingLicense() {
		new Renting(car, " ", begin, end);
	}
	
	@Test( expected = RentingException.class)
	public void nullDrivingLicense() {
		new Renting(car, null, begin, end);
	}
	
	@Test( expected = RentingException.class)
	public void nullDateBegin() {
		new Renting(car, drivingLicense, null, end);
	}
	
	@Test( expected = RentingException.class)
	public void nullDateEnd() {
		new Renting(car, drivingLicense, begin, null);
	}
	
}
