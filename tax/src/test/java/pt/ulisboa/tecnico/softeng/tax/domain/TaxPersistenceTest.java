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

    private static final double INVOICE_VALUE = 100;
    private static final LocalDate INVOICE_DATE = new LocalDate();


    @Test
    public void success() {
        atomicProcess();
        atomicAssert();
    }

    @Atomic(mode = TxMode.WRITE)
    public void atomicProcess() {
        //ItemType itemType = new ItemType(IRS, ITEMTYPE_NAME, ITEMTYPE_TAX);

        //Invoice invoice = new Invoice(INVOICE_VALUE, INVOICE_DATE, itemType, seller, buyer);

    }

    @Atomic(mode = TxMode.READ)
    public void atomicAssert() {

    }

    @After
    @Atomic(mode = TxMode.WRITE)
    public void tearDown() {

    }
}
