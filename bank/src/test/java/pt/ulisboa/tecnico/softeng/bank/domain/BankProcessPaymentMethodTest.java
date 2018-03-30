package pt.ulisboa.tecnico.softeng.bank.domain;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankProcessPaymentMethodTest extends RollbackTestAbstractClass {
	private Bank bank;
	private Account account;
	private String iban;

	@Override
	public void populate4Test() {
		this.bank = new Bank("Money", "BK01");
		Client client = new Client(this.bank, "António");
		this.account = new Account(this.bank, client);
		this.iban = this.account.getIBAN();
		this.account.deposit(500);
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
	public void successTwoBanks() {
		Bank otherBank = new Bank("Money", "BK02");
		Client otherClient = new Client(otherBank, "Manuel");
		Account otherAccount = new Account(otherBank, otherClient);
		String otherIban = otherAccount.getIBAN();
		otherAccount.deposit(1000);

		Bank.processPayment(otherIban, 100);
		assertEquals(900, otherAccount.getBalance(), 0.0d);

		Bank.processPayment(this.iban, 100);
		assertEquals(400, this.account.getBalance(), 0.0d);
	}

	@Test
	public void twoOtherBanks() {
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
	public void nullIban() {
		Bank.processPayment(null, 100);
	}

	@Test(expected = BankException.class)
	public void emptyIban() {
		Bank.processPayment("  ", 100);
	}

	@Test(expected = BankException.class)
	public void zeroAmount() {
		Bank.processPayment(this.iban, 0);
	}

	@Test
	public void oneAmount() {
		Bank.processPayment(this.iban, 1);

		assertEquals(499, this.account.getBalance(), 0.0d);
	}

	@Test(expected = BankException.class)
	public void notExistIban() {
		Bank.processPayment("other", 0);
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
		FenixFramework.getDomainRoot().getBankSet().clear();
		Bank.processPayment(this.account.getIBAN(), 10);
	}
}
