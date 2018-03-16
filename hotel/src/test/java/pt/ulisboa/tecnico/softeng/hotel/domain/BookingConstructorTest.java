package pt.ulisboa.tecnico.softeng.hotel.domain;

import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.TaxInterface;

@RunWith(JMockit.class)
public class BookingConstructorTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private Hotel hotel;
	private final double price = 20.0;

    @Mocked
    private TaxInterface taxInterface;
    @Mocked private BankInterface bankInterface;

    @Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Londres", "NIF", "IBAN", 20.0, 30.0);
	}

	@Test
	public void success() {
		Booking booking = new Booking(this.hotel, Room.Type.SINGLE, this.arrival, this.departure, "NIF");

		Assert.assertTrue(booking.getReference().startsWith(this.hotel.getCode()));
		Assert.assertTrue(booking.getReference().length() > Hotel.CODE_SIZE);
		Assert.assertEquals(this.arrival, booking.getArrival());
		Assert.assertEquals(this.departure, booking.getDeparture());
		Assert.assertEquals(booking.getPrice(), this.price, 0.0d);
	}

	@Test(expected = HotelException.class)
	public void nullHotel() {
		new Booking(null, Room.Type.SINGLE, this.arrival, this.departure, "NIF");
	}

	@Test(expected = HotelException.class)
	public void nullArrival() {
		new Booking(this.hotel, Room.Type.SINGLE, null, this.departure, "NIF");
	}

	@Test(expected = HotelException.class)
	public void nullDeparture() {
		new Booking(this.hotel, Room.Type.SINGLE, this.arrival, null, "NIF");
	}

	@Test(expected = HotelException.class)
	public void departureBeforeArrival() {
		new Booking(this.hotel, Room.Type.SINGLE, this.arrival, this.arrival.minusDays(1), "NIF");
	}

	@Test
	public void arrivalEqualDeparture() {
		new Booking(this.hotel, Room.Type.SINGLE, this.arrival, this.arrival, "NIF");
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
