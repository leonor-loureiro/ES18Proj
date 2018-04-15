package pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects;

public class ClientData {
	private String iban;
	private String nif;
	private String drivingLicense;
	private int age;

	public ClientData() {
	}

	public ClientData(String iban, String nif, String drivingLicense, int age) {
		this.iban = iban;
		this.nif = nif;
		this.drivingLicense = drivingLicense;
		this.age = age;
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

	public int getAge() {
		return this.age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}
