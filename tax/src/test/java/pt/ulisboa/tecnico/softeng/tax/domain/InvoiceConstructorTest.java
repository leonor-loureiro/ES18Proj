package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;


public class InvoiceConstructorTest {
	private final LocalDate date = new LocalDate(2018,3,5);
	private float value;
	private Seller seller;
	private Buyer buyer;
	private Buyer buyer2;
	private String itemtype;
	private ItemType type;
	
	@Before
	public void setUp() {
		this.type = new ItemType("batatas", 23);
		this.value = 13;
		this.itemtype = "batatas";
		this.seller = new Seller("123456789", "Jose", "Sao Roque");
		this.buyer = new Buyer("987654321", "Manuel", "Lisboa");
	}

	@Test
	public void success() {
		Invoice invoice = new Invoice(this.value, this.date, this.itemtype, this.seller, this.buyer);
		
		Assert.assertTrue(invoice.getDate().getYear() > 1970);
		Assert.assertNotEquals(invoice.getSeller().getNif(), invoice.getBuyer().getNif());
		Assert.assertEquals(1, Invoice.invoices.size());
		Assert.assertEquals(this.value + this.value * (invoice.getIVA()/100), invoice.getValue(),0.0);
		Assert.assertEquals(this.itemtype, invoice.getItemType());
	}
	
	@Test(expected = TaxException.class)
	public void equalNIFs() {
		new Invoice(this.value, this.date, this.itemtype, this.seller, this.buyer2);
	}
	
	@Test(expected = TaxException.class)
	public void datePrevious1970() {
		LocalDate newDate =  new LocalDate(1960,3,5);
		new Invoice(this.value, newDate, this.itemtype, this.seller, this.buyer);
	}
	
	@Test(expected = TaxException.class)
	public void nullItemType() {
		new Invoice(this.value, this.date, null, this.seller, this.buyer);
	}
	
	@Test(expected = TaxException.class)
	public void nullSeller() {
		new Invoice(this.value, this.date, this.itemtype, null, this.buyer);
	}
	
	@Test(expected = TaxException.class)
	public void nullBuyer() {
		Invoice invoice = new Invoice(this.value, this.date, this.itemtype, this.seller, null);
	}
	
	@Test(expected = TaxException.class)
	public void emptyItemType() {
		this.itemtype = "";
		Invoice invoice = new Invoice(this.value, this.date, this.itemtype, this.seller, this.buyer);
	}
	
	@Test(expected = TaxException.class)
	public void blankItemType() {
		this.itemtype = "    ";
		Invoice invoice = new Invoice(this.value, this.date, this.itemtype, this.seller, this.buyer);
	}
	
	@Test(expected = TaxException.class)
	public void nullDate() {
		new Invoice(this.value, null, this.itemtype, this.seller, this.buyer);
	}
	
	@Test(expected = TaxException.class)
	public void negativeValue() {
		new Invoice(-1, this.date, this.itemtype, this.seller, this.buyer);
	}
	
	@After
	public void tearDown() {
		Invoice.invoices.clear();
		TaxPayer.taxPayers.clear();
		ItemType.itemTypes.clear();
	}

}
