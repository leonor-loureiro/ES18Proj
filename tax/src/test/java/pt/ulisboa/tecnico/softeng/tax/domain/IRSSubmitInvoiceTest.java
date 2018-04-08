package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class IRSSubmitInvoiceTest {
	private static final String SELLER_NIF = "123456789";
	private static final String BUYER_NIF = "987654321";
	private static final String FOOD = "FOOD";
	private static final int VALUE = 16;
	private final LocalDate date = new LocalDate(2018, 02, 13);

	private IRS irs;

	@Before
	public void setUp() {
		this.irs = IRS.getIRS();
		new Seller(this.irs, SELLER_NIF, "José Vendido", "Somewhere");
		new Buyer(this.irs, BUYER_NIF, "Manuel Comprado", "Anywhere");
		new ItemType(this.irs, FOOD, VALUE);
	}

	@Test
	public void success() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, FOOD, VALUE, this.date);
		String invoiceReference = this.irs.submitInvoice(invoiceData);

		Invoice invoice = this.irs.getTaxPayerByNIF(SELLER_NIF).getInvoiceByReference(invoiceReference);

		assertEquals(invoiceReference, invoice.getReference());
		assertEquals(SELLER_NIF, invoice.getSeller().getNIF());
		assertEquals(BUYER_NIF, invoice.getBuyer().getNIF());
		assertEquals(FOOD, invoice.getItemType().getName());
		assertEquals(VALUE, invoice.getValue(), 0.0000);
		assertEquals(this.date, invoice.getDate());
		assertTrue(IRS.taxPayerHasInvoice(SELLER_NIF,invoiceReference));
		assertFalse(IRS.taxPayerHasInvoice(SELLER_NIF,"XPTO"));

	}

	@Test(expected = TaxException.class)
	public void nullSellerNIF() {
		InvoiceData invoiceData = new InvoiceData(null, BUYER_NIF, FOOD, VALUE, this.date);
		this.irs.submitInvoice(invoiceData);		
	}

	@Test(expected = TaxException.class)
	public void emptySellerNIF() {
		InvoiceData invoiceData = new InvoiceData("", BUYER_NIF, FOOD, VALUE, this.date);
		this.irs.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void nullBuyerNIF() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, null, FOOD, VALUE, this.date);
		this.irs.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void emptyBuyerNIF() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, "", FOOD, VALUE, this.date);
		this.irs.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void nullItemType() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, null, VALUE, this.date);
		this.irs.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void emptyItemType() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, "", VALUE, this.date);
		this.irs.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void zeroValue() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, FOOD, 0.0f, this.date);
		this.irs.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void negativeValue() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, FOOD, -23.7f, this.date);
		this.irs.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void nullDate() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, FOOD, VALUE, null);
		this.irs.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void before1970() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, FOOD, VALUE, new LocalDate(1969, 12, 31));
		this.irs.submitInvoice(invoiceData);
	}

	public void equal1970() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, FOOD, VALUE, new LocalDate(1970, 01, 01));
		this.irs.submitInvoice(invoiceData);
	}

	@After
	public void tearDown() {
		this.irs.clearAll();
	}
}
