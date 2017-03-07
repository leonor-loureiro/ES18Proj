package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class ActivityOfferConstructorMethodTest {
	private Activity activity;

	@Before
	public void setUp() {
		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		this.activity = new Activity(provider, "Bush Walking", 18, 80, 25);
	}

	@Test
	public void success() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		ActivityOffer offer = new ActivityOffer(this.activity, begin, end);

		Assert.assertEquals(begin, offer.getBegin());
		Assert.assertEquals(end, offer.getEnd());
		Assert.assertEquals(1, this.activity.getNumberOfOffers());
		Assert.assertEquals(0, offer.getNumberOfBookings());
	}

	@Test(expected = ActivityException.class)
	public void nullActivity() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		new ActivityOffer(null, begin, end);
	}

	@Test(expected = ActivityException.class)
	public void nullbeginDate() {
		LocalDate end = new LocalDate(2016, 12, 21);

		new ActivityOffer(this.activity, null, end);
	}

	@Test(expected = ActivityException.class)
	public void nullEndDate() {
		LocalDate begin = new LocalDate(2016, 12, 19);

		new ActivityOffer(this.activity, begin, null);
	}

	@Test(expected = ActivityException.class)
	public void endDateBeforeBeginDate() {
		LocalDate begin = new LocalDate(2016, 12, 21);
		LocalDate end = new LocalDate(2016, 12, 19);

		new ActivityOffer(this.activity, begin, end);
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
