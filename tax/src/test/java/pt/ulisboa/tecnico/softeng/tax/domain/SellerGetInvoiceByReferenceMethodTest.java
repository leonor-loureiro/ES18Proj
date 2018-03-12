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
	private Seller seller;
	private Invoice invoice;
	private Buyer buyer;
	private ItemType type;
	
	@Before
	public void setUp() {
		this.seller = new Seller(NIF, NAME, ADDRESS);
		this.type = new ItemType("batatas", 23);
		this.value = 13;
		this.itemtype = "batatas";
		this.buyer = new Buyer("987654321", "Manuel", "Lisboa");
		this.invoice = new Invoice(this.value, this.date, this.itemtype, this.seller, this.buyer);
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
		TaxPayer.taxPayers.clear();
		ItemType.itemTypes.clear();
	}

}