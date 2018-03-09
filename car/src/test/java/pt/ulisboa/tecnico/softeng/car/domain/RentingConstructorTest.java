package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RentingConstructorTest {
	private RentACar rentACar;
	private String rentACarCode;
	private String drivingLicense = "A123456789";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private final String plate = "ST-EV-EH";
	
	@Before
	public void setUp() {
		rentACar = new RentACar("test");
		rentACarCode = rentACar.getCode();
	}
	
	@Test
	public void testConstructorSuccess() {
		new Renting(plate, drivingLicense, rentACarCode, begin, end);
	}
	
}
