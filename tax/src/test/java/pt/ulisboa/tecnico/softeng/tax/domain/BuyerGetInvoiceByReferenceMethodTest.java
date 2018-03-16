package pt.ulisboa.tecnico.softeng.tax.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;


public class BuyerGetInvoiceByReferenceMethodTest {
	private String NIF = "123456789";
	private String NAME = "Jertrudes";
	private String ADDRESS = "Porto";
	private int YEAR = 2018;
	private Buyer buyer;
	
	@Before
	public void setUp() {
		this.buyer = new Buyer(NIF, NAME, ADDRESS);
	}

	@Test(expected = TaxException.class)
	public void failure() {
		this.buyer.getInvoiceByReference("INVOICE REFERENCE");
	}
	
	@After
	public void tearDown() {
		TaxPayer.taxPayers.clear();
	}
}