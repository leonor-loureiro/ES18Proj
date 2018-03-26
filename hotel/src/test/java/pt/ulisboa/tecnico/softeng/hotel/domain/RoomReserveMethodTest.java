package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.fail;

import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.TaxInterface;

@RunWith(JMockit.class)
public class RoomReserveMethodTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 24);
	private Room room;
	private final String NIF_HOTEL = "123456700";
	private String NIF_BUYER = "123456789";
	private String IBAN_BUYER = "IBAN_BUYER";

    @Mocked
    private TaxInterface taxInterface;
    @Mocked private BankInterface bankInterface;

    @Before
	public void setUp() {
		Hotel hotel = new Hotel("XPTO123", "Lisboa", NIF_HOTEL, "IBAN", 20.0, 30.0);
		this.room = new Room(hotel, "01", Type.SINGLE);
	}

	@Test
	public void success() {
		Booking booking = this.room.reserve(Type.SINGLE, this.arrival, this.departure, NIF_BUYER, IBAN_BUYER);

		Assert.assertEquals(1, this.room.getNumberOfBookings());
		Assert.assertTrue(booking.getReference().length() > 0);
		Assert.assertEquals(this.arrival, booking.getArrival());
		Assert.assertEquals(this.departure, booking.getDeparture());
	}

	@Test(expected = HotelException.class)
	public void noDouble() {
		this.room.reserve(Type.DOUBLE, this.arrival, this.departure, NIF_BUYER, IBAN_BUYER);
	}

	@Test(expected = HotelException.class)
	public void nullType() {
		this.room.reserve(null, this.arrival, this.departure, NIF_BUYER, IBAN_BUYER);
	}

	@Test(expected = HotelException.class)
	public void nullArrival() {
		this.room.reserve(Type.SINGLE, null, this.departure, NIF_BUYER, IBAN_BUYER);
	}

	@Test(expected = HotelException.class)
	public void nullDeparture() {
		this.room.reserve(Type.SINGLE, this.arrival, null, NIF_BUYER, IBAN_BUYER);
	}

	@Test
	public void allConflict() {
		this.room.reserve(Type.SINGLE, this.arrival, this.departure, NIF_BUYER, IBAN_BUYER);

		try {
			this.room.reserve(Type.SINGLE, this.arrival, this.departure, NIF_BUYER, IBAN_BUYER);
			fail();
		} catch (HotelException he) {
			Assert.assertEquals(1, this.room.getNumberOfBookings());
		}
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
}
