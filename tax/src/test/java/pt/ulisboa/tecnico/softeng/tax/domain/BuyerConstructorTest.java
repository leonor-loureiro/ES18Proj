package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class BuyerConstructorTest {
	private static final String ADDRESS = "Somewhere";
	private static final String NAME = "José Vendido";
	private static final String NIF = "123456789";

	@Test
	public void success() {
		Buyer buyer = new Buyer(NIF, NAME, ADDRESS);

		assertEquals(NIF, buyer.getNIF());
		assertEquals(NAME, buyer.getName());
		assertEquals(ADDRESS, buyer.getAddress());

		assertEquals(buyer, IRS.getIRS().getTaxPayerByNIF(NIF));
	}

	@Test
	public void uniqueNIF() {
		Buyer seller = new Buyer(NIF, NAME, ADDRESS);

		try {
			new Buyer(NIF, NAME, ADDRESS);
			fail();
		} catch (TaxException ie) {
			assertEquals(seller, IRS.getIRS().getTaxPayerByNIF(NIF));
		}
	}

	@Test(expected = TaxException.class)
	public void nullNIF() {
		new Buyer(null, NAME, ADDRESS);
	}

	@Test(expected = TaxException.class)
	public void emptyNIF() {
		new Buyer("", NAME, ADDRESS);
	}

	@Test(expected = TaxException.class)
	public void nonNineDigitsNIF() {
		new Buyer("12345678", NAME, ADDRESS);
	}

	@Test(expected = TaxException.class)
	public void nullName() {
		new Buyer(NIF, null, ADDRESS);
	}

	@Test(expected = TaxException.class)
	public void emptyName() {
		new Buyer(NIF, "", ADDRESS);
	}

	@Test(expected = TaxException.class)
	public void nullAddress() {
		new Buyer(NIF, NAME, null);
	}

	@Test(expected = TaxException.class)
	public void emptyAddress() {
		new Buyer(NIF, NAME, "");
	}

	@After
	public void tearDown() {
		IRS.getIRS().clearAll();
	}

}
