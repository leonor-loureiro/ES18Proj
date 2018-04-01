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
	private static final String clientNIF = "135792468";
	private static final String clientIBAN = "ES063";
	
	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Londres", NIF, IBAN); // novo
	}

	@Test
	public void success() {
		Booking booking = new Booking(this.hotel, this.arrival, this.departure, clientNIF, clientIBAN);

		Assert.assertTrue(booking.getReference().startsWith(this.hotel.getCode()));
		Assert.assertTrue(booking.getReference().length() > Hotel.CODE_SIZE);
		Assert.assertEquals(this.arrival, booking.getArrival());
		Assert.assertEquals(this.departure, booking.getDeparture());
		Assert.assertEquals(clientNIF, booking.getClientNIF());
		Assert.assertEquals(clientIBAN, booking.getClientIBAN());
		
	}

	@Test(expected = HotelException.class)
	public void nullHotel() {
		new Booking(null, this.arrival, this.departure, clientNIF, clientIBAN);
	}

	@Test(expected = HotelException.class)
	public void nullArrival() {
		new Booking(this.hotel, null, this.departure, clientNIF, clientIBAN);
	}

	@Test(expected = HotelException.class)
	public void nullDeparture() {
		new Booking(this.hotel, this.arrival, null, clientNIF, clientIBAN);
	}

	@Test(expected = HotelException.class)
	public void departureBeforeArrival() {
		new Booking(this.hotel, this.arrival, this.arrival.minusDays(1), clientNIF, clientIBAN);
	}

	@Test
	public void arrivalEqualDeparture() {
		new Booking(this.hotel, this.arrival, this.arrival, clientNIF, clientIBAN);
	}
	
	@Test(expected = HotelException.class)
	public void nullNIF() {
		new Booking(this.hotel, this.arrival, this.departure, null, clientIBAN);
	}
	
	@Test(expected = HotelException.class)
	public void biggerNIF() {
		new Booking(this.hotel, this.arrival, this.departure, "9876543210", clientIBAN);
	}

	@Test(expected = HotelException.class)
	public void smallerNIF() {
		new Booking(this.hotel, this.arrival, this.departure, "98765432", clientIBAN);
	}
	
	@Test(expected = HotelException.class)
	public void nullIBAN() {
		new Booking(this.hotel, this.arrival, this.departure, clientNIF, null);
	}
	
	@Test(expected = HotelException.class)
	public void smallerIBAN() {
		new Booking(this.hotel, this.arrival, this.departure, clientNIF, "ES06");
	}
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
