package pt.ulisboa.tecnico.softeng.bank.services.local;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.bank.domain.RollbackTestAbstractClass;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankInterfaceProcessPaymentMethodTest extends RollbackTestAbstractClass {
	private static final String ADVENTURE_ID = "adventureId";

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
		BankInterface.processPayment(this.iban, 100, ADVENTURE_ID);

		assertEquals(400, this.account.getBalance());
	}

	@Test
	public void successTwoBanks() {
		Bank otherBank = new Bank("Money", "BK02");
		Client otherClient = new Client(otherBank, "Manuel");
		Account otherAccount = new Account(otherBank, otherClient);
		String otherIban = otherAccount.getIBAN();
		otherAccount.deposit(1000);

		BankInterface.processPayment(otherIban, 100, ADVENTURE_ID);
		assertEquals(900, otherAccount.getBalance());

		BankInterface.processPayment(this.iban, 100, ADVENTURE_ID + "PLUS");
		assertEquals(400, this.account.getBalance());
	}

	@Test(expected = BankException.class)
	public void nullIban() {
		BankInterface.processPayment(null, 100, ADVENTURE_ID);
	}

	@Test(expected = BankException.class)
	public void emptyIban() {
		BankInterface.processPayment("  ", 100, ADVENTURE_ID);
	}

	@Test(expected = BankException.class)
	public void zeroAmount() {
		BankInterface.processPayment(this.iban, 0, ADVENTURE_ID);
	}

	@Test
	public void oneAmount() {
		BankInterface.processPayment(this.iban, 1, ADVENTURE_ID);

		assertEquals(499, this.account.getBalance());
	}

	@Test(expected = BankException.class)
	public void notExistIban() {
		BankInterface.processPayment("other", 0, ADVENTURE_ID);
	}

}
