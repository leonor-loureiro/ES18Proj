package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class BookingConstructorTest extends RollbackTestAbstractClass {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private Room room;

	@Override
	public void populate4Test() {
		Hotel hotel = new Hotel("XPTO123", "Londres");
		this.room = new Room(hotel, "01", Room.Type.SINGLE);
	}

	@Test
	public void success() {
		Booking booking = new Booking(this.room, this.arrival, this.departure);

		Assert.assertTrue(booking.getReference().startsWith(this.room.getHotel().getCode()));
		Assert.assertTrue(booking.getReference().length() > Hotel.CODE_SIZE);
		Assert.assertEquals(this.arrival, booking.getArrival());
		Assert.assertEquals(this.departure, booking.getDeparture());
	}

	@Test(expected = HotelException.class)
	public void nullRoom() {
		new Booking(null, this.arrival, this.departure);
	}

	@Test(expected = HotelException.class)
	public void nullArrival() {
		new Booking(this.room, null, this.departure);
	}

	@Test(expected = HotelException.class)
	public void nullDeparture() {
		new Booking(this.room, this.arrival, null);
	}

	@Test(expected = HotelException.class)
	public void departureBeforeArrival() {
		new Booking(this.room, this.arrival, this.arrival.minusDays(1));
	}

	@Test
	public void arrivalEqualDeparture() {
		new Booking(this.room, this.arrival, this.arrival);
	}

}
