package pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.broker.domain.Client;

public class ClientData {
	private String iban;
	private String nif;
	private String drivingLicense;
	private Integer age;

	public ClientData() {
	}

	public ClientData(Client client) {
		this.iban = client.getIban();
		this.nif = client.getNif();
		this.drivingLicense = client.getDrivingLicense();
		this.age = client.getAge();
	}

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getNif() {
		return this.nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getDrivingLicense() {
		return this.drivingLicense;
	}

	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}

	public Integer getAge() {
		return this.age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
}
