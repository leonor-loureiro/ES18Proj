package pt.ulisboa.tecnico.softeng.tax.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class BuyerTaxReturnMethodTest {
	private final int year = 1970;
	private Buyer buyer;
	
	@Before
	public void setUp() {
		this.buyer = new Buyer("123456789", "Toze", "Rua da Couves, no.4");
	}

	@Test
	public void success() {
		double returnValue = buyer.taxReturn(year);
		
		Assert.assertNotNull(returnValue);
	}

	@Test(expected = TaxException.class)
	public void lesserThan1970() {
		buyer.taxReturn(1969);
	}
	
	@After
	public void tearDown() {
		TaxPayer.taxPayers.clear();
	}
}
