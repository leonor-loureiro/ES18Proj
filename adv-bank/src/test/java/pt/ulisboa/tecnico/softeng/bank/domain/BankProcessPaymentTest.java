package pt.ulisboa.tecnico.softeng.bank.domain;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

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
		String iban = this.account.getIBAN();
		String newReference = Bank.processPayment(iban, 50);

		assertNotNull(newReference);
		assertTrue(newReference.startsWith("BK01"));

		assertNotNull(this.bank.getOperation(newReference));
		assertEquals(Operation.Type.WITHDRAW, this.bank.getOperation(newReference).getType());
	}

	@Test
	public void twoBanks() {
		Bank bank = new Bank("OtherDream", "BK02");
		Client client = new Client(bank, "Manuel");
		Account account = new Account(bank, client);
		account.deposit(10);

		String reference = Bank.processPayment(account.getIBAN(), 10);

		assertNotNull(reference);
		assertTrue(reference.startsWith("BK02"));

		assertNotNull(bank.getOperation(reference));
		assertEquals(Operation.Type.WITHDRAW, bank.getOperation(reference).getType());
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
		Bank.processPayment(this.account.getIBAN(), 10);
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}