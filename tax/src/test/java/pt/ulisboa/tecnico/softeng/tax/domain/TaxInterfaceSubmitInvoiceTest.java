package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;
import pt.ulisboa.tecnico.softeng.tax.services.local.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.InvoiceData;

public class TaxInterfaceSubmitInvoiceTest extends RollbackTestAbstractClass {
	private static final String REFERENCE = "123456789";
	private static final String SELLER_NIF = "123456789";
	private static final String BUYER_NIF = "987654321";
	private static final String FOOD = "FOOD";
	private static final double VALUE = 160;
	private static final int TAX = 16;
	private final LocalDate date = new LocalDate(2018, 02, 13);
	private final DateTime time = new DateTime(2018, 02, 13, 10, 10);

	private IRS irs;

	@Override
	public void populate4Test() {
		this.irs = IRS.getIRSInstance();
		new Seller(this.irs, SELLER_NIF, "José Vendido", "Somewhere");
		new Buyer(this.irs, BUYER_NIF, "Manuel Comprado", "Anywhere");
		new ItemType(this.irs, FOOD, TAX);
	}

	@Test
	public void success() {
		InvoiceData invoiceData = new InvoiceData(REFERENCE, SELLER_NIF, BUYER_NIF, FOOD, VALUE, this.date, this.time);
		String invoiceReference = TaxInterface.submitInvoice(invoiceData);

		Invoice invoice = this.irs.getTaxPayerByNIF(SELLER_NIF).getInvoiceByReference(invoiceReference);

		assertEquals(invoiceReference, invoice.getReference());
		assertEquals(SELLER_NIF, invoice.getSeller().getNif());
		assertEquals(BUYER_NIF, invoice.getBuyer().getNif());
		assertEquals(FOOD, invoice.getItemType().getName());
		assertEquals(VALUE, invoice.getValue(), 0.0000);
		assertEquals(this.date, invoice.getDate());
	}

	@Test
	public void submitTwice() {
		InvoiceData invoiceData = new InvoiceData(REFERENCE, SELLER_NIF, BUYER_NIF, FOOD, VALUE, this.date, this.time);
		String invoiceReference = TaxInterface.submitInvoice(invoiceData);

		String secondInvoiceReference = TaxInterface.submitInvoice(invoiceData);

		assertEquals(invoiceReference, secondInvoiceReference);
	}

	@Test(expected = TaxException.class)
	public void nullSellerNIF() {
		InvoiceData invoiceData = new InvoiceData(REFERENCE, null, BUYER_NIF, FOOD, VALUE, this.date, this.time);
		TaxInterface.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void emptySellerNIF() {
		InvoiceData invoiceData = new InvoiceData(REFERENCE, "", BUYER_NIF, FOOD, VALUE, this.date, this.time);
		TaxInterface.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void nullBuyerNIF() {
		InvoiceData invoiceData = new InvoiceData(REFERENCE, SELLER_NIF, null, FOOD, VALUE, this.date, this.time);
		TaxInterface.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void emptyBuyerNIF() {
		InvoiceData invoiceData = new InvoiceData(REFERENCE, SELLER_NIF, "", FOOD, VALUE, this.date, this.time);
		TaxInterface.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void nullItemType() {
		InvoiceData invoiceData = new InvoiceData(REFERENCE, SELLER_NIF, BUYER_NIF, null, VALUE, this.date, this.time);
		TaxInterface.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void emptyItemType() {
		InvoiceData invoiceData = new InvoiceData(REFERENCE, SELLER_NIF, BUYER_NIF, "", VALUE, this.date, this.time);
		TaxInterface.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void zeroValue() {
		InvoiceData invoiceData = new InvoiceData(REFERENCE, SELLER_NIF, BUYER_NIF, FOOD, 0.0d, this.date, this.time);
		TaxInterface.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void negativeValue() {
		InvoiceData invoiceData = new InvoiceData(REFERENCE, SELLER_NIF, BUYER_NIF, FOOD, -23.7d, this.date, this.time);
		TaxInterface.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void nullDate() {
		InvoiceData invoiceData = new InvoiceData(REFERENCE, SELLER_NIF, BUYER_NIF, FOOD, VALUE, null, this.time);
		TaxInterface.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void nullTime() {
		InvoiceData invoiceData = new InvoiceData(REFERENCE, SELLER_NIF, BUYER_NIF, FOOD, VALUE, this.date, null);
		TaxInterface.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void before1970() {
		InvoiceData invoiceData = new InvoiceData(REFERENCE, SELLER_NIF, BUYER_NIF, FOOD, VALUE,
				new LocalDate(1969, 12, 31), new DateTime(1969, 12, 31, 10, 10));
		TaxInterface.submitInvoice(invoiceData);
	}

	@Test
	public void equal1970() {
		InvoiceData invoiceData = new InvoiceData(REFERENCE, SELLER_NIF, BUYER_NIF, FOOD, VALUE,
				new LocalDate(1970, 01, 01), new DateTime(1970, 01, 01, 10, 10));
		TaxInterface.submitInvoice(invoiceData);
	}

	@Test(expected = TaxException.class)
	public void NullReference() {
		InvoiceData invoiceData = new InvoiceData(null, SELLER_NIF, BUYER_NIF, FOOD, VALUE, new LocalDate(1970, 01, 01),
				new DateTime(1970, 01, 01, 10, 10));
		TaxInterface.submitInvoice(invoiceData);
	}

}
