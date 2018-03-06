package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class InvoiceConstructorTest {
	private final LocalDate date = new LocalDate(2018,3,5);
	private final float value = 13;
	private Seller seller;
	private Buyer buyer;
	private ItemType itemtype;
	
	@Before
	public void setUp() {
		this.itemtype("batatas", 23);
		this.seller("123456789", "Jose", "Sao Roque");
		this.buyer("987654321", "Manuel", "Lisboa");
	}

	@Test
	public void success() {
		InVoice invoice = new InVoice(this.value, this.date, this.itemtype, this.seller, this.buyer);
		
		
		
	}



	@After
	public void tearDown() {
		
	}

}
