package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentingConflictTest {
	private static final String PLATE_CAR = "22-33-HZ";
	private static final String DRIVING_LICENSE = "br112233";
	private static final LocalDate date0 = LocalDate.parse("2018-01-05");
	private static final LocalDate date1 = LocalDate.parse("2018-01-06");
	private static final LocalDate date2 = LocalDate.parse("2018-01-07");
	private static final LocalDate date3 = LocalDate.parse("2018-01-08");
	private static final LocalDate date4 = LocalDate.parse("2018-01-09");
	private static final String RENT_A_CAR_NAME = "Eartz";
	private Car car;


	@Before
	public void setUp() {
		RentACar rentACar = new RentACar(RENT_A_CAR_NAME);
		this.car = new Car(PLATE_CAR, 10, rentACar);
	}

	@Test()
	public void checkAvailability() {
		Renting renting = new Renting(DRIVING_LICENSE, date1, date2, car);
		assertFalse(renting.conflict(date3, date4));
		assertFalse(renting.conflict(date3, date3));
		assertTrue(renting.conflict(date2, date3));
		assertTrue(renting.conflict(date1, date1));
		assertTrue(renting.conflict(date0, date3));
	}

	@Test(expected = CarException.class)
	public void endBeforeBeginCheckAvailability() {
		Renting renting = new Renting(DRIVING_LICENSE, date1, date2, car);
		renting.conflict(date2, date1);
	}
	
	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
	}
}
