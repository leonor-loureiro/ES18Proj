package pt.ulisboa.tecnico.softeng.tax.services.local;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.RollbackTestAbstractClass;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.TaxPayerData;

public class TaxInterfaceGetTaxPayersTest extends RollbackTestAbstractClass {
	private static final String BUYER_NIF = "123456789";
	private static final String BUYER_NAME = "Toze";
	private static final String BUYER_ADDRESS = "Rua das Couves, no.1";

	private static final String SELLER_NIF = "987654321";
	private static final String SELLER_NAME = "Tozerino";
	private static final String SELLER_ADDRESS = "Rua das Couves, no.2";
	
	
	@Override
	public void populate4Test() {
		IRS irs = IRS.getIRSInstance();
		Buyer buyer = new Buyer(irs, BUYER_NIF, BUYER_NAME, BUYER_ADDRESS);
		Seller seller = new Seller(irs, SELLER_NIF, SELLER_NAME, SELLER_ADDRESS);
	}

	@Test
	public void success() {
		List<TaxPayerData> buyers = TaxInterface.getBuyers();
		
		assertEquals(1, buyers.size());
		
		TaxPayerData b1 = buyers.get(0);

		assertEquals(BUYER_NIF, b1.getNif());
		assertEquals(BUYER_NAME, b1.getName());
		assertEquals(BUYER_ADDRESS, b1.getAddress());
		
		List<TaxPayerData> sellers = TaxInterface.getSellers();
		
		assertEquals(1, sellers.size());
		
		TaxPayerData s1 = sellers.get(0);

		assertEquals(SELLER_NIF, s1.getNif());
		assertEquals(SELLER_NAME, s1.getName());
		assertEquals(SELLER_ADDRESS, s1.getAddress());
	}
}
