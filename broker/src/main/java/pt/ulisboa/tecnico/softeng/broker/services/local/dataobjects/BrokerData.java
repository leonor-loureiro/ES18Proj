package pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.broker.domain.Adventure;
import pt.ulisboa.tecnico.softeng.broker.domain.Broker;

public class BrokerData {
	private String name;
	private String code;
	private List<AdventureData> adventures = new ArrayList<>();

	public BrokerData() {
	}

	public BrokerData(Broker broker) {
		this.name = broker.getName();
		this.code = broker.getCode();
		for (Adventure adventure : broker.getAdventureSet()) {
			this.adventures.add(new AdventureData(adventure));
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

	public List<AdventureData> getAdventures() {
		return this.adventures;
	}

	public void setAdventures(List<AdventureData> adventures) {
		this.adventures = adventures;
	}

}
