package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;


public class InvoiceGetInvoiceByYearMethodTest {
	
	private final int year = 2018;
	private LocalDate date;
	private float value;
	private Seller seller;
	private Buyer buyer;
	private String itemtype;

	
	@Before
	public void setUp() {
		new ItemType("batatas", 23);
		this.value = 13;
		this.itemtype = "batatas";
		this.seller = new Seller("123456789", "Jose", "Sao Roque");
		this.buyer = new Buyer("987654321", "Manuel", "Lisboa");
		this.date = new LocalDate(this.year,3,5);
		Invoice invoice = new Invoice(this.value, this.date, this.itemtype, this.seller, this.buyer);
	}

	@Test
	public void success() {
		Set<Invoice> invoices = Invoice.getInvoiceByYear(this.year);
		Assert.assertNotNull(invoices);
		
	}
	
	@Test
	public void failure() {
		Set<Invoice> invoices = Invoice.getInvoiceByYear(this.year+1);
		Assert.assertTrue(invoices.isEmpty());
	}
	
	@Test
	public void multipleInvoicesDiffYear() {
		LocalDate newDate =  new LocalDate(this.year+2,5,5);
		new Invoice(this.value, newDate, this.itemtype, this.seller, this.buyer);
		
		Set<Invoice> invoices = Invoice.getInvoiceByYear(this.year);
		Assert.assertEquals(invoices.size(),1);
		
		for(Invoice invoice : invoices) {
			Assert.assertEquals(invoice.getDate().getYear(), this.year);
		}
	}
	
	@Test
	public void multipleInvoicesSameYear() {
		LocalDate newDate =  new LocalDate(this.year,5,5);
		new Invoice(this.value, newDate, this.itemtype, this.seller, this.buyer);
		
		Set<Invoice> invoices = Invoice.getInvoiceByYear(this.year);
		Assert.assertEquals(invoices.size(),2);
		
		for(Invoice invoice : invoices) {
			Assert.assertEquals(invoice.getDate().getYear(), this.year);
		}
	
	}
	
	@After
	public void tearDown() {
		Invoice.invoices.clear();
		TaxPayer.taxPayers.clear();
		ItemType.itemTypes.clear();
	}

}
