package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class Client {
	private final String IBAN;
	private final String NIF;
	private final int age;

	public Client(Broker broker, String IBAN, String NIF, int age) {
		checkArguments(broker, IBAN, NIF, age);
		this.IBAN = IBAN;
		this.NIF = NIF;
		this.age = age;

		broker.addClient(this);
	}

	private void checkArguments(Broker broker, String IBAN, String NIF, int age) {
		if (broker == null || IBAN == null || IBAN.trim().equals("") || NIF == null || NIF.trim().equals("")) {
			throw new BrokerException();
		}

		if (age < 0) {
			throw new BrokerException();
		}

		if (broker.getClientByNIF(NIF) != null) {
			throw new BrokerException();
		}

	}

	public String getIBAN() {
		return this.IBAN;
	}

	public String getNIF() {
		return this.NIF;
	}

	public int getAge() {
		return this.age;
	}

}
