package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class ItemTypeConstructorTest {
	private String ITEM_TYPE = "batatas";
	private int TAX = 23;
	
	@Test
	public void success() {
		ItemType itemType = new ItemType(ITEM_TYPE, TAX);
		
		Assert.assertEquals(ITEM_TYPE, itemType.getItemType());
		Assert.assertEquals(TAX, itemType.getTAX());
		Assert.assertTrue(itemType.getTAX() >= 0);
		Assert.assertEquals(1, ItemType.itemTypes.size());
	}
	 

	@Test(expected = TaxException.class)
	public void nullTAX() {
		ItemType itemType = new ItemType(ITEM_TYPE, null);
	}
	
	@Test(expected = TaxException.class)
	public void itemTypeBlank() {
		ItemType itemType = new ItemType("   ", TAX);
	}
	
	@Test(expected = TaxException.class)
	public void blankItemType() {
		ItemType itemType = new ItemType("   ", TAX);
	}
	
	@Test(expected = TaxException.class)
	public void emptyItemType() {
		ItemType itemType = new ItemType("", TAX);
	}
	
	@Test(expected = TaxException.class)
	public void negativeTAX() {
		ItemType itemType = new ItemType(ITEM_TYPE, -20);
	}
	
	@After
	public void tearDown() {
		
	}

}