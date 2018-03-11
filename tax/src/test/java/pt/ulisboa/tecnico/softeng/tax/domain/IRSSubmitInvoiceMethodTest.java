package pt.ulisboa.tecnico.softeng.tax.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.invoiceData;

public class IRSSubmitInvoiceMethodTest {
	private invoiceData invoiceData;
	private final LocalDate date = new LocalDate(1997, 1, 31);
	
	@Before
	public void setUp() {
		this.invoiceData = new invoiceData("123456789", "987654321", "leite", (float) 4.99, date);
	}
	
	@Test
	public void success() {
		IRS.submitInvoice(invoiceData);
		
		Assert.assertNotNull(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void nullInvoice() {
		IRS.submitInvoice(null);
	}
	
	@After
	public void tearDown() {
		Invoice.invoices.clear();
	}
}