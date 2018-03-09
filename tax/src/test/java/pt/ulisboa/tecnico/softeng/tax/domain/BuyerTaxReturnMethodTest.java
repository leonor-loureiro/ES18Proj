package pt.ulisboa.tecnico.softeng.tax.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BuyerTaxReturnMethodTest {
	private final String year = "1970";
	private Buyer buyer;
	
	@Before
	public void setUp() {
		this.buyer = new Buyer("123456789", "Toze", "Rua da Couves, no.4");
	}

	@Test
	public void success() {
		int returnValue = Buyer.taxReturn(year);
		
		Assert.assertNotNull(returnValue);
	}

	@Test(expected = TaxException.class)
	public void lesserThan1970() {
		Buyer.taxReturn("1969");
	}

	@Test(expected = TaxException.class)
	public void nullYear() {
		Buyer.taxReturn(null);
	}
	
	@Test(expected = TaxException.class)
	public void blankYear() {
		Buyer.taxReturn("  ");
	}
	
	@Test(expected = TaxException.class)
	public void emptyYear() {
		Buyer.taxReturn("");
	}
	
	@After
	public void tearDown() {
		TaxPayer.taxPayers.clear();
	}
}
