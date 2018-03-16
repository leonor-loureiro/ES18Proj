package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.ArrayList;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;


public class SellerToPayMethodTest {
	private String NIF = "123456789";
	private String NAME = "Jertrudes";
	private String ADDRESS = "Porto";
	private int YEAR = 2018;
	private Seller seller;
	
	@Before
	public void setUp() {
		this.seller = new Seller(NIF, NAME, ADDRESS);
	}

	@Test
	public void nothingToPay() {
		double pay = this.seller.toPay(YEAR);
		Assert.assertEquals(pay,0,0.0);
		
	}
	
	@Test(expected = TaxException.class )
	public void failure() {
		double pay = this.seller.toPay(1960);		
	}
	
	@Test
	public void sucess() {
		Buyer buyer = new Buyer("987654321", "Manuel", "Lisboa");
		LocalDate date = new LocalDate(this.YEAR,3,5);
		ItemType type = new ItemType("batatas", 23);
		Invoice invoice = new Invoice(13, date, "batatas", this.seller, buyer);
		
		double pay = this.seller.toPay(this.YEAR);
		double expectedPay = invoice.getValue() * (invoice.getIVA()/100);
		Assert.assertEquals(expectedPay, pay, 0.0);
	}
	
	@Test
	public void successMultipleInvoices() {
		Buyer buyer = new Buyer("987654321", "Manuel", "Lisboa");
		LocalDate date = new LocalDate(this.YEAR,3,5);
		ItemType type = new ItemType("batatas", 23);
		
		ArrayList<Invoice> invoices = new ArrayList<Invoice>();
		invoices.add(new Invoice(13, date, "batatas", this.seller, buyer));
		invoices.add(new Invoice(20, date, "batatas", this.seller, buyer));
		
		double pay = this.seller.toPay(this.YEAR);
		double expectedPay = 0;
		for(Invoice invoice : invoices) {
			expectedPay += invoice.getValue() * (invoice.getIVA()/100);
		}
		Assert.assertEquals(expectedPay, pay, 0.0);
	}
	
	@After
	public void tearDown() {
		Invoice.invoices.clear();
		TaxPayer.taxPayers.clear();
		ItemType.itemTypes.clear();
	}
}