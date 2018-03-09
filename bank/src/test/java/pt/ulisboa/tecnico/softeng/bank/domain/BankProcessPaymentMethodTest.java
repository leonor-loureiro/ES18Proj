package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankProcessPaymentMethodTest {
	Bank bank;
	Account account;
	Client client;

	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");
		this.client = new Client(this.bank, "António");
		this.account = new Account(this.bank, this.client);
		this.account.deposit(100);
	}

	@Test
	public void success() {
		String reference = Bank.processPayment(this.account.getIBAN(), 50);
		Operation operation = this.bank.getOperation(reference);
		
		Assert.assertNotNull(operation);
		Assert.assertEquals(Operation.Type.WITHDRAW, operation.getType());
		Assert.assertEquals(50, operation.getValue());
		Assert.assertEquals(this.account.getIBAN(), operation.getAccount().getIBAN());
	}

	@Test(expected = BankException.class)
	public void nullIBAN() {
		Bank.processPayment(null,50);
	}
	
	@Test(expected = BankException.class)
	public void unexistingIBAN() {
		Bank.processPayment("XPTO",50);
	}
	
	@Test
	public void multipleBanks() {
		Bank newBank = new Bank("Money1","BK02");
		Client newClient = new Client(newBank, "Manel");
		Account newAccount = new Account(newBank, newClient);
		newAccount.deposit(100);
		String reference = Bank.processPayment(newAccount.getIBAN(),50);
		Assert.assertNotNull(reference);
	}
	
	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}