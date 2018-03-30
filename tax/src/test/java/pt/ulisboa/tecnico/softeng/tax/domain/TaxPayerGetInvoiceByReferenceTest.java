package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class TaxPayerGetInvoiceByReferenceTest {
	private static final String SELLER_NIF = "123456789";
	private static final String BUYER_NIF = "987654321";
	private static final String FOOD = "FOOD";
	private static final int VALUE = 16;
	private static final int TAX = 23;
	private final LocalDate date = new LocalDate(2018, 02, 13);

	private Seller seller;
	private Buyer buyer;
	private ItemType itemType;
	private Invoice invoice;

	@Before
	public void setUp() {
		IRS irs = IRS.getIRS();
		this.seller = new Seller(irs, SELLER_NIF, "José Vendido", "Somewhere");
		this.buyer = new Buyer(irs, BUYER_NIF, "Manuel Comprado", "Anywhere");
		this.itemType = new ItemType(irs, FOOD, TAX);
		this.invoice = new Invoice(VALUE, this.date, this.itemType, this.seller, this.buyer);
	}

	@Test
	public void success() {
		assertEquals(this.invoice, this.seller.getInvoiceByReference(this.invoice.getReference()));
	}

	@Test(expected = TaxException.class)
	public void nullReference() {
		this.seller.getInvoiceByReference(null);
	}

	@Test(expected = TaxException.class)
	public void emptyReference() {
		this.seller.getInvoiceByReference("");
	}

	@Test
	public void desNotExist() {
		assertNull(this.seller.getInvoiceByReference(BUYER_NIF));
	}

	@After
	public void tearDown() {
		IRS.getIRS().clearAll();
	}

}
