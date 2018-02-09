package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;

import static org.junit.Assert.assertEquals;

public class BankOperationDataTest {
    private static int AMOUNT = 100;
    private Bank bank;
    private Account account;
    private String reference;

    @Before
    public void setUp() {
        this.bank = new Bank("Money", "BK01");
        Client client = new Client(this.bank, "António");
        this.account = new Account(this.bank, client);
        this.reference = this.account.deposit(AMOUNT);
    }

    @Test
    public void GetsAndSetters() {
        BankOperationData d0 = Bank.getOperationData(this.reference);
        BankOperationData d1 = Bank.getOperationData(this.reference);

        d1.setIban(d1.getIban());
        d1.setReference(d1.getReference());
        d1.setTime(d1.getTime());
        d1.setType(d1.getType());
        d1.setValue(d1.getValue());

        assertEquals(d0.getIban(), d1.getIban());
        assertEquals(d0.getReference(), d1.getReference());
        assertEquals(d0.getTime(), d1.getTime());
        assertEquals(d0.getType(), d1.getType());
        assertEquals(d0.getValue(), d1.getValue());
    }

    @After
    public void tearDown() {
        Bank.banks.clear();
    }

}
