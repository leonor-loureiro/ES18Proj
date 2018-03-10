package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class SellerToPayMethodTest {
	private String NIF = "123456789";
	private String NAME = "Jertrudes";
	private String ADDRESS = "Porto";
	private int YEAR = 2018;
	Seller seller;
	
	@Before
	public void setUp() {
		this.seller = new Seller(NIF, NAME, ADDRESS);
	}

	@Test
	public void success() {
		double pay = this.seller.toPay(YEAR);
		
		Assert.assertNotNull(pay);
	}

	@Test(expected = TaxException.class)
	public void nullYear() {
		double pay = this.seller.toPay(null);
	}
	
	@Test(expected = TaxException.class)
	public void lesserThan1970() {
		seller.toPay(1969);
	}
}