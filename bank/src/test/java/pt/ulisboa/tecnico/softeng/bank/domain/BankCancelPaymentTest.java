package pt.ulisboa.tecnico.softeng.bank.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankCancelPaymentTest {
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
		String newReference = Bank.cancelPayment(this.reference);

		assertNotNull(this.bank.getOperation(newReference));
	}

	@Test(expected = BankException.class)
	public void nullReference() {
		Bank.cancelPayment(null);
	}

	@Test(expected = BankException.class)
	public void emptyReference() {
		Bank.cancelPayment("");
	}

	@Test(expected = BankException.class)
	public void notExistsReference() {
		Bank.cancelPayment("XPTO");
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}
