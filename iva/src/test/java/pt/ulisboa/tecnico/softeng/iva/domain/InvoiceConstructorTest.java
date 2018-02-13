package pt.ulisboa.tecnico.softeng.iva.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.iva.exception.IvaException;

public class InvoiceConstructorTest {
	private static final String SELLER_NIF = "123456789";
	private static final String BUYER_NIF = "987654321";
	private static final String FOOD = "FOOD";
	private static final int VALUE = 16;
	private static final int TAX = 23;
	private final LocalDate date = new LocalDate(2018, 02, 13);

	private IRS irs;
	private Seller seller;
	private Buyer buyer;
	private ItemType itemType;

	@Before
	public void setUp() {
		this.irs = IRS.getIRS();
		this.seller = new Seller(SELLER_NIF, "José Vendido", "Somewhere");
		this.buyer = new Buyer(BUYER_NIF, "Manuel Comprado", "Anywhere");
		this.itemType = new ItemType(FOOD, TAX);
	}

	@Test
	public void success() {
		Invoice invoice = new Invoice(VALUE, this.date, this.itemType, this.seller, this.buyer);

		assertEquals(VALUE, invoice.getValue(), 0.0f);
		assertEquals(this.date, invoice.getDate());
		assertEquals(this.itemType, invoice.getItemType());
		assertEquals(this.seller, invoice.getSeller());
		assertEquals(this.buyer, invoice.getBuyer());
		assertEquals(VALUE * TAX / 100.0, invoice.getIva(), 0.00001f);
	}

	@Test(expected = IvaException.class)
	public void nullSeller() {
		new Invoice(VALUE, this.date, this.itemType, null, this.buyer);
	}

	@Test(expected = IvaException.class)
	public void nullBuyer() {
		new Invoice(VALUE, this.date, this.itemType, this.seller, null);
	}

	@Test(expected = IvaException.class)
	public void nullItemType() {
		new Invoice(VALUE, this.date, null, this.seller, this.buyer);
	}

	@Test(expected = IvaException.class)
	public void zeroValue() {
		new Invoice(0, this.date, this.itemType, this.seller, this.buyer);
	}

	@Test(expected = IvaException.class)
	public void negativeValue() {
		new Invoice(-23.6f, this.date, this.itemType, this.seller, this.buyer);
	}

	@Test(expected = IvaException.class)
	public void nullDate() {
		new Invoice(VALUE, null, this.itemType, this.seller, this.buyer);
	}

	@After
	public void tearDown() {
		IRS.getIRS().clearAll();
	}

}
