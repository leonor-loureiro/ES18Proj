package pt.ulisboa.tecnico.softeng.activity.domain;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ActivityReserveActivityMethodTest {
	private ActivityProvider provider;
	private ActivityOffer offer;
	private LocalDate begin;
	private LocalDate end;
	private static int age = 20;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		Activity activity = new Activity(this.provider, "Bush Walking", 18, 80, 3);

		this.begin = new LocalDate(2016, 12, 19);
		this.end = new LocalDate(2016, 12, 21);

		this.offer = new ActivityOffer(activity, begin, end);
	}

	@Test
	public void success() {
		String reservation = ActivityProvider.reserveActivity(this.begin, this.end, this.age);

		assertNotNull(reservation);
	}

	@Test(expected = ActivityException.class)
	public void offerNotProvided() {
		String reservation = ActivityProvider.reserveActivity(this.begin, this.end, 100);
	}
/*
	@Test
	public void doesNotExist() {
		new Booking(this.provider, this.offer);

		assertNull(this.offer.getBooking("XPTO"));
	}
*/
	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
