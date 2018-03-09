package pt.ulisboa.tecnico.softeng.tax.domain;


import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class SellerGetInvoiceByReferenceMethodTest {
	
	private String NIF = "123456111";
	private String NAME = "Jertrudes";
	private String ADDRESS = "Porto";
	
	private final LocalDate date = new LocalDate(2018,3,5);
	private float value;
	private String itemtype;
	
	@Before
	public void setUp() {
		Seller seller = new Seller(NIF, NAME, ADDRESS);
		
		this.value = 13;
		this.itemtype = "batatas";
		Seller seller2 = new Seller("123456789", "Jose", "Sao Roque");
		Buyer buyer = new Buyer("987654321", "Manuel", "Lisboa");
		InVoice invoice = new Invoice(this.value, this.date, this.itemtype, this.seller2, this.buyer);
	}

	@Test
	public void success() {	
		Assert.assertEquals(this.invoice, this.seller.getInvoiceByReference(this.invoice.getReference()));
	}

	@Test
	public void doesNotExist() {
		Assert.assertNull(this.seller.getInvoiceByReference("NaoExisto"));
	}


	@After
	public void tearDown() {
		Invoice.invoices.clear();
	}

}