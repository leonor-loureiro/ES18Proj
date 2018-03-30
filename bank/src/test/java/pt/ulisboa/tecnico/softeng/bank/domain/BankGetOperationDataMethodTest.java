package pt.ulisboa.tecnico.softeng.bank.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation.Type;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankGetOperationDataMethodTest extends RollbackTestAbstractClass {
	private static int AMOUNT = 100;
	private Bank bank;
	private Account account;
	private String reference;

	@Override
	public void populate4Test() {
		this.bank = new Bank("Money", "BK01");
		Client client = new Client(this.bank, "António");
		this.account = new Account(this.bank, client);
		this.reference = this.account.deposit(AMOUNT);
	}

	@Test
	public void success() {
		BankOperationData data = Bank.getOperationData(this.reference);

		assertEquals(this.reference, data.getReference());
		assertEquals(this.account.getIBAN(), data.getIban());
		assertEquals(Type.DEPOSIT.name(), data.getType());
		assertEquals(AMOUNT, data.getValue(), 0);
		assertNotNull(data.getTime());
	}

	@Test(expected = BankException.class)
	public void nullReference() {
		Bank.getOperationData(null);
	}

	@Test(expected = BankException.class)
	public void emptyReference() {
		Bank.getOperationData("");
	}

	@Test(expected = BankException.class)
	public void referenceNotExists() {
		Bank.getOperationData("XPTO");
	}

}
