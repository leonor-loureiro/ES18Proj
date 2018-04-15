package pt.ulisboa.tecnico.softeng.tax.domain;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.After;
import org.junit.Before;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;

public abstract class RollbackTestAbstractClass {
    @Before
    public void setUp() throws Exception {
        try {
            FenixFramework.getTransactionManager().begin(false);
            populate4Test();
        } catch (WriteOnReadError | NotSupportedException | SystemException e1) {
            e1.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        try {
            FenixFramework.getTransactionManager().rollback();
            tearDownNotPersistent();	//FIXME delete when tax is persistent
        } catch (IllegalStateException | SecurityException | SystemException e) {
            e.printStackTrace();
        }
    }

    public abstract void populate4Test();

    public void tearDownNotPersistent() {}  //FIXME delete when tax is persistent
}
