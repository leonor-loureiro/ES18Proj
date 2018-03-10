package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.RentingException;

public class RentingConstructorTest {
	private String drivingLicense = "A123456789";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private final String plate = "ST-EV-EH";
	
	
	@Test
	public void success() {
		new Renting(plate, drivingLicense, begin, end);
	}
	
	@Test
	public void successSingleDayRenting() {
		new Renting(plate, drivingLicense, begin, begin);
		new Renting(plate, drivingLicense, end, end);
	}
	
	@Test(expected = RentingException.class)
	public void switchedBeginWithEnd() {
		new Renting(plate, drivingLicense, end, begin);
	}
	
	@Test( expected = RentingException.class)
	public void emptyPlate() {
		new Renting(" ", drivingLicense, begin, end);
	}
	
	@Test( expected = RentingException.class)
	public void nullPlate() {
		new Renting( null, drivingLicense, begin, end);
	}
	
	@Test( expected = RentingException.class)
	public void wrongFormatDrivingLicense1() {
		new Renting(plate, "A", begin, end);
	}
	
	@Test( expected = RentingException.class)
	public void wrongFormatDrivingLicense2() {
		new Renting(plate, "1238974", begin, end);
	}
	
	@Test( expected = RentingException.class)
	public void emptyDrivingLicense() {
		new Renting(plate, " ", begin, end);
	}
	
	@Test( expected = RentingException.class)
	public void nullDrivingLicense() {
		new Renting(plate, null, begin, end);
	}
	
	@Test( expected = RentingException.class)
	public void nullDateBegin() {
		new Renting(plate, drivingLicense, null, end);
	}
	
	@Test( expected = RentingException.class)
	public void nullDateEnd() {
		new Renting(plate, drivingLicense, begin, null);
	}
	
}
