package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class BookingConstructorTest {
	private Hotel hotel;
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);

	@Test
	public void success() {
		this.hotel = new Hotel("XPTO123", "Londres");

		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 21);

		Booking booking = new Booking(this.hotel, arrival, departure);

		Assert.assertTrue(booking.getReference().startsWith(this.hotel.getCode()));
		Assert.assertTrue(booking.getReference().length() > Hotel.CODE_SIZE);
		Assert.assertEquals(arrival, booking.getArrival());
		Assert.assertEquals(departure, booking.getDeparture());
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
	public void nullDepartureBeforeArrival() {
		new Booking(this.hotel, this.arrival, new LocalDate(2016, 12, 18));
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
