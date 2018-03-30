package pt.ulisboa.tecnico.softeng.bank.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankCancelPaymentTest extends RollbackTestAbstractClass {
	private Bank bank;
	private Account account;
	private String reference;

	@Override
	public void populate4Test() {
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

}
