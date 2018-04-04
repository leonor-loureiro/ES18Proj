package pt.ulisboa.tecnico.softeng.broker.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class Broker extends Broker_Base {
	private static Logger logger = LoggerFactory.getLogger(Broker.class);

	private final String nifAsSeller;
	private final String nifAsBuyer;
	private final String iban;
	private final Set<Client> clients = new HashSet<>();

	@Override
	public int getCounter() {
		int counter = super.getCounter() + 1;
		setCounter(counter);
		return counter;
	}

	public Broker(String code, String name, String nifAsSeller, String nifAsBuyer, String iban) {
		checkArguments(code, name, nifAsSeller, nifAsBuyer, iban);

		setCode(code);
		setName(name);

		this.nifAsSeller = nifAsSeller;
		this.nifAsBuyer = nifAsBuyer;
		this.iban = iban;

		FenixFramework.getDomainRoot().addBroker(this);
	}

	public void delete() {
		setRoot(null);

		for (Adventure adventure : getAdventureSet()) {
			adventure.delete();
		}

		for (BulkRoomBooking bulkRoomBooking : getRoomBulkBookingSet()) {
			bulkRoomBooking.delete();
		}

		deleteDomainObject();
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

		for (Broker broker : FenixFramework.getDomainRoot().getBrokerSet()) {
			if (broker.getCode().equals(code)) {
				throw new BrokerException();
			}
		}

		for (Broker broker : FenixFramework.getDomainRoot().getBrokerSet()) {
			if (broker.getNifAsSeller().equals(nifAsSeller) || broker.getNifAsSeller().equals(nifAsBuyer)
					|| broker.getNifAsBuyer().equals(nifAsSeller) || broker.getNifAsBuyer().equals(nifAsBuyer)) {
				throw new BrokerException();
			}
		}

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

	public Client getClientByNIF(String NIF) {
		for (Client client : this.clients) {
			if (client.getNIF().equals(NIF)) {
				return client;
			}
		}
		return null;
	}

	public boolean drivingLicenseIsRegistered(String drivingLicense) {
		return this.clients.stream().anyMatch(client -> client.getDrivingLicense().equals(drivingLicense));
	}

	public void addClient(Client client) {
		this.clients.add(client);
	}

	public void bulkBooking(int number, LocalDate arrival, LocalDate departure) {
		BulkRoomBooking bulkBooking = new BulkRoomBooking(this, number, arrival, departure, this.nifAsBuyer, this.iban);
		bulkBooking.processBooking();
	}
}
