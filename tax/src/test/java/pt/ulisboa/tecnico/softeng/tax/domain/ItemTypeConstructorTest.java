package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class ItemTypeConstructorTest extends RollbackTestAbstractClass {
	private static final String CAR = "CAR";
	private static final int TAX = 23;

	IRS irs;

	@Override
	public void populate4Test() {
		this.irs = IRS.getIRS();
	}

	@Test
	public void success() {
		IRS irs = IRS.getIRS();

		ItemType itemType = new ItemType(irs, CAR, TAX);

		assertEquals(CAR, itemType.getName());
		assertEquals(TAX, itemType.tax);
		assertNotNull(IRS.getIRS().getItemTypeByName(CAR));

		assertEquals(itemType, irs.getItemTypeByName(CAR));
	}

	@Test
	public void uniqueName() {
		ItemType itemType = new ItemType(this.irs, CAR, TAX);

		try {
			new ItemType(this.irs, CAR, TAX);
			fail();
		} catch (TaxException te) {
			assertEquals(itemType, IRS.getIRS().getItemTypeByName(CAR));
		}
	}

	@Test(expected = TaxException.class)
	public void nullItemType() {
		new ItemType(this.irs, null, TAX);
	}

	@Test(expected = TaxException.class)
	public void emptyItemType() {
		new ItemType(this.irs, "", TAX);
	}

	@Test(expected = TaxException.class)
	public void negativeTax() {
		new ItemType(this.irs, CAR, -34);
	}

	public void zeroTax() {
		new ItemType(this.irs, CAR, 0);
	}

	@Override
	public void tearDownNotPersistent() {
		IRS.getIRS().clearAll();
	}

}
