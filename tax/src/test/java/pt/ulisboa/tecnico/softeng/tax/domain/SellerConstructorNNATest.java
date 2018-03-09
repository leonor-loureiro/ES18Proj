package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class SellerConstructorNNATest {
	private String NIF = "123456789";
	private String NAME = "Jertrudes";
	private String ADDRESS = "Porto";
	
	@Test
	public void success() {
		Seller seller = new Seller(NIF, NAME, ADDRESS);
		
		Assert.assertEquals(NIF, seller.getNIF());
		Assert.assertEquals(NAME, seller.getName());
		Assert.assertEquals(ADDRESS, seller.getAddress());
		Assert.assertTrue(NIF.length() == 9);
	}

		
	@Test(expected = TaxException.class)
	public void nullNIF() {
		Seller seller = new Seller(null, NAME, ADDRESS);
	}

	@Test(expected = TaxException.class)
	public void nullName() {
		Seller seller = new Seller(NIF, null, ADDRESS);
	}
	
	@Test(expected = TaxException.class)
	public void nullAddress() {
		Seller seller = new Seller(NIF, NAME, null);
	}
	
	@After
	public void tearDown() {
		
	}

}