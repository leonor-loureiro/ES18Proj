package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.fail;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class RoomReserveMethodTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 24);
	private Room room;

	private static final String NIF = "123456789"; 
	private static final String IBAN = "ES061"; 
	private static final String clientNIF = "135792468"; 
	private static final String clientIBAN = "ES063"; 
	private static final int singlePRICE = 10;
	private static final int doublePRICE = 10;
	
	@Before
	public void setUp() {
		Hotel hotel = new Hotel("XPTO123", "Lisboa", NIF, IBAN, singlePRICE, doublePRICE); 
		this.room = new Room(hotel, "01", Type.SINGLE);
	}

	@Test
	public void success() {
		Booking booking = this.room.reserve(Type.SINGLE, this.arrival, this.departure, clientNIF, clientIBAN);

		Assert.assertEquals(1, this.room.getNumberOfBookings());
		Assert.assertTrue(booking.getReference().length() > 0);
		Assert.assertEquals(this.arrival, booking.getArrival());
		Assert.assertEquals(this.departure, booking.getDeparture());
	}

	@Test(expected = HotelException.class)
	public void noDouble() {
		this.room.reserve(Type.DOUBLE, this.arrival, this.departure, clientNIF, clientIBAN); 
	}

	@Test(expected = HotelException.class)
	public void nullType() {
		this.room.reserve(null, this.arrival, this.departure, clientNIF, clientIBAN); 
	}

	@Test(expected = HotelException.class)
	public void nullArrival() {
		this.room.reserve(Type.SINGLE, null, this.departure, clientNIF, clientIBAN); 
	}

	@Test(expected = HotelException.class)
	public void nullDeparture() {
		this.room.reserve(Type.SINGLE, this.arrival, null, clientNIF, clientIBAN); 
	}

	@Test
	public void allConflict() {
		this.room.reserve(Type.SINGLE, this.arrival, this.departure, clientNIF, clientIBAN); 

		try {
			this.room.reserve(Type.SINGLE, this.arrival, this.departure, clientNIF, clientIBAN); 
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
