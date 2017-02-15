package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityProviderFindOfferMethodTest {
	private ActivityProvider provider;
	private Activity activity;
	private ActivityOffer offer;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		this.activity = new Activity(this.provider, "Bush Walking", 18, 80, 25);

		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		this.offer = new ActivityOffer(this.activity, begin, end);
	}

	@Test
	public void success() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		Set<ActivityOffer> offers = this.provider.findOffer(begin, end, 40);

		Assert.assertEquals(1, offers.size());
		Assert.assertTrue(offers.contains(this.offer));
	}

	@Test(expected = ActivityException.class)
	public void nullBeginDate() {
		LocalDate end = new LocalDate(2016, 12, 21);

		this.provider.findOffer(null, end, 40);

	}

	@Test(expected = ActivityException.class)
	public void nullEndDate() {
		LocalDate begin = new LocalDate(2016, 12, 19);

		this.provider.findOffer(begin, null, 40);

	}

	@Test
	public void illegalAge() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		Set<ActivityOffer> offers = this.provider.findOffer(begin, end, 0);

		Assert.assertTrue(offers.isEmpty());
	}

	@Test
	public void emptyActivitySet() {
		ActivityProvider otherProvider = new ActivityProvider("Xtrems", "Adventure");

		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		Set<ActivityOffer> offers = otherProvider.findOffer(begin, end, 40);

		Assert.assertTrue(offers.isEmpty());
	}

	@Test
	public void emptyActivityOfferSet() {
		ActivityProvider otherProvider = new ActivityProvider("Xtrems", "Adventure");
		new Activity(otherProvider, "Bush Walking", 18, 80, 25);

		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		Set<ActivityOffer> offers = otherProvider.findOffer(begin, end, 40);

		Assert.assertTrue(offers.isEmpty());
	}

	@Test
	public void TwoActivityOffers() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		new ActivityOffer(this.activity, begin, end);

		Set<ActivityOffer> offers = this.provider.findOffer(begin, end, 40);

		Assert.assertEquals(2, offers.size());
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
