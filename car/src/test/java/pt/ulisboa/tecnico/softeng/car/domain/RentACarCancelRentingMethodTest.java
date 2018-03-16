package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACarCancelRentingMethodTest {
	private static final String PLATE_CAR = "22-33-HZ";
	private static final String RENT_A_CAR_NAME = "Eartz";
 	private static final String DRIVING_LICENSE = "lx1423";
	private static final LocalDate BEGIN = LocalDate.parse("2018-01-06");
	private static final LocalDate END = LocalDate.parse("2018-01-09");
	private static final String NIF = "NIF";
 	private static final String IBAN = "IBAN";
	private RentACar rentACar;
	private Car car;
	private Renting renting;

	@Before
	public void setUp() {
		rentACar = new RentACar(RENT_A_CAR_NAME, NIF, IBAN);
		car = new Car(PLATE_CAR, 10, 10, rentACar);

		String reference = RentACar.rent(Car.class, DRIVING_LICENSE, NIF, BEGIN, END);

		renting = RentACar.getRenting(reference);
	}

	@Test
	public void success() {
		String cancel = RentACar.cancelRenting(this.renting.getReference());

		Assert.assertTrue(this.renting.isCancelled());
		Assert.assertEquals(cancel, this.renting.getCancellationReference());
	}

	@Test(expected = CarException.class)
	public void doesNotExist() {
		RentACar.cancelRenting("MISSING_REFERENCE");
	}

	@Test(expected = CarException.class)
	public void nullReference() {
		RentACar.cancelRenting(null);
	}

	@Test(expected = CarException.class)
	public void emptyReference() {
		RentACar.cancelRenting("");
	}

	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
	}
}
