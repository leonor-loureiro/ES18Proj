package pt.ulisboa.tecnico.softeng.iva.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.iva.exception.IvaException;

public class ItemTypeConstructorTest {
	private static final String CAR = "CAR";
	private static final int TAX = 23;

	@Before
	public void setUp() {
		IRS.getIRS().clearAll();
	}

	@Test
	public void success() {
		ItemType itemType = new ItemType(CAR, TAX);

		assertEquals(CAR, itemType.getName());
		assertEquals(TAX, itemType.tax);
		assertNotNull(IRS.getIRS().getItemTypeByName(CAR));
	}

	@Test(expected = IvaException.class)
	public void nullItemType() {
		new ItemType(null, TAX);
	}

	@Test(expected = IvaException.class)
	public void emptyItemType() {
		new ItemType("", TAX);
	}

	@Test(expected = IvaException.class)
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
