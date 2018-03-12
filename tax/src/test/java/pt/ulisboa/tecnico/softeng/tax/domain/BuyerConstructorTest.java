package pt.ulisboa.tecnico.softeng.tax.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class BuyerConstructorTest {
	private static final String NIF = "123456789";
	private static final String NAME = "Toze";
	private static final String ADDRESS = "Rua das Couves, no.4";
	
	static final int NIF_SIZE = 9;
	
	// constructor tests
	@Test
	public void sucess() {
		Buyer buyer = new Buyer(NIF, NAME, ADDRESS);
		
		Assert.assertTrue(buyer.getNif().length() == NIF_SIZE);	
	}
	
	// nif tests
	@Test(expected = TaxException.class)
	public void nifNotUnique() {
		new Buyer(NIF, NAME, ADDRESS);
		new Buyer(NIF, NAME + "rino", ADDRESS + "5");
	}
	
	@Test(expected = TaxException.class)
	public void nifSizeLess() {
		new Buyer("12345678", NAME, ADDRESS);
	}

	@Test(expected = TaxException.class)
	public void nifSizeMore() {
		new Buyer("1234567890", NAME, ADDRESS);
	}
	
	@Test(expected = TaxException.class)
	public void nullNif() {
		new Buyer(null, NAME, ADDRESS);
	}
	
	@Test(expected = TaxException.class)
	public void blankNif() {
		new Buyer("  ", NAME, ADDRESS);
	}

	@Test(expected = TaxException.class)
	public void emptyNif() {
		new Buyer("", NAME, ADDRESS);
	}
	
	// name tests
	@Test(expected = TaxException.class)
	public void nullName() {
		new Buyer(NIF, null, ADDRESS);
	}
	
	@Test(expected = TaxException.class)
	public void blankName() {
		new Buyer(NIF, "  ", ADDRESS);
	}

	@Test(expected = TaxException.class)
	public void emptyName() {
		new Buyer(NIF, "", ADDRESS);
	}	
	
	// address tests
	@Test(expected = TaxException.class)
	public void nullAddress() {
		new Buyer(NIF, NAME, null);
	}
	
	@Test(expected = TaxException.class)
	public void blankAddress() {
		new Buyer(NIF, NAME, "  ");
	}

	@Test(expected = TaxException.class)
	public void emptyAddress() {
		new Buyer(NIF, NAME, "");
	}
	
	@After
	public void tearDown() {
		TaxPayer.taxPayers.clear();
	}
}