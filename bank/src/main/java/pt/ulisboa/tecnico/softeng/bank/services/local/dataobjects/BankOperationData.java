package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;

import org.joda.time.DateTime;

import pt.ulisboa.tecnico.softeng.bank.domain.Operation;

public class BankOperationData {
	private String reference;
	private String type;
	private String iban;
	private int value;
	private DateTime time;

	public BankOperationData() {
	}

	public BankOperationData(Operation operation) {
		this.reference = operation.getReference();
		this.type = operation.getType().name();
		this.iban = operation.getAccount().getIBAN();
		this.value = operation.getValue();
		this.time = operation.getTime();
	}

	public String getReference() {
		return this.reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public DateTime getTime() {
		return this.time;
	}

	public void setTime(DateTime time) {
		this.time = time;
	}
}
