package pt.ulisboa.tecnico.softeng.tax.domain;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

public class IRSGetItemByNameMethodTest {
	private IRS irs;
	private ItemType item;
	
	@Before
	public void setUp() {
		this.irs = new IRS();
		this.item = new ItemType("leite", 21);
	}
	
	@Test
	public void success() {
		ItemType data = irs.getItemTypeByName("leite");
		
		Assert.assertNotNull(data);
		Assert.assertEquals(data.getName(), this.item.getName());
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
