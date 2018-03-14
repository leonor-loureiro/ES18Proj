package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class ActivityProviderReserveActivityMethodTest {
	private static final String IBAN = "IBAN";
	private static final String NIF = "123456789";
	private static final int MIN_AGE = 18;
	private static final int MAX_AGE = 50;
	private static final int CAPACITY = 30;

	private static ActivityProvider provider1;
	private static ActivityProvider provider2;

	@Before
	public void setup() {
		provider1 = new ActivityProvider("XtremX", "Adventure++", "NIF", IBAN);
		provider2 = new ActivityProvider("Walker", "Sky", "NIF2", IBAN);
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

	@Test
	public void numberOfProviders() {
		Assert.assertTrue(ActivityProvider.providers.size() == 2);
	}

	@Test
	public void nameOfProviders() {
		Assert.assertTrue(ActivityProvider.providers.contains(provider1));
		Assert.assertTrue(ActivityProvider.providers.contains(provider2));
	}

	@Test(expected = ActivityException.class)
	public void reserveAcitivityNoOption() {
		String act = ActivityProvider.reserveActivity(new LocalDate(2018, 02, 19), new LocalDate(2016, 12, 19), 20, NIF,
				IBAN);
	}

	@Test
	public void reserveAcitivity() {
		Activity activity = new Activity(provider1, "XtremX", MIN_AGE, MAX_AGE, CAPACITY);
		ActivityOffer offer = new ActivityOffer(activity, new LocalDate(2018, 02, 19), new LocalDate(2018, 12, 20), 30);

		String act = ActivityProvider.reserveActivity(new LocalDate(2018, 02, 19), new LocalDate(2018, 12, 20), 20, NIF,
				IBAN);

		Assert.assertTrue(act != null);
		Assert.assertTrue(act.startsWith("XtremX"));
	}

}