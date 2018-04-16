package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class TaxPersistenceTest {

    private static final String ITEMTYPE_NAME = "Bina";
    private static final int ITEMTYPE_TAX = 23;

    private static final String BUYER_NIF = "123456789";
    private static final String BUYER_NAME = "Toze";
    private static final String BUYER_ADDRESS = "Rua das Couves, No. 1";

    private static final String SELLER_NIF = "987654321";
    private static final String SELLER_NAME = "Tozerino";
    private static final String SELLER_ADDRESS = "Rua das Couves, No. 2";
    
    private static final double INVOICE_VALUE = 100;
    private static final LocalDate INVOICE_DATE = new LocalDate();


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
        
        Invoice invoice = new Invoice(INVOICE_VALUE, INVOICE_DATE, itemType, seller, buyer);
    }

    @Atomic(mode = TxMode.READ)
    public void atomicAssert() {

    }

    @After
    @Atomic(mode = TxMode.WRITE)
    public void tearDown() {
    	
    }
}
