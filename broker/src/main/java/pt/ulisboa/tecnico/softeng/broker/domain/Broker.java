package pt.ulisboa.tecnico.softeng.broker.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class Broker {
	private static Logger logger = LoggerFactory.getLogger(Broker.class);

	public static final Set<Broker> brokers    = new HashSet<>();

	private final String code;
	private final String name;
	private final Set<Adventure> adventures = new HashSet<>();
	private final Set<BulkRoomBooking> bulkBookings = new HashSet<>();
	
	private final String nifSeller;
	private final String nifBuyer;
	private final String iban;
	
	public Broker(String code, String name, String nifSeller, String nifBuyer, String iban) {
		checkArguments(code, name, nifSeller, nifBuyer, iban);
		this.code = code;
				
		this.nifSeller = nifSeller;
		this.nifBuyer  = nifBuyer;
		this.iban      = iban;
		this.name      = name;

		Broker.brokers.add(this);
	}

	private void checkArguments(String code, String name, String nifSeller, String nifBuyer, String iban) {
		checkStringValue(code);
		checkStringValue(name);
		checkStringValue(nifSeller);
		checkStringValue(nifBuyer);
		checkStringValue(iban);
		
		if (Broker.brokers.stream().anyMatch(br -> (br.getNIFSeller().equals(nifSeller) ||
													br.getNIFBuyer().equals(nifBuyer) ||
													br.getIBAN().equals(iban)   ||
													br.getCode().equals(code)
													)))
			throw new BrokerException();
	}

	private void checkStringValue(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new BrokerException();
		}
	}

	String getCode() {
		return this.code;
	}

	String getName() {
		return this.name;
	}

	public String getNIFSeller() {
		return nifSeller;
	}

	public String getNIFBuyer() {
		return nifBuyer;
	}

	public String getIBAN() {
		return iban;
	}

	public int getNumberOfAdventures() {
		return this.adventures.size();
	}

	public void addAdventure(Adventure adventure) {
		this.adventures.add(adventure);
	}

	public boolean hasAdventure(Adventure adventure) {
		return this.adventures.contains(adventure);
	}

	public void bulkBooking(int number, LocalDate arrival, LocalDate departure) {
		BulkRoomBooking bulkBooking = new BulkRoomBooking(number, arrival, departure, this.getNIFBuyer(), this.getIBAN());
		this.bulkBookings.add(bulkBooking);
		bulkBooking.processBooking();
	}
}
