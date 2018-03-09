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
	
	@Before
	public void setUp() {
		Seller seller = new Seller(NIF, NAME, ADDRESS);
	}

	@Test
	public void success() {
		private int pay = this.seller.toPay(2018);
		
		Assert.assertNotNull(pay);
	}



	@After
	public void tearDown() {
		
	}

}