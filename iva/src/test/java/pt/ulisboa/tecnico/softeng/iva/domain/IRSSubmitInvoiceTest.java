package pt.ulisboa.tecnico.softeng.iva.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.iva.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.iva.exception.IvaException;

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
		new Seller(SELLER_NIF, "José Vendido", "Somewhere");
		new Buyer(BUYER_NIF, "Manuel Comprado", "Anywhere");
		new ItemType(FOOD, VALUE);
	}

	@Test
	public void success() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, FOOD, VALUE, this.date);
		String invoiceReference = this.irs.submitInvoice(invoiceData);

		Invoice invoice = this.irs.getTaxPayerByNIF(SELLER_NIF).getInvoice(invoiceReference);

		assertEquals(invoiceReference, invoice.getReference());
		assertEquals(SELLER_NIF, invoice.getSeller().getNIF());
		assertEquals(BUYER_NIF, invoice.getBuyer().getNIF());
		assertEquals(FOOD, invoice.getItemType().getName());
		assertEquals(VALUE, invoice.getValue(), 0.0000);
		assertEquals(this.date, invoice.getDate());
	}

	@Test(expected = IvaException.class)
	public void nullSellerNIF() {
		InvoiceData invoiceData = new InvoiceData(null, BUYER_NIF, FOOD, VALUE, this.date);
		this.irs.submitInvoice(invoiceData);
	}

	@Test(expected = IvaException.class)
	public void emptySellerNIF() {
		InvoiceData invoiceData = new InvoiceData("", BUYER_NIF, FOOD, VALUE, this.date);
		this.irs.submitInvoice(invoiceData);
	}

	@Test(expected = IvaException.class)
	public void nullBuyerNIF() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, null, FOOD, VALUE, this.date);
		this.irs.submitInvoice(invoiceData);
	}

	@Test(expected = IvaException.class)
	public void emptyBuyerNIF() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, "", FOOD, VALUE, this.date);
		this.irs.submitInvoice(invoiceData);
	}

	@Test(expected = IvaException.class)
	public void nullItemType() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, null, VALUE, this.date);
		this.irs.submitInvoice(invoiceData);
	}

	@Test(expected = IvaException.class)
	public void emptyItemType() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, "", VALUE, this.date);
		this.irs.submitInvoice(invoiceData);
	}

	@Test(expected = IvaException.class)
	public void zeroValue() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, FOOD, 0.0f, this.date);
		this.irs.submitInvoice(invoiceData);
	}

	@Test(expected = IvaException.class)
	public void negativeValue() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, FOOD, -23.7f, this.date);
		this.irs.submitInvoice(invoiceData);
	}

	@Test(expected = IvaException.class)
	public void nullDate() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, FOOD, -23.7f, null);
		this.irs.submitInvoice(invoiceData);
	}

	@After
	public void tearDown() {
		this.irs.clearAll();
	}
}
