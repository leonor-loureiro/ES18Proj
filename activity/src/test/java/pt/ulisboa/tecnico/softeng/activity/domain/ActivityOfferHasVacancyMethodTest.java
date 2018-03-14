package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ActivityOfferHasVacancyMethodTest {
	private static final String NIF = "123456789";
	private ActivityProvider provider;
	private ActivityOffer offer;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure", "NIF", "IBAN");
		Activity activity = new Activity(this.provider, "Bush Walking", 18, 80, 3);

		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		this.offer = new ActivityOffer(activity, begin, end, 30);
	}

	@Test
	public void success() {
		new Booking(this.provider, this.offer, NIF);
		Assert.assertTrue(this.offer.hasVacancy());
	}

	@Test
	public void bookingIsFull() {
		new Booking(this.provider, this.offer, NIF);
		new Booking(this.provider, this.offer, NIF);
		new Booking(this.provider, this.offer, NIF);
		Assert.assertFalse(this.offer.hasVacancy());
	}

	@Test
	public void bookingIsFullMinusOne() {
		new Booking(this.provider, this.offer, NIF);
		new Booking(this.provider, this.offer, NIF);
		Assert.assertTrue(this.offer.hasVacancy());
	}

	@Test
	public void hasCancelledBookings() {
		new Booking(this.provider, this.offer, NIF);
		new Booking(this.provider, this.offer, NIF);
		Booking booking = new Booking(this.provider, this.offer, NIF);
		booking.cancel();

		Assert.assertTrue(this.offer.hasVacancy());
	}

	@Test
	public void hasCancelledBookingsButFull() {
		new Booking(this.provider, this.offer, NIF);
		new Booking(this.provider, this.offer, NIF);
		Booking booking = new Booking(this.provider, this.offer, NIF);
		booking.cancel();
		new Booking(this.provider, this.offer, NIF);

		Assert.assertFalse(this.offer.hasVacancy());
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
