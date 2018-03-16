package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.TaxInterface;

@RunWith(JMockit.class)
public class HotelCancelBookingMethodTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private Hotel hotel;
	private Room room;
	private Booking booking;

	@Mocked
	private TaxInterface taxInterface;
	@Mocked
	private BankInterface bankInterface;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Paris", "NIF", "IBAN", 20.0, 30.0);
		this.room = new Room(this.hotel, "01", Type.DOUBLE);
		this.booking = this.room.reserve(Type.DOUBLE, this.arrival, this.departure, "NIF");
	}

	@Test
	public void success() {
		String cancel = Hotel.cancelBooking(this.booking.getReference());

		Assert.assertTrue(this.booking.isCancelled());
		Assert.assertEquals(cancel, this.booking.getCancellation());
	}

	@Test(expected = HotelException.class)
	public void doesNotExist() {
		Hotel.cancelBooking("XPTO");
	}

	@Test(expected = HotelException.class)
	public void nullReference() {
		Hotel.cancelBooking(null);
	}

	@Test(expected = HotelException.class)
	public void emptyReference() {
		Hotel.cancelBooking("");
	}

	@Test
	public void successIntegration() {
		new Expectations() {
			{
				TaxInterface.cancelInvoice(this.anyString);
			}
		};
		String cancel = Hotel.cancelBooking(this.booking.getReference());

		Assert.assertTrue(this.booking.isCancelled());
		Assert.assertEquals(cancel, this.booking.getCancellation());
	}

	@Test(expected = HotelException.class)
	public void doesNotExistIntegration() {
		new Expectations() {
			{
				TaxInterface.cancelInvoice(this.anyString);
				this.times = 0;
			}
		};
		Hotel.cancelBooking("XPTO");
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
}
