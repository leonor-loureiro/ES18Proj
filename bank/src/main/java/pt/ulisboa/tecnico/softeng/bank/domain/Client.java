package pt.ulisboa.tecnico.softeng.bank.domain;

public class Client {
	private static int counter = 0;

	private final String name;
	private final String ID;

	public Client(Bank bank, String name) {
		this.ID = Integer.toString(++Client.counter);
		this.name = name;

		bank.addClient(this);
	}

	public String getName() {
		return this.name;
	}

	public String getID() {
		return this.ID;
	}

}
