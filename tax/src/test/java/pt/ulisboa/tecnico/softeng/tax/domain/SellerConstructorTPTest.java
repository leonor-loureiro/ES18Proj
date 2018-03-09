package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class SellerConstructorTPTest {
	
	private String NIF = "123456789";
	private String NAME = "Jertrudes";
	private String ADDRESS = "Porto";
	
	private TaxPayer taxPayer = new TaxPayer(NIF, NAME, ADDRESS);

	@Test
	public void success() {
		Seller seller = new Seller(taxPayer);
		
		Assert.assertEquals(NIF, seller.getNIF());
		Assert.assertEquals(NAME, seller.getName());
		Assert.assertEquals(ADDRESS, seller.getAddress());
		Assert.assertTrue(NIF.length() == 9);
	}

		
	@Test(expected = TaxException.class)
	public void nullTaxPayer() {
		Seller seller = new Seller(null);
	}

	@Test(expected = TaxException.class)
	public void duplicatedTaxPayer() {
		Seller seller = new Seller(taxPayer);
	}
	
	@After
	public void tearDown() {
		
	}

}