package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BankProcessPaymentTest {

    private Bank bank;
    private Account account;
    private String reference;

    @Before
    public void setUp() {
        this.bank = new Bank("Money", "BK01");
        Client client = new Client(this.bank, "António");
        this.account = new Account(this.bank, client);
        this.reference = this.account.deposit(100);
    }

    @Test
    public void success() {
        String iban = account.getIBAN();
        String newReference = Bank.processPayment(iban, 50);

        assertNotNull(newReference);
        assertTrue(newReference.startsWith("BK01"));

        assertNotNull(this.bank.getOperation(newReference));
        assertEquals(Operation.Type.WITHDRAW, this.bank.getOperation(newReference).getType());
	}



    @Test(expected = BankException.class)
    public void nullReference() {
        Bank.processPayment(null, 10);
    }

    @Test(expected = BankException.class)
    public void emptyReference() {
        Bank.processPayment("", 10);
    }

    @Test(expected = BankException.class)
    public void notExistsReference() {
        Bank.processPayment("XPTO", 10);
    }

    @Test(expected = BankException.class)
    public void noBanks() {
        Bank.banks.clear();
	    Bank.processPayment(account.getIBAN(), 10);
    }

	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}
