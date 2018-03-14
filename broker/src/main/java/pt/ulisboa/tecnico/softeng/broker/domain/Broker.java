package pt.ulisboa.tecnico.softeng.broker.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class Broker {
	private static Logger logger = LoggerFactory.getLogger(Broker.class);

	public static Set<Broker> brokers = new HashSet<>();

	private final String code;
	private final String name;
	private final String NIF;
	private final String IBAN;
	private final Set<Client> clients = new HashSet<>();
	private final Set<Adventure> adventures = new HashSet<>();
	private final Set<BulkRoomBooking> bulkBookings = new HashSet<>();

	public Broker(String code, String name, String NIF, String IBAN) {
		checkArguments(code, name, NIF, IBAN);
		this.code = code;
		this.name = name;
		this.NIF = NIF;
		this.IBAN = IBAN;

		Broker.brokers.add(this);
	}

	private void checkArguments(String code, String name, String NIF, String IBAN) {
		if (code == null || code.trim().length() == 0 || name == null || name.trim().length() == 0 || NIF == null
				|| NIF.trim().length() == 0 || IBAN == null || IBAN.trim().length() == 0) {
			throw new BrokerException();
		}

		for (Broker broker : Broker.brokers) {
			if (broker.getCode().equals(code)) {
				throw new BrokerException();
			}
		}

		for (Broker broker : Broker.brokers) {
			if (broker.getNIF().equals(NIF)) {
				throw new BrokerException();
			}
		}

	}

	String getCode() {
		return this.code;
	}

	String getName() {
		return this.name;
	}

	public String getNIF() {
		return this.NIF;
	}

	public String getIBAN() {
		return this.IBAN;
	}

	public int getNumberOfAdventures() {
		return this.adventures.size();
	}

	public void addAdventure(Adventure adventure) {
		this.adventures.add(adventure);
	}

	public Client getClientByNIF(String NIF) {
		for (Client client : this.clients) {
			if (client.getNIF().equals(NIF)) {
				return client;
			}
		}
		return null;
	}

	public void addClient(Client client) {
		this.clients.add(client);
	}

	public boolean hasAdventure(Adventure adventure) {
		return this.adventures.contains(adventure);
	}

	public void bulkBooking(int number, LocalDate arrival, LocalDate departure) {
		BulkRoomBooking bulkBooking = new BulkRoomBooking(number, arrival, departure);
		this.bulkBookings.add(bulkBooking);
		bulkBooking.processBooking();
	}

}
