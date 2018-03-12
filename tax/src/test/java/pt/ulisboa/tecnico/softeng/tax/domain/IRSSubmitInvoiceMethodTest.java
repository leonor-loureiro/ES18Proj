package pt.ulisboa.tecnico.softeng.tax.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;

public class IRSSubmitInvoiceMethodTest {
	private InvoiceData invoiceData;
	private final LocalDate date = new LocalDate(1997, 1, 31);
	private Buyer buyer;
	private Seller seller;
	private ItemType item;
	
	@Before
	public void setUp() {
		this.buyer = new Buyer("987654321", "Toze", "Rua das Couves, no.4");
		this.seller = new Seller("123456789", "Tozerino", "Rua das Couves, no.45");
		this.item = new ItemType("leite", 21);
		this.invoiceData = new InvoiceData("123456789", "987654321", "leite", (float) 4.99, date);
	}
	
	@Test
	public void success() {
		IRS.submitInvoice(invoiceData);
		
		Assert.assertNotNull(invoiceData);
		Assert.assertEquals(buyer.getNif(), invoiceData.getBuyerNIF());
		Assert.assertEquals(seller.getNif(), invoiceData.getSellerNIF());
		Assert.assertEquals(item.getItemType(), invoiceData.getItemType());
	}

	@Test(expected = TaxException.class)
	public void nullInvoice() {
		IRS.submitInvoice(null);
	}
	
	@After
	public void tearDown() {
		Invoice.invoices.clear();
		TaxPayer.taxPayers.clear();
		ItemType.itemTypes.clear();
	}
}