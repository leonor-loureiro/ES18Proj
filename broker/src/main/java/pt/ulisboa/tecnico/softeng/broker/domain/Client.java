package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class Client {
	private final String IBAN;
	private final String NIF;
	private final String drivingLicense;
	private final int age;

	public Client(Broker broker, String IBAN, String NIF, String drivingLicense, int age) {
		checkArguments(broker, IBAN, NIF, drivingLicense, age);
		this.IBAN = IBAN;
		this.NIF = NIF;
		this.drivingLicense = drivingLicense;
		this.age = age;

		broker.addClient(this);
	}

	private void checkArguments(Broker broker, String IBAN, String NIF, String drivingLicense, int age) {
		if (broker == null || IBAN == null || drivingLicense == null ||
				IBAN.trim().isEmpty() || NIF == null || NIF.trim().isEmpty() || drivingLicense.trim().isEmpty()) {
			throw new BrokerException();
		}

		if (age < 0) {
			throw new BrokerException();
		}

		if (broker.getClientByNIF(NIF) != null) {
			throw new BrokerException();
		}

		if (broker.drivingLicenseIsRegistered(drivingLicense)) {
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

	public String getDrivingLicense() {
		return drivingLicense;
	}
}
