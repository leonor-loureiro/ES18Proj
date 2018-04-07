package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class Client {

	private final String iban, nif, drivingLicense;
	private final Broker broker; 
	private final int age;
	
	
	public Client(Broker broker, String iban, String nif, String drivingLicense, int age) {
		checkArguments(broker, iban, nif, drivingLicense, age);
		
		this.nif    = nif;
		this.age    = age;
		this.iban   = iban;
		this.broker = broker;
		this.drivingLicense = drivingLicense;
	}


	private void checkArguments(Broker inBroker, String inIBAN, String inNIF, String inDR_L, int inAge) {
		if (inAge < 18 || inAge > 100)
			throw new BrokerException();
		if (inBroker == null)
			throw new BrokerException();
		if (inIBAN == null || inIBAN.trim().isEmpty())
			throw new BrokerException();
		if (inNIF == null  || inNIF.trim().isEmpty())
			throw new BrokerException();
		if (inDR_L == null || inDR_L.trim().isEmpty())
			throw new BrokerException();
	}

	public String getIban() {
		return iban;
	}

	public String getNif() {
		return nif;
	}

	public String getDrivingLicense() {
		return drivingLicense;
	}

	public Broker getBroker() {
		return broker;
	}
	
	public int getAge() {
		return age;
	}
}
