package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class Client extends Client_Base {

	public Client(Broker broker, String IBAN, String NIF, String drivingLicense, int age) {
		checkArguments(broker, IBAN, NIF, drivingLicense, age);
		setIban(IBAN);
		setNif(NIF);
		setDrivingLicense(drivingLicense);
        setAge(age);

		broker.addClient(this);
	}

    public void delete() {
        setBroker(null);

        for (Adventure adventure: getAdventureSet()) {
        	adventure.delete();
		}

        deleteDomainObject();
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

	public String getIBAN() {
		return getIban();
	}

	public String getNIF() {
		return getNif();
	}
}
