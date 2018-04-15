package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.InvoiceData;

public class IRSSubmitInvoiceTest extends RollbackTestAbstractClass {
	private static final String SELLER_NIF = "123456789";
	private static final String BUYER_NIF = "987654321";
	private static final String FOOD = "FOOD";
	private static final int VALUE = 16;
	private final LocalDate date = new LocalDate(2018, 02, 13);

	private IRS irs;

	@Override
	public void populate4Test() {
		this.irs = IRS.getIRSInstance();
		new Seller(this.irs, SELLER_NIF, "José Vendido", "Somewhere");
		new Buyer(this.irs, BUYER_NIF, "Manuel Comprado", "Anywhere");
		new ItemType(this.irs, FOOD, VALUE);
	}

	@Test
	public void success() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, FOOD, VALUE, this.date);
		String invoiceReference = IRS.submitInvoice(invoiceData);

		Invoice invoice = this.irs.getTaxPayerByNIF(SELLER_NIF).getInvoiceByReference(invoiceReference);

		assertEquals(invoiceReference, invoice.getReference());
		assertEquals(SELLER_NIF, invoice.getSeller().getNif());
		assertEquals(BUYER_NIF, invoice.getBuyer().getNif());
		assertEquals(FOOD, invoice.getItemType().getName());
		assertEquals(VALUE, invoice.getValue(), 0.0000);
		assertEquals(this.date, invoice.getDate());
	}

	@Test(expected = TaxException.class)
	public void nullSellerNIF() {
		InvoiceData invoiceData = new InvoiceData(null, BUYER_NIF, FOOD, VALUE, this.date);
		IRS.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void emptySellerNIF() {
		InvoiceData invoiceData = new InvoiceData("", BUYER_NIF, FOOD, VALUE, this.date);
		IRS.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void nullBuyerNIF() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, null, FOOD, VALUE, this.date);
		IRS.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void emptyBuyerNIF() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, "", FOOD, VALUE, this.date);
		IRS.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void nullItemType() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, null, VALUE, this.date);
		IRS.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void emptyItemType() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, "", VALUE, this.date);
		IRS.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void zeroValue() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, FOOD, 0.0f, this.date);
		IRS.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void negativeValue() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, FOOD, -23.7f, this.date);
		IRS.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void nullDate() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, FOOD, VALUE, null);
		IRS.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void before1970() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, FOOD, VALUE, new LocalDate(1969, 12, 31));
		IRS.submitInvoice(invoiceData);
	}

	@Test
	public void equal1970() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, FOOD, VALUE, new LocalDate(1970, 01, 01));
		IRS.submitInvoice(invoiceData);
	}

}
