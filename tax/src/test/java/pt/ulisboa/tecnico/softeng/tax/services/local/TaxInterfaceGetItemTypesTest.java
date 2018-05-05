package pt.ulisboa.tecnico.softeng.tax.services.local;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.ItemType;
import pt.ulisboa.tecnico.softeng.tax.domain.RollbackTestAbstractClass;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.ItemTypeData;

public class TaxInterfaceGetItemTypesTest extends RollbackTestAbstractClass {
	private static final String ITEM_NAME = "Peixe";
	private static final int ITEM_TAX = 23;	
	
	@Override
	public void populate4Test() {
		IRS irs = IRS.getIRSInstance();
		ItemType itemType = new ItemType(irs, ITEM_NAME, ITEM_TAX);
	}

	@Test
	public void success() {
		List<ItemTypeData> items = TaxInterface.getItemTypes();
		
		assertEquals(1, items.size());
		
		ItemTypeData i1 = items.get(0);

		assertEquals(ITEM_NAME, i1.getName());
		assertEquals(ITEM_TAX, i1.getTax());
	}
}
