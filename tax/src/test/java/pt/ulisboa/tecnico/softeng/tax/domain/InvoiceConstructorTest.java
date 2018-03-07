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
	private String itemtype;
	
	@Before
	public void setUp() {
		this.itemtype = "batatas";
		this.seller("123456789", "Jose", "Sao Roque");
		this.buyer("987654321", "Manuel", "Lisboa");
	}

	@Test
	public void success() {
		InVoice invoice = new InVoice(this.value, this.date, this.itemtype, this.seller, this.buyer);
		
		Assert.assertTrue(invoice.getDate().getYear() > 1970);
		Assert.assertNotEquals(invoice.getSeller().getNif(), invoice.getBuyer().getNif());
	}

	@Test(expected = InVoiceException.class)
	public void nullValue() {
		new InVoice(null, this.date, this.itemtype, this.seller, this.buyer);
	}
	
	@Test(expected = InVoiceException.class)
	public void nullDate() {
		new InVoice(this.value, null, this.itemtype, this.seller, this.buyer);
	}
	
	@Test(expected = InVoiceException.class)
	public void nullItemType() {
		new InVoice(this.value, this.date, null, this.seller, this.buyer);
	}
	
	@Test(expected = InVoiceException.class)
	public void nullSeller() {
		new InVoice(this.value, this.date, this.itemtype, null, this.buyer);
	}
	
	@Test(expected = InVoiceException.class)
	public void nullBuyer() {
		new InVoice(this.value, this.date, this.itemtype, this.seller, null);
	}
	
	@Test(expected = InVoiceException.class)
	public void emptyItemType() {
		this.itemtype = "";
		new InVoice(this.value, this.date, this.itemtype, this.seller, this.buyer);
	}
	
	
	@After
	public void tearDown() {
		
	}

}
