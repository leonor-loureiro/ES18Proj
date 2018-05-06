package pt.ulisboa.tecnico.softeng.tax.services.local;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.tax.domain.RollbackTestAbstractClass;
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
		TaxInterface.initIRS();
	}

	@Test
	public void success() {
		TaxPayerData buyer = new TaxPayerData();
		buyer.setIrs(FenixFramework.getDomainRoot().getIrs());
		buyer.setNif(BUYER_NIF);
		buyer.setName(BUYER_NAME);
		buyer.setAddress(BUYER_ADDRESS);
		buyer.setInvoices(null);
		
		TaxInterface.createBuyer(buyer);
		
		List<TaxPayerData> buyers = TaxInterface.getBuyers();
		
		assertEquals(1, buyers.size());
		
		TaxPayerData b1 = buyers.get(0);

		assertEquals(BUYER_NIF, b1.getNif());
		assertEquals(BUYER_NAME, b1.getName());
		assertEquals(BUYER_ADDRESS, b1.getAddress());
		
		TaxPayerData seller = new TaxPayerData();
		seller.setIrs(FenixFramework.getDomainRoot().getIrs());
		seller.setNif(SELLER_NIF);
		seller.setName(SELLER_NAME);
		seller.setAddress(SELLER_ADDRESS);
		seller.setInvoices(null);
		
		TaxInterface.createSeller(seller);
		
		List<TaxPayerData> sellers = TaxInterface.getSellers();
		
		assertEquals(1, sellers.size());
		
		TaxPayerData s1 = sellers.get(0);

		assertEquals(SELLER_NIF, s1.getNif());
		assertEquals(SELLER_NAME, s1.getName());
		assertEquals(SELLER_ADDRESS, s1.getAddress());
		
		List<TaxPayerData> payers = TaxInterface.getTaxPayers();
		
		assertEquals(2, payers.size());
		
		TaxPayerData p1 = payers.get(0);
		TaxPayerData p2 = payers.get(1);
		
		assertEquals(b1.getNif(), p1.getNif());
		assertEquals(s1.getNif(), p2.getNif());
	}
}
