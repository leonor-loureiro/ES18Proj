package pt.ulisboa.tecnico.softeng.tax.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.LocalDate;

public class IRSSubmitInvoiceMethodTest {
	private IRS irs;
	private InvoiceData invoiceData;
	private final LocalDate date = new LocalDate(1997, 1, 31);
	
	@Before
	public void setUp() {
		this.irs = new IRS();
		this.invoiceData = new InvoiceData("123456789", "987654321", "leite", 4.99, date);
	}
	
	@Test
	public void success() {
		irs.submitInvoice(invoiceData);
		
		Assert.assertNotNull(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void nullInvoice() {
		IRS.submitInvoice(null);
	}
	
	@Test(expected = TaxException.class)
	public void blankInvoice() {
		IRS.submitInvoice("  ");
	}
	
	@Test(expected = TaxException.class)
	public void emptyInvoice() {
		IRS.submitInvoice("");
	}
	
	@After
	public void tearDown() {
		Invoice.invoices.clear();
	}

}