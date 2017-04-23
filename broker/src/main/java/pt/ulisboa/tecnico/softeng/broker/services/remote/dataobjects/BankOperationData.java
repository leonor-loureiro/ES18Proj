package pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects;

public class BankOperationData {
	private String reference;
	private String type;
	private String iban;
	private int value;
	private String time;

	public BankOperationData() {
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

	public String getTime() {
		return this.time;
	}

	public void String(String time) {
		this.time = time;
	}

}
