package pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.broker.domain.Adventure;
import pt.ulisboa.tecnico.softeng.broker.domain.Broker;
import pt.ulisboa.tecnico.softeng.broker.domain.BulkRoomBooking;
import pt.ulisboa.tecnico.softeng.broker.domain.Client;

public class BrokerData {
	public static enum CopyDepth {
		SHALLOW, BULKS, CLIENTS, ADVENTURES
	};

	private String name;
	private String code;
	private String nifAsSeller;
	private String nifAsBuyer;
	private String iban;
	private List<ClientData> clients = new ArrayList<>();
	private List<AdventureData> adventures = new ArrayList<>();
	private List<BulkData> bulks = new ArrayList<>();

	public BrokerData() {
	}

	public BrokerData(Broker broker, CopyDepth depth) {
		this.name = broker.getName();
		this.code = broker.getCode();

		switch (depth) {
		case CLIENTS:
			for (Client client : broker.getClientSet()) {
				this.getClients().add(new ClientData(client));
			}
			break;
		case ADVENTURES:
			for (Adventure adventure : broker.getAdventureSet()) {
				this.adventures.add(new AdventureData(adventure));
			}
			break;
		case BULKS:
			for (BulkRoomBooking bulkRoomBooking : broker.getRoomBulkBookingSet()) {
				this.bulks.add(new BulkData(bulkRoomBooking));
			}
			break;
		case SHALLOW:
			break;
		default:
			break;
		}

	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNifAsSeller() {
		return this.nifAsSeller;
	}

	public void setNifAsSeller(String nifAsSeller) {
		this.nifAsSeller = nifAsSeller;
	}

	public String getNifAsBuyer() {
		return this.nifAsBuyer;
	}

	public void setNifAsBuyer(String nifAsBuyer) {
		this.nifAsBuyer = nifAsBuyer;
	}

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public List<ClientData> getClients() {
		return this.clients;
	}

	public void setClients(List<ClientData> clients) {
		this.clients = clients;
	}

	public List<AdventureData> getAdventures() {
		return this.adventures;
	}

	public void setAdventures(List<AdventureData> adventures) {
		this.adventures = adventures;
	}

	public List<BulkData> getBulks() {
		return this.bulks;
	}

	public void setBulks(List<BulkData> bulks) {
		this.bulks = bulks;
	}

}
