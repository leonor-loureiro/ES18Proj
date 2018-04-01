package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentingConstructorTest {
	private static final String RENT_A_CAR_NAME = "Eartz";
	private static final String PLATE_CAR = "22-33-HZ";
	private static final String DRIVING_LICENSE = "br112233";
	private static final LocalDate date1 = LocalDate.parse("2018-01-06");
	private static final LocalDate date2 = LocalDate.parse("2018-01-07");
	private Car car;

	private static final String NIF1 = "123456789"; // novo
	private static final String IBAN1 = "ES061"; // novo
	private static final int PRICE = 50; 
	private static final String clientNIF = "135792468";
	private static final String clientIBAN = "ES063";
	
	@Before
	public void setUp() {
		RentACar rentACar1 = new RentACar(RENT_A_CAR_NAME, NIF1, IBAN1); // novo
		this.car = new Car(PLATE_CAR, 10, PRICE, rentACar1); 
	}

	@Test
	public void success() {
		Renting renting = new Renting(DRIVING_LICENSE, date1, date2, car, clientNIF, clientIBAN);
		assertEquals(DRIVING_LICENSE, renting.getDrivingLicense());
	}

	@Test(expected = CarException.class)
	public void nullDrivingLicense() {
		new Renting(null, date1, date2, car, clientNIF, clientIBAN);
	}

	@Test(expected = CarException.class)
	public void emptyDrivingLicense() {
		new Renting("", date1, date2, car, clientNIF, clientIBAN);
	}

	@Test(expected = CarException.class)
	public void invalidDrivingLicense() {
		new Renting("12", date1, date2, car, clientNIF, clientIBAN);
	}

	@Test(expected = CarException.class)
	public void nullBegin() {
		new Renting(DRIVING_LICENSE, null, date2, car, clientNIF, clientIBAN);
	}

	@Test(expected = CarException.class)
	public void nullEnd() {
		new Renting(DRIVING_LICENSE, date1, null, car, clientNIF, clientIBAN);
	}
	
	@Test(expected = CarException.class)
	public void endBeforeBegin() {
		new Renting(DRIVING_LICENSE, date2, date1, car, clientNIF, clientIBAN);
	}

	@Test(expected = CarException.class)
	public void nullCar() {
		new Renting(DRIVING_LICENSE, date1, date2, null, clientNIF, clientIBAN);
	}
	
	@Test(expected = CarException.class)
	public void nullClientNIF() {
		new Renting(DRIVING_LICENSE, date1, date2, car, null, clientIBAN);
	}

	@Test(expected = CarException.class)
	public void biggerClientNIF() {
		new Renting(DRIVING_LICENSE, date1, date2, car, "1357924680", clientIBAN);
	}
	
	@Test(expected = CarException.class)
	public void smallerClientNIF() {
		new Renting(DRIVING_LICENSE, date1, date2, car, "13579246", clientIBAN);
	}
	
	@Test(expected = CarException.class)
	public void nullClientIBAN() {
		new Renting(DRIVING_LICENSE, date1, date2, car, clientNIF, null);
	}
	
	@Test(expected = CarException.class)
	public void smallerClientIBAN() {
		new Renting(DRIVING_LICENSE, date1, date2, car, clientNIF, "ES06");
	}
	
	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
	}

}
