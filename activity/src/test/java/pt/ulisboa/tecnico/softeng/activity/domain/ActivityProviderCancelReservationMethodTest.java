package pt.ulisboa.tecnico.softeng.activity.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class ActivityProviderCancelReservationMethodTest {
	private static final String IBAN = "IBAN";
	private static final String NIF = "123456789";
	private ActivityProvider provider;
	private ActivityOffer offer;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure", "NIF", IBAN);
		Activity activity = new Activity(this.provider, "Bush Walking", 18, 80, 3);

		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		this.offer = new ActivityOffer(activity, begin, end, 30);
	}

	@Test
	public void success() {
		Booking booking = new Booking(this.provider, this.offer, NIF, IBAN);

		String cancel = ActivityProvider.cancelReservation(booking.getReference());

		assertTrue(booking.isCancelled());
		assertEquals(cancel, booking.getCancellation());
	}

	@Test(expected = ActivityException.class)
	public void doesNotExist() {
		new Booking(this.provider, this.offer, NIF, IBAN);

		ActivityProvider.cancelReservation("XPTO");
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
