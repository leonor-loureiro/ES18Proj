package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VehicleIsFreeTest {

	private static final String PLATE_CAR = "22-33-HZ";
	private static final String RENT_A_CAR_NAME = "Eartz";
	private static final String DRIVING_LICENSE = "lx1423";
	private static final LocalDate date1 = LocalDate.parse("2018-01-06");
	private static final LocalDate date2 = LocalDate.parse("2018-01-07");
	private static final LocalDate date3 = LocalDate.parse("2018-01-08");
	private static final LocalDate date4 = LocalDate.parse("2018-01-09");
	private Car car;

	private static final String NIF1 = "123456789"; // novo
	private static final String IBAN1 = "ES061"; // novo
	private static final int PRICE = 50; // novo
	
	@Before
	public void setUp() {
		RentACar rentACar = new RentACar(RENT_A_CAR_NAME, NIF1, IBAN1); // novo
		this.car = new Car(PLATE_CAR, 10, PRICE, rentACar); // novo
	}

	@Test
	public void noBookingWasMade() {
		assertTrue(car.isFree(date1, date2));
		assertTrue(car.isFree(date1, date3));
		assertTrue(car.isFree(date3, date4));
		assertTrue(car.isFree(date4, date4));
	}

	@Test
	public void bookingsWereMade() {
		car.rent(DRIVING_LICENSE, date2, date2);
		car.rent(DRIVING_LICENSE, date3, date4);

		assertFalse(car.isFree(date1, date2));
		assertFalse(car.isFree(date1, date3));
		assertFalse(car.isFree(date3, date4));
		assertFalse(car.isFree(date4, date4));
		assertTrue(car.isFree(date1, date1));
	}

	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
	}
}
