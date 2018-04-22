package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;

public class TaxPersistenceTest {

    private static final String ITEMTYPE_NAME = "Bina";
    private static final int ITEMTYPE_TAX = 23;

    private static final String BUYER_NIF = "123456789";
    private static final String BUYER_NAME = "Toze";
    private static final String BUYER_ADDRESS = "Rua das Couves, No. 1";

    private static final String SELLER_NIF = "987654321";
    private static final String SELLER_NAME = "Tozerino";
    private static final String SELLER_ADDRESS = "Rua das Couves, No. 2";
    
    private static final double INVOICE_VALUE1 = 100;
    private static final LocalDate INVOICE_DATE1 = new LocalDate(2018,5,4);
    
    private static final double INVOICE_VALUE2 = 50;
    private static final LocalDate INVOICE_DATE2 = new LocalDate(2018,5,5);

    @Test
    public void success() {
        atomicProcess();
        atomicAssert();
    }

    @Atomic(mode = TxMode.WRITE)
    public void atomicProcess() {
    	IRS irs = IRS.getIRS();
        ItemType itemType = new ItemType(irs, ITEMTYPE_NAME, ITEMTYPE_TAX);

        Buyer buyer = new Buyer(irs, BUYER_NIF, BUYER_NAME, BUYER_ADDRESS);
        Seller seller = new Seller(irs, SELLER_NIF, SELLER_NAME, SELLER_ADDRESS);
        		
        Invoice invoice = new Invoice(INVOICE_VALUE1, INVOICE_DATE1, itemType, seller, buyer);
        		
        InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, ITEMTYPE_NAME, INVOICE_VALUE2, INVOICE_DATE2);
        String invoiceRef = IRS.submitInvoice(invoiceData);
        IRS.cancelInvoice(invoiceRef);
    }

    @Atomic(mode = TxMode.READ)
    public void atomicAssert() {
        IRS irs = FenixFramework.getDomainRoot().getIrs();

        assertNotNull(irs);

        List<ItemType> itemTypes = new ArrayList<>(irs.getItemTypeSet());
        ItemType itemType = itemTypes.get(0);

        assertEquals(ITEMTYPE_NAME, itemType.getName());
        assertEquals(ITEMTYPE_TAX, itemType.getTax());
        assertEquals(irs.getItemTypeByName(ITEMTYPE_NAME), itemType);

        List<TaxPayer> taxPayers = new ArrayList<>(irs.getTaxPayerSet());
        TaxPayer taxPayer = taxPayers.get(1);

        assertEquals(BUYER_NIF, taxPayer.getNIF());
        assertEquals(BUYER_NAME, taxPayer.getName());
        assertEquals(BUYER_ADDRESS, taxPayer.getAddress());
        assertEquals(irs.getTaxPayerByNIF(BUYER_NIF), taxPayer);
        
        Buyer buyer = (Buyer) taxPayer;
        TaxPayer taxPayer1 = taxPayers.get(0);

        assertEquals(SELLER_NIF, taxPayer1.getNIF());
        assertEquals(SELLER_NAME, taxPayer1.getName());
        assertEquals(SELLER_ADDRESS, taxPayer1.getAddress());
        assertEquals(irs.getTaxPayerByNIF(SELLER_NIF), taxPayer1);
        
        Seller seller = (Seller) taxPayer1;
        List<Invoice> buyerInvoices = new ArrayList<>(buyer.getInvoiceSet());
        Invoice invoice0 = buyerInvoices.get(0);
        
        assertEquals((int) INVOICE_VALUE1, (int) invoice0.getValue());
        assertEquals(INVOICE_DATE1, invoice0.getDate());
        
        List<Invoice> sellerInvoices = new ArrayList<>(seller.getInvoiceSet());
        Invoice invoice1 = sellerInvoices.get(0);
        
        assertEquals((int) invoice1.getValue(), (int) invoice0.getValue());
        assertEquals(invoice1.getDate(), invoice0.getDate());
        
        Invoice invoice2 = sellerInvoices.get(1);
        
        assertEquals(invoice2.getDate(), INVOICE_DATE2);
        assertTrue(invoice2.getCancelled());
    }

    @After
    @Atomic(mode = TxMode.WRITE)
    public void tearDown() {
    	FenixFramework.getDomainRoot().getIrs().delete();
    }
}
