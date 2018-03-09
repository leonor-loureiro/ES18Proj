package pt.ulisboa.tecnico.softeng.tax.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

public class IRSGetTaxPayerByNIFTest {
	private IRS irs;
	private TaxPayer payer;
	
	@Before
	public void setUp() {
		this.irs = new IRS();
		this.payer = new TaxPayer("123456789", "Toze", "Rua das Couves, no.4");
	}
	
	@Test
	public void success() {
		ItemType data = irs.getTaxPayerByNIF("123456789");
		
		Assert.assertNotNull(data);
		Assert.assertEquals(data.getNIF(), this.payer.getNIF());
		Assert.assertEquals(data.getName(), this.payer.getName());
		Assert.assertEquals(data.getAddress, this.payer.getAddress());
	}

	@Test(expected = TaxException.class)
	public void nullNIF() {
		IRS.getTaxPayerByNIF(null);
	}
	
	@Test(expected = TaxException.class)
	public void blankNIF() {
		IRS.getTaxPayerByNIF("  ");
	}
	
	@Test(expected = TaxException.class)
	public void emptyNIF() {
		IRS.getTaxPayerByNIF("");
	}
	
	@After
	public void tearDown() {
		TaxPayer.taxPayers.clear();
	}
}