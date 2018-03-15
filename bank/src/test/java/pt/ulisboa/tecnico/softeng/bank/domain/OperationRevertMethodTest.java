package pt.ulisboa.tecnico.softeng.bank.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OperationRevertMethodTest {
	private Bank bank;
	private Account account;

	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");
		Client client = new Client(this.bank, "António");
		this.account = new Account(this.bank, client);
	}

	@Test
	public void revertDeposit() {
		String reference = this.account.deposit(100);
		Operation operation = this.bank.getOperation(reference);

		String newReference = operation.revert();

		assertEquals(0, this.account.getBalance(), 0);
		assertNotNull(this.bank.getOperation(newReference));
		assertNotNull(this.bank.getOperation(reference));
	}

	@Test
	public void revertWithdraw() {
		this.account.deposit(1000);
		String reference = this.account.withdraw(100);
		Operation operation = this.bank.getOperation(reference);

		String newReference = operation.revert();

		assertEquals(1000, this.account.getBalance(), 0);
		assertNotNull(this.bank.getOperation(newReference));
		assertNotNull(this.bank.getOperation(reference));
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}
