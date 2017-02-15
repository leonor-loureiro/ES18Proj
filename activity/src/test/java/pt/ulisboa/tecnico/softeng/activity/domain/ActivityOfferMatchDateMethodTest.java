package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityOfferMatchDateMethodTest {
	private ActivityOffer offer;

	@Before
	public void setUp() {
		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		Activity activity = new Activity(provider, "Bush Walking", 18, 80, 3);

		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		this.offer = new ActivityOffer(activity, begin, end);
	}

	@Test
	public void success() {
		Assert.assertTrue(this.offer.matchDate(new LocalDate(2016, 12, 19), new LocalDate(2016, 12, 21)));
	}

	@Test(expected = ActivityException.class)
	public void nullBeginDate() {
		this.offer.matchDate(null, new LocalDate(2016, 12, 21));
	}

	@Test(expected = ActivityException.class)
	public void nullEndDate() {
		this.offer.matchDate(new LocalDate(2016, 12, 19), null);
	}

	@Test
	public void beginDoesntMatch() {
		Assert.assertFalse(this.offer.matchDate(new LocalDate(2016, 12, 20), new LocalDate(2016, 12, 21)));
	}

	@Test
	public void endDoesntMatch() {
		Assert.assertFalse(this.offer.matchDate(new LocalDate(2016, 12, 19), new LocalDate(2016, 12, 23)));
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
