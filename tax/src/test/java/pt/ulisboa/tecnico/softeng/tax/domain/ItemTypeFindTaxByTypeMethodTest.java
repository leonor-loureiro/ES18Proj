package pt.ulisboa.tecnico.softeng.tax.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class ItemTypeFindTaxByTypeMethodTest {
	private final String ITEM_TYPE = "batatas";
	private final int TAX = 23;
	
	@Before
	public void setUp() {
		ItemType itemType = new ItemType(ITEM_TYPE, TAX);
	}

	@Test
	public void success() {
		int tax = ItemType.findTaxByType(this.ITEM_TYPE);
		Assert.assertEquals(tax, this.TAX);
	}
	
	@Test(expected = TaxException.class)
	public void nonExistentType() {
		int tax = ItemType.findTaxByType("bolachas");
	}
	
	@Test(expected = TaxException.class)
	public void nullType() {
		int tax = ItemType.findTaxByType(null);
	}
	
	@Test
	public void multipleTypes() {
		ItemType itemType = new ItemType("bolachas",19);
		int tax = ItemType.findTaxByType("bolachas");
		Assert.assertEquals(tax, 19);
	}
	

	@After
	public void tearDown() {
		ItemType.itemTypes.clear();
	}

}