package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelGetPriceMethodTest {
	private Hotel hotel;
	private final double priceSingle = 20.0;
	private final double priceDouble = 30.0;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Lisboa", "NIF", "IBAN", this.priceSingle, this.priceDouble);
	}

	@Test
	public void successSingle() {
		assertEquals(this.priceSingle, this.hotel.getPrice(Room.Type.SINGLE), 0.0d);
	}

	@Test
	public void successDouble() {
		assertEquals(this.priceDouble, this.hotel.getPrice(Room.Type.DOUBLE), 0.0d);
	}

	@Test(expected = HotelException.class)
	public void nullType() {
		this.hotel.getPrice(null);
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
}