package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class ItemTypeConstructorTest {
	private final String ITEM_TYPE = "batatas";
	private final int TAX = 23;
	
	@Test
	public void success() {
		ItemType itemType = new ItemType(ITEM_TYPE, TAX);
		
		Assert.assertEquals(ITEM_TYPE, itemType.getItemType());
		Assert.assertEquals(TAX, itemType.getTax());
		Assert.assertTrue(itemType.getTax() >= 0);
		Assert.assertEquals(1, ItemType.itemTypes.size());
	}
	 

	@Test(expected = TaxException.class)
	public void nullItemType() {
		ItemType itemType = new ItemType(null, TAX);
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
	
	@Test
	public void diffItemType() {
		ItemType itemType = new ItemType(ITEM_TYPE, TAX);
		ItemType itemType2 = new ItemType("bolachas", TAX);
	}
	
	@Test(expected = TaxException.class)
	public void itemTypeNotUnique() {
		ItemType itemType = new ItemType(ITEM_TYPE, TAX);
		ItemType itemType2 = new ItemType(ITEM_TYPE, TAX + 10);		
	}
	
	
	@After
	public void tearDown() {
		ItemType.itemTypes.clear();
	}

}