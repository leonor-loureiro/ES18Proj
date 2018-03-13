package pt.ulisboa.tecnico.softeng.tax.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;


public class IRSGetIRSMethodTest {
	
	@Before
	public void setUp() {
		//empty
	}
	
	@Test
	public void success() {
		Assert.assertNotNull(IRS.getIRS());
	}

	
	
	@After
	public void tearDown() {
		// empty
	}
}