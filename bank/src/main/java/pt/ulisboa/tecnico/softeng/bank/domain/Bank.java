package pt.ulisboa.tecnico.softeng.bank.domain;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class Bank extends Bank_Base {
	public static final int CODE_SIZE = 4;

	public Bank(String name, String code) {
		checkArguments(name, code);

		setName(name);
		setCode(code);

		FenixFramework.getDomainRoot().addBank(this);
	}

	public void delete() {
		setRoot(null);

		for (Client client : getClientSet()) {
			client.delete();
		}

		for (Account account : getAccountSet()) {
			account.delete();
		}

		for (Operation operation : getOperationSet()) {
			operation.delete();
		}

		deleteDomainObject();
	}

	private void checkArguments(String name, String code) {
		if (name == null || code == null || name.trim().equals("") || code.trim().equals("")) {
			throw new BankException();
		}

		if (code.length() != Bank.CODE_SIZE) {
			throw new BankException();
		}

		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			if (bank.getCode().equals(code)) {
				throw new BankException();
			}
		}
	}

	@Override
	public int getClientCounter() {
		int counter = super.getClientCounter() + 1;
		setClientCounter(counter);
		return counter;
	}

	@Override
	public int getAccountCounter() {
		int counter = super.getAccountCounter() + 1;
		setAccountCounter(counter);
		return counter;
	}

	@Override
	public int getOperationCounter() {
		int counter = super.getOperationCounter() + 1;
		setOperationCounter(counter);
		return counter;
	}

	public Account getAccount(String IBAN) {
		if (IBAN == null || IBAN.trim().equals("")) {
			throw new BankException();
		}

		for (Account account : getAccountSet()) {
			if (account.getIBAN().equals(IBAN)) {
				return account;
			}
		}

		return null;
	}

	public Operation getOperation(String reference) {
		for (Operation operation : getOperationSet()) {
			if (operation.getReference().equals(reference)) {
				return operation;
			}
		}
		return null;
	}

	public Operation getOperationbyAdventureId(String adventureId) {
		for (Operation operation : getOperationSet()) {
			if (operation.getAdventureId() != null && operation.getAdventureId().equals(adventureId)) {
				return operation;
			}
		}
		return null;
	}

	public Client getClientById(String id) {
		return getClientSet().stream().filter(c -> c.getID().equals(id)).findFirst().orElse(null);
	}

}
