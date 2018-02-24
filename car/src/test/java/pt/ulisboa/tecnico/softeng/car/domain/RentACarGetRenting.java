package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

public class RentACarGetRenting {
	private static final String NAME1 = "eartz";
	private static final String PLATE_CAR1 = "aa-00-11";
	private static final String DRIVING_LICENSE = "br123";
	private static final LocalDate date1 = LocalDate.parse("2018-01-06");
	private static final LocalDate date2 = LocalDate.parse("2018-01-07");
	private static final LocalDate date3 = LocalDate.parse("2018-01-08");
	private static final LocalDate date4 = LocalDate.parse("2018-01-09");
	private Renting renting;

	@Before
	public void setUp() {
		RentACar rentACar1 = new RentACar(NAME1);
		Vehicle car1 = new Car(PLATE_CAR1, 10, rentACar1);
		this.renting = car1.rent(DRIVING_LICENSE, date1, date2);
		car1.rent(DRIVING_LICENSE, date3, date4);
	}

	@Test
	public void getRenting() {
		assertEquals(this.renting, RentACar.getRenting(this.renting.getReference()));
	}

	public void nonExistent() {
		assertNull(RentACar.getRenting("a"));
	}

}
