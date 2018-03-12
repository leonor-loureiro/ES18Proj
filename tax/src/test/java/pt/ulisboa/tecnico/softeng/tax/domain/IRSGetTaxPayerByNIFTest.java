package pt.ulisboa.tecnico.softeng.tax.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class IRSGetTaxPayerByNIFTest {
	private TaxPayer payer;
	
	@Before
	public void setUp() {
		this.payer = new TaxPayer("123456789", "Toze", "Rua das Couves, no.4");
	}
	
	@Test
	public void success() {
		TaxPayer data = IRS.getTaxPayerByNIF("123456789");
		
		Assert.assertNotNull(data);
		Assert.assertEquals(data.getNif(), this.payer.getNif());
		Assert.assertEquals(data.getName(), this.payer.getName());
		Assert.assertEquals(data.getAddress(), this.payer.getAddress());
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