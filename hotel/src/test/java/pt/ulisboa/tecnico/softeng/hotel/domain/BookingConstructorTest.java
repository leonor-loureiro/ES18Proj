package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.TaxInterface;

@RunWith(JMockit.class)
public class BookingConstructorTest {
	private static final LocalDate ARRIVAL = new LocalDate(2016, 12, 19);
	private static final LocalDate DEPARTURE = new LocalDate(2016, 12, 21);
	private static final double ROOM_PRICE = 20.0;
	private static String NIF_BUYER = "123456789";
	private static String IBAN_BUYER = "IBAN_BUYER";
	private Hotel hotel;

	@Mocked
	private TaxInterface taxInterface;
	@Mocked
	private BankInterface bankInterface;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Londres", "NIF", "IBAN", 20.0, 30.0);
	}

	@Test
	public void success() {
		Booking booking = new Booking(this.hotel, Room.Type.SINGLE, ARRIVAL, DEPARTURE, NIF_BUYER, IBAN_BUYER);

		Assert.assertTrue(booking.getReference().startsWith(this.hotel.getCode()));
		Assert.assertTrue(booking.getReference().length() > Hotel.CODE_SIZE);
		Assert.assertEquals(ARRIVAL, booking.getArrival());
		Assert.assertEquals(DEPARTURE, booking.getDeparture());
		Assert.assertEquals(ROOM_PRICE * 2, booking.getPrice(), 0.0d);
	}

	@Test(expected = HotelException.class)
	public void nullHotel() {
		new Booking(null, Room.Type.SINGLE, ARRIVAL, DEPARTURE, NIF_BUYER, IBAN_BUYER);
	}

	@Test(expected = HotelException.class)
	public void nullArrival() {
		new Booking(this.hotel, Room.Type.SINGLE, null, DEPARTURE, NIF_BUYER, IBAN_BUYER);
	}

	@Test(expected = HotelException.class)
	public void nullDeparture() {
		new Booking(this.hotel, Room.Type.SINGLE, ARRIVAL, null, NIF_BUYER, IBAN_BUYER);
	}

	@Test(expected = HotelException.class)
	public void departureBeforeArrival() {
		new Booking(this.hotel, Room.Type.SINGLE, ARRIVAL, ARRIVAL.minusDays(1), NIF_BUYER, IBAN_BUYER);
	}

	@Test
	public void arrivalEqualDeparture() {
		new Booking(this.hotel, Room.Type.SINGLE, ARRIVAL, ARRIVAL, NIF_BUYER, IBAN_BUYER);
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
