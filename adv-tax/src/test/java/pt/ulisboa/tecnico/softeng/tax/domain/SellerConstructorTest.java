package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class SellerConstructorTest {
	private static final String ADDRESS = "Somewhere";
	private static final String NAME = "José Vendido";
	private static final String NIF = "123456789";

	IRS irs;

	@Before
	public void setUp() {
		this.irs = IRS.getIRS();
	}

	@Test
	public void success() {
		Seller seller = new Seller(this.irs, NIF, NAME, ADDRESS);

		assertEquals(NIF, seller.getNIF());
		assertEquals(NAME, seller.getName());
		assertEquals(ADDRESS, seller.getAddress());

		assertEquals(seller, IRS.getIRS().getTaxPayerByNIF(NIF));
	}

	@Test
	public void uniqueNIF() {
		Seller seller = new Seller(this.irs, NIF, NAME, ADDRESS);

		try {
			new Seller(this.irs, NIF, NAME, ADDRESS);
			fail();
		} catch (TaxException ie) {
			assertEquals(seller, IRS.getIRS().getTaxPayerByNIF(NIF));
		}
	}

	@Test(expected = TaxException.class)
	public void nullNIF() {
		new Seller(this.irs, null, NAME, ADDRESS);
	}

	@Test(expected = TaxException.class)
	public void emptyNIF() {
		new Seller(this.irs, "", NAME, ADDRESS);
	}

	@Test(expected = TaxException.class)
	public void nullName() {
		new Seller(this.irs, NIF, null, ADDRESS);
	}

	@Test(expected = TaxException.class)
	public void emptyName() {
		new Seller(this.irs, NIF, "", ADDRESS);
	}

	@Test(expected = TaxException.class)
	public void nullAddress() {
		new Seller(this.irs, NIF, NAME, null);
	}

	@Test(expected = TaxException.class)
	public void emptyAddress() {
		new Seller(this.irs, NIF, NAME, "");
	}

	@After
	public void tearDown() {
		IRS.getIRS().clearAll();
	}

}
