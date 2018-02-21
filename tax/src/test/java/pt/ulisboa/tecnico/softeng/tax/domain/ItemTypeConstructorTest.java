package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class ItemTypeConstructorTest {
	private static final String CAR = "CAR";
	private static final int TAX = 23;

	@Test
	public void success() {
		ItemType itemType = new ItemType(CAR, TAX);

		assertEquals(CAR, itemType.getName());
		assertEquals(TAX, itemType.tax);
		assertNotNull(IRS.getIRS().getItemTypeByName(CAR));

		assertEquals(itemType, IRS.getIRS().getItemTypeByName(CAR));
	}

	@Test
	public void uniqueName() {
		ItemType itemType = new ItemType(CAR, TAX);

		try {
			new ItemType(CAR, TAX);
			fail();
		} catch (TaxException te) {
			assertEquals(itemType, IRS.getIRS().getItemTypeByName(CAR));
		}
	}

	@Test(expected = TaxException.class)
	public void nullItemType() {
		new ItemType(null, TAX);
	}

	@Test(expected = TaxException.class)
	public void emptyItemType() {
		new ItemType("", TAX);
	}

	@Test(expected = TaxException.class)
	public void negativeTax() {
		new ItemType(CAR, -34);
	}

	public void zeroTax() {
		new ItemType(CAR, 0);
	}

	@After
	public void tearDown() {
		IRS.getIRS().clearAll();
	}

}
