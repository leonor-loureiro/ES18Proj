package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.ArrayList;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;


public class BuyerTaxReturnMethodTest {
	private String NIF = "123456789";
	private String NAME = "Jertrudes";
	private String ADDRESS = "Porto";
	private int YEAR = 2018;
	private Buyer buyer;
	
	@Before
	public void setUp() {
		this.buyer = new Buyer(NIF, NAME, ADDRESS);
	}

	@Test
	public void nothingToPay() {
		double pay = this.buyer.taxReturn(YEAR);
		Assert.assertEquals(pay,0,0.0);
	}
	
	@Test(expected = TaxException.class )
	public void failure() {
		double pay = this.buyer.taxReturn(1960);		
	}
	
	
	@Test
	public void sucess() {
		Seller seller = new Seller("987654321", "Manuel", "Lisboa");
		LocalDate date = new LocalDate(YEAR,3,5);
		ItemType type = new ItemType("batatas", 23);
		Invoice invoice = new Invoice(13, date, "batatas", seller, buyer);
		
		double pay = this.buyer.taxReturn(YEAR);
		double itemIVAValue = (invoice.getValue() / ( 1 + (ItemType.findTaxByType(invoice.getItemType()) / 100)))
				* ((ItemType.findTaxByType(invoice.getItemType()))/ 100);
		double expectedPay = itemIVAValue * 0.05; 
		Assert.assertEquals(expectedPay, pay, 0.0);
	}

		
	@Test
	public void successMultipleInvoices() {
		Seller seller = new Seller("987654321", "Manuel", "Lisboa");
		LocalDate date = new LocalDate(YEAR,3,5);
		ItemType type = new ItemType("batatas", 23);
		
		ArrayList<Invoice> invoices = new ArrayList<Invoice>();
		invoices.add(new Invoice(13, date, "batatas", seller, buyer));
		invoices.add(new Invoice(20, date, "batatas", seller, buyer));
		
		double pay = this.buyer.taxReturn(YEAR);
		
		double expectedPay = 0;
		double itemIVAValue = 0;
		for(Invoice invoice : invoices) {
			itemIVAValue = (invoice.getValue() / ( 1 + (ItemType.findTaxByType(invoice.getItemType()) / 100)))
					* ((ItemType.findTaxByType(invoice.getItemType()))/ 100);
			expectedPay += itemIVAValue * 0.05; 
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