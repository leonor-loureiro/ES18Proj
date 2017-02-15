package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BookingConflictMethodTest {
	Booking booking;

	@Before
	public void setUp() {
		Hotel hotel = new Hotel("XPTO123", "Londres");

		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 24);
		this.booking = new Booking(hotel, arrival, departure);
	}

	@Test
	public void noConflictBefore() {
		LocalDate arrival = new LocalDate(2016, 12, 16);
		LocalDate departure = new LocalDate(2016, 12, 19);

		Assert.assertFalse(this.booking.conflict(arrival, departure));
	}

	@Test
	public void noConflictAfter() {
		LocalDate arrival = new LocalDate(2016, 12, 24);
		LocalDate departure = new LocalDate(2016, 12, 30);

		Assert.assertFalse(this.booking.conflict(arrival, departure));
	}

	@Test
	public void conflictBeforeIn() {
		LocalDate arrival = new LocalDate(2016, 12, 20);
		LocalDate departure = new LocalDate(2016, 12, 30);

		Assert.assertTrue(this.booking.conflict(arrival, departure));
	}

	@Test
	public void conflictAfterIn() {
		LocalDate arrival = new LocalDate(2016, 12, 14);
		LocalDate departure = new LocalDate(2016, 12, 23);

		Assert.assertTrue(this.booking.conflict(arrival, departure));
	}

	@Test
	public void conflictSameDates() {
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 24);

		Assert.assertTrue(this.booking.conflict(arrival, departure));
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
