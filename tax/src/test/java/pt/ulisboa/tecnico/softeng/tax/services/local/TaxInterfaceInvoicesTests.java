package pt.ulisboa.tecnico.softeng.tax.services.local;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.Invoice;
import pt.ulisboa.tecnico.softeng.tax.domain.ItemType;
import pt.ulisboa.tecnico.softeng.tax.domain.RollbackTestAbstractClass;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.TaxPayerData;

public class TaxInterfaceInvoicesTests extends RollbackTestAbstractClass {
	private static final String BUYER_NIF = "123456789";
	private static final String BUYER_NAME = "Toze";
	private static final String BUYER_ADDRESS = "Rua das Couves, no.1";

	private static final String SELLER_NIF = "987654321";
	private static final String SELLER_NAME = "Tozerino";
	private static final String SELLER_ADDRESS = "Rua das Couves, no.2";
	
	private static final String ITEM_NAME = "Peixe";
	private static final int ITEM_TAX = 23;
	
	private static final String INVOICE_REFERENCE = "1";
	private static final boolean INVOICE_CANCELLED = false;
	private static final LocalDate INVOICE_DATE = new LocalDate(2018, 01, 01);
	private static final Double INVOICE_VALUE = new Double(350);
	private static final Double INVOICE_IVA = new Double((ITEM_TAX * INVOICE_VALUE) / 100);
	
	@Override
	public void populate4Test() {
		TaxInterface.initIRS();
		Buyer buyer = new Buyer(FenixFramework.getDomainRoot().getIrs(), BUYER_NIF, BUYER_NAME, BUYER_ADDRESS);
		Seller seller = new Seller(FenixFramework.getDomainRoot().getIrs(), SELLER_NIF, SELLER_NAME, SELLER_ADDRESS);
		ItemType itemType = new ItemType(FenixFramework.getDomainRoot().getIrs(), ITEM_NAME, ITEM_TAX);
	}
	
	@Test
	public void success() {
		InvoiceData invoice = new InvoiceData();
		invoice.setBuyerNIF(BUYER_NIF);
		invoice.setCancelled(INVOICE_CANCELLED);
		invoice.setDate(INVOICE_DATE);
		invoice.setItemType(ITEM_NAME);
		invoice.setIva(INVOICE_IVA);
		invoice.setReference(INVOICE_REFERENCE);
		invoice.setSellerNIF(SELLER_NIF);
		invoice.setValue(INVOICE_VALUE);
		
		TaxInterface.createInvoice(invoice);
		
		Invoice inv = (Invoice) FenixFramework.getDomainRoot().getIrs().getInvoiceSet().toArray()[0];
		
		assertEquals(INVOICE_REFERENCE, inv.getReference());
		assertEquals(INVOICE_CANCELLED, inv.getCancelled());
		assertEquals(SELLER_NIF, inv.getSeller().getNif());
		assertEquals(BUYER_NIF, inv.getBuyer().getNif());
		assertEquals(ITEM_NAME, inv.getItemType().getName());
		assertEquals((int) INVOICE_IVA.doubleValue(), (int) inv.getIva());
		assertEquals((int) INVOICE_VALUE.doubleValue(), (int) inv.getValue());
		assertEquals(INVOICE_DATE, inv.getDate());
		
		TaxPayerData buyer = TaxInterface.getTaxPayerDataByNif(BUYER_NIF);
		
		assertEquals(BUYER_NIF, buyer.getNif());
		assertEquals(BUYER_NAME, buyer.getName());
		assertEquals(BUYER_ADDRESS, buyer.getAddress());
		
		TaxPayerData seller = TaxInterface.getTaxPayerDataByNif(SELLER_NIF);
		
		assertEquals(SELLER_NIF, seller.getNif());
		assertEquals(SELLER_NAME, seller.getName());
		assertEquals(SELLER_ADDRESS, seller.getAddress());
	}

}
