package pt.ulisboa.tecnico.softeng.bank.domain;

import java.time.LocalDateTime;

class Operation {
	static enum Type {
		DEPOSIT, WITHDRAW
	};

	private static int counter = 0;

	private final String reference;
	private final Type type;
	private final Account account;
	private final int value;
	private final LocalDateTime time;

	Operation(Type type, Account account, int value) {
		this.reference = account.getBank().getCode() + Integer.toString(++Operation.counter);
		this.type = type;
		this.account = account;
		this.value = value;
		this.time = LocalDateTime.now();

		account.getBank().addLog(this);
	}

	String getReference() {
		return this.reference;
	}

	Type getType() {
		return this.type;
	}

	Account getAccount() {
		return this.account;
	}

	int getValue() {
		return this.value;
	}

	LocalDateTime getTime() {
		return this.time;
	}

}
