package pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects;

import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import pt.ulisboa.tecnico.softeng.broker.domain.BulkRoomBooking;

public class BulkData {
	private Integer number;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate arrival;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate departure;
	private int actualNumber;
	private boolean cancelled;
	private List<String> references;
	private String nif;
	private String iban;


	public BulkData() {
	}

	public BulkData(BulkRoomBooking bulkRoomBooking) {
		this.number = bulkRoomBooking.getNumber();
		this.arrival = bulkRoomBooking.getArrival();
		this.departure = bulkRoomBooking.getDeparture();
		this.actualNumber = bulkRoomBooking.getReferenceSet().size();
		this.cancelled = bulkRoomBooking.getCancelled();
		this.nif = bulkRoomBooking.getBuyerNif();
		this.iban = bulkRoomBooking.getBuyerIban();

		this.references = bulkRoomBooking.getReferenceSet().stream().map(r -> r.getValue())
				.collect(Collectors.toList());

		//TODO delete dummy
		this.references.add("dummyRef1");
		this.references.add("dummyRef2");
	}

	public Integer getNumber() {
		return this.number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public LocalDate getArrival() {
		return this.arrival;
	}

	public void setArrival(LocalDate arrival) {
		this.arrival = arrival;
	}

	public LocalDate getDeparture() {
		return this.departure;
	}

	public void setDeparture(LocalDate departure) {
		this.departure = departure;
	}

	public int getActualNumber() {
		return this.actualNumber;
	}

	public void setActualNumber(int actualNumber) {
		this.actualNumber = actualNumber;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public List<String> getReferences() {
		return this.references;
	}

	public void setReferences(List<String> references) {
		this.references = references;
	}
	
	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}


}
