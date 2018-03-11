package pt.ulisboa.tecnico.softeng.tax.domain;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class IRSGetItemByNameMethodTest {
	private ItemType item;
	
	@Before
	public void setUp() {
		this.item = new ItemType("leite", 21);
	}
	
	@Test
	public void success() {
		ItemType data = IRS.getItemTypeByName("leite");
		
		Assert.assertNotNull(data);
		Assert.assertEquals(data.getItemType(), this.item.getItemType());
		Assert.assertEquals(data.getTax(), this.item.getTax());
	}
	
	@Test(expected = TaxException.class)
	public void nullName() {
		IRS.getItemTypeByName(null);
	}
	
	@Test(expected = TaxException.class)
	public void blankName() {
		IRS.getItemTypeByName("  ");
	}
	
	@Test(expected = TaxException.class)
	public void emptyName() {
		IRS.getItemTypeByName("");
	}
	
	@After
	public void tearDown() {
		ItemType.itemTypes.clear();
	}
}