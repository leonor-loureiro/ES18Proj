package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACarTest {
	private static final String NAME = "eartz";
	private static final String PLATE_CAR = "aa-00-11";
	private static final String DRIVING_LICENSE = "br123";

	@Test
	public void constructorSuccess() {
		RentACar rentACar = new RentACar(NAME);

		assertEquals(NAME, rentACar.getName());
	}

	@Test(expected = CarException.class)
	public void constructorFailNull() {
		new RentACar(null);
	}
	
	@Test(expected = CarException.class)
	public void constructorFailEmpty() {
		new RentACar("");
	}

	@Test
	public void getRenting() {
		RentACar rentACar = new RentACar("Eartz");
		Vehicle car = new Car(PLATE_CAR, 10, rentACar);
		Renting renting = car.rent(DRIVING_LICENSE, new LocalDate(), new LocalDate());
		Renting rentingLookedUp = RentACar.getRenting(renting.getReference());
		Renting rentingLookedUpNull = RentACar.getRenting("a");
		assertEquals(renting, rentingLookedUp);
		assertNull(rentingLookedUpNull);
	}

	@Test
	public void getRentingData() {
		RentACar rentACar = new RentACar("Eartz");
		Vehicle car = new Car(PLATE_CAR, 10, rentACar);
		Renting renting = car.rent(DRIVING_LICENSE, new LocalDate(), new LocalDate());

		RentingData rentingData = RentACar.getRentingData(renting.getReference());
		assertEquals(renting.getReference(), rentingData.getReference());
		assertEquals(DRIVING_LICENSE, rentingData.getDrivingLicense());
		assertEquals(PLATE_CAR, rentingData.getPlate());
		assertEquals(rentACar.getCode(), rentingData.getRentACarCode());
	}
	@Test(expected = CarException.class)
	public void getRentingDataFail() {
		RentACar.getRentingData("1");
	}

	@After
	public void tearDown() {
		RentACar.rentACars.clear();
	}

}
