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
	private final String nifAsSeller;
	private final String nifAsBuyer;
	private final String iban;
	private final Set<Client> clients = new HashSet<>();
	private final Set<Adventure> adventures = new HashSet<>();
	private final Set<BulkRoomBooking> bulkBookings = new HashSet<>();

	public Broker(String code, String name, String nifAsSeller, String nifAsBuyer, String iban) {
		checkArguments(code, name, nifAsSeller, nifAsBuyer, iban);
		this.code = code;
		this.name = name;
		this.nifAsSeller = nifAsSeller;
		this.nifAsBuyer = nifAsBuyer;
		this.iban = iban;

		Broker.brokers.add(this);
	}

	private void checkArguments(String code, String name, String nifAsSeller, String nifAsBuyer, String iban) {
		if (code == null || code.trim().length() == 0 || name == null || name.trim().length() == 0
				|| nifAsSeller == null || nifAsSeller.trim().length() == 0 || nifAsBuyer == null
				|| nifAsBuyer.trim().length() == 0 || iban == null || iban.trim().length() == 0) {
			throw new BrokerException();
		}

		if (nifAsSeller.equals(nifAsBuyer)) {
			throw new BrokerException();
		}

		for (Broker broker : Broker.brokers) {
			if (broker.getCode().equals(code)) {
				throw new BrokerException();
			}
		}

		for (Broker broker : Broker.brokers) {
			if (broker.getNifAsSeller().equals(nifAsSeller) || broker.getNifAsSeller().equals(nifAsBuyer)
					|| broker.getNifAsBuyer().equals(nifAsSeller) || broker.getNifAsBuyer().equals(nifAsBuyer)) {
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

	public String getNifAsSeller() {
		return this.nifAsSeller;
	}

	public String getNifAsBuyer() {
		return this.nifAsBuyer;
	}

	public String getIBAN() {
		return this.iban;
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
