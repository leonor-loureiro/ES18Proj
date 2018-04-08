package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;

public class IRSCancelInvoiceTest {
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
		
		this.irs.cancelInvoice(invoiceReference);

		assertEquals(true, IRS.checkCancelledInvoice(invoiceReference));
	}
	
	@Test
	public void cancelledTwiceSuccessTest() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, FOOD, VALUE, this.date);
		String invoiceReference = this.irs.submitInvoice(invoiceData);
		
		this.irs.cancelInvoice(invoiceReference);
		this.irs.cancelInvoice(invoiceReference);
		
		assertEquals(true, IRS.checkCancelledInvoice(invoiceReference));
	}
	
	@Test
	public void cancelledBadReferenceSuccessTest() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, FOOD, VALUE, this.date);
		String invoiceReference = this.irs.submitInvoice(invoiceData);
		
		this.irs.cancelInvoice(invoiceReference + "Ruina");
		assertEquals(false, IRS.checkCancelledInvoice(invoiceReference));
	}
	
	@After
	public void tearDown() {
		this.irs.clearAll();
	}
}
