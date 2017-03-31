package pt.ulisboa.tecnico.softeng.bank.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

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
		Bank.processPayment(this.iban, 100);

		assertEquals(400, this.account.getBalance());
	}

	@Test
	public void successTwoBanks() {
		Bank otherBank = new Bank("Money", "BK02");
		Client otherClient = new Client(otherBank, "Manuel");
		Account otherAccount = new Account(otherBank, otherClient);
		String otherIban = otherAccount.getIBAN();
		otherAccount.deposit(1000);

		Bank.processPayment(otherIban, 100);
		assertEquals(900, otherAccount.getBalance());

		Bank.processPayment(this.iban, 100);
		assertEquals(400, this.account.getBalance());
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

		assertEquals(499, this.account.getBalance());
	}

	@Test(expected = BankException.class)
	public void notExistIban() {
		Bank.processPayment("other", 0);
	}

}
