package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class BookingConstructorTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private Hotel hotel;

	private static final String NIF = "123456789"; // novo
	private static final String IBAN = "ES061"; // novo
	
	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Londres", NIF, IBAN); // novo
	}

	@Test
	public void success() {
		Booking booking = new Booking(this.hotel, this.arrival, this.departure);

		Assert.assertTrue(booking.getReference().startsWith(this.hotel.getCode()));
		Assert.assertTrue(booking.getReference().length() > Hotel.CODE_SIZE);
		Assert.assertEquals(this.arrival, booking.getArrival());
		Assert.assertEquals(this.departure, booking.getDeparture());
	}

	@Test(expected = HotelException.class)
	public void nullHotel() {
		new Booking(null, this.arrival, this.departure);
	}

	@Test(expected = HotelException.class)
	public void nullArrival() {
		new Booking(this.hotel, null, this.departure);
	}

	@Test(expected = HotelException.class)
	public void nullDeparture() {
		new Booking(this.hotel, this.arrival, null);
	}

	@Test(expected = HotelException.class)
	public void departureBeforeArrival() {
		new Booking(this.hotel, this.arrival, this.arrival.minusDays(1));
	}

	@Test
	public void arrivalEqualDeparture() {
		new Booking(this.hotel, this.arrival, this.arrival);
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
