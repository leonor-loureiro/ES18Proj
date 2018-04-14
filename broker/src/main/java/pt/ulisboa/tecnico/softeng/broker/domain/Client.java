package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class Client extends Client_Base {

	public Client(Broker broker, String IBAN, String NIF, String drivingLicense, int age) {
		checkArguments(broker, IBAN, NIF, drivingLicense, age);

		setIBAN(IBAN);
        setNIF(NIF);
        setDrivingLicense(drivingLicense);
        setAge(age);

		broker.addClient(this);
	}

	private void checkArguments(Broker broker, String IBAN, String NIF, String drivingLicense, int age) {
		if (broker == null || IBAN == null || NIF == null ||
				IBAN.trim().isEmpty() || NIF.trim().isEmpty()) {
			throw new BrokerException();
		}

		if (drivingLicense != null && drivingLicense.trim().isEmpty()) {
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

    @Override
	public String getIBAN() { return super.getIBAN(); }

    @Override
	public String getNIF() { return super.getNIF(); }

    @Override
	public int getAge() { return super.getAge(); }

    @Override
	public String getDrivingLicense() { return super.getDrivingLicense(); }
}
