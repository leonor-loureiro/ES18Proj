package pt.ulisboa.tecnico.softeng.tax.domain;


import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class SellerGetInvoiceByReferenceMethodTest {
	
	private String NIF = "123456111";
	private String NAME = "Jertrudes";
	private String ADDRESS = "Porto";
	
	private final LocalDate date = new LocalDate(2018,3,5);
	private float value;
	private String itemtype;
	Seller seller;
	Invoice invoice;
	Buyer buyer;
	Seller seller2;
	
	@Before
	public void setUp() {
		seller = new Seller(NIF, NAME, ADDRESS);
		
		this.value = 13;
		this.itemtype = "batatas";
		this.seller2 = new Seller("123456789", "Jose", "Sao Roque");
		this.buyer = new Buyer("987654321", "Manuel", "Lisboa");
		this.invoice = new Invoice(this.value, this.date, this.itemtype, this.seller2, this.buyer);
	}

	@Test
	public void success() {	
		Assert.assertEquals(this.invoice, this.seller.getInvoiceByReference(this.invoice.getReference()));
	}

	@Test(expected = TaxException.class)
	public void doesNotExist() {
		Assert.assertNull(this.seller.getInvoiceByReference("NaoExisto"));
	}


	@After
	public void tearDown() {
		Invoice.invoices.clear();
	}

}