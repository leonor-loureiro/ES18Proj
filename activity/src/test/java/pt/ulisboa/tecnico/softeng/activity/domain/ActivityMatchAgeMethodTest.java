package pt.ulisboa.tecnico.softeng.activity.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ActivityMatchAgeMethodTest {
	private Activity activity;

	@Before
	public void setUp() {
		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		this.activity = new Activity(provider, "Bush Walking", 18, 80, 3);
	}

	@Test
	public void successIn() {
		Assert.assertTrue(this.activity.matchAge(50));
	}

	public void lessThanMinAge() {
		Assert.assertFalse(this.activity.matchAge(17));
	}

	public void equalMinAge() {
		Assert.assertFalse(this.activity.matchAge(18));
	}

	public void equalMaxAge() {
		Assert.assertFalse(this.activity.matchAge(80));
	}

	public void greaterThanMaxAge() {
		Assert.assertFalse(this.activity.matchAge(81));
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
