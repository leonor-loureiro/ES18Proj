package pt.ulisboa.tecnico.softeng.activity.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class ActivityConstructorMethodTest {
	private static final String PROVIDER_NAME = "Bush Walking";
	private ActivityProvider provider;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
	}

	@Test
	public void success() {
		Activity activity = new Activity(this.provider, PROVIDER_NAME, 18, 80, 25);

		Assert.assertTrue(activity.getCode().startsWith(this.provider.getCode()));
		Assert.assertTrue(activity.getCode().length() > ActivityProvider.CODE_SIZE);
		Assert.assertEquals("Bush Walking", activity.getName());
		Assert.assertEquals(18, activity.getMinAge());
		Assert.assertEquals(80, activity.getMaxAge());
		Assert.assertEquals(25, activity.getCapacity());
		Assert.assertEquals(0, activity.getNumberOfOffers());
		Assert.assertEquals(1, this.provider.getNumberOfActivities());
	}

	@Test(expected = ActivityException.class)
	public void nullProvider() {
		new Activity(null, PROVIDER_NAME, 18, 80, 25);
	}

	@Test(expected = ActivityException.class)
	public void nullProviderName() {
		new Activity(this.provider, null, 18, 80, 25);
	}

	@Test(expected = ActivityException.class)
	public void emptyProviderName() {
		new Activity(this.provider, "    ", 18, 80, 25);
	}

	@Test(expected = ActivityException.class)
	public void MinAgeLessThan18() {
		new Activity(this.provider, PROVIDER_NAME, 17, 80, 25);
	}

	@Test(expected = ActivityException.class)
	public void MinAgeGreaterThan100() {
		new Activity(this.provider, PROVIDER_NAME, 101, 101, 25);
	}

	@Test(expected = ActivityException.class)
	public void MaxAgeLessThan18() {
		new Activity(this.provider, PROVIDER_NAME, 17, 17, 25);
	}

	@Test(expected = ActivityException.class)
	public void MaxAgeGreaterThan100() {
		new Activity(this.provider, PROVIDER_NAME, 56, 101, 25);
	}

	@Test(expected = ActivityException.class)
	public void MinAgeGreaterThanMaxAge() {
		new Activity(this.provider, PROVIDER_NAME, 56, 55, 25);
	}

	@Test(expected = ActivityException.class)
	public void nonPositiveCapacity() {
		new Activity(this.provider, PROVIDER_NAME, 18, 50, 0);
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
