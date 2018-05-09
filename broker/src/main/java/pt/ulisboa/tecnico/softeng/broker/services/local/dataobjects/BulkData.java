package pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects;

import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import pt.ulisboa.tecnico.softeng.broker.domain.BulkRoomBooking;

public class BulkData {
	private String id;
	private Integer number;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate arrival;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate departure;
	private String buyerNif;
	private String buyerIban;
	private int actualNumber;
	private boolean cancelled;
	private List<String> references;

	public BulkData() {
	}

	public BulkData(BulkRoomBooking bulkRoomBooking) {
		this.id = bulkRoomBooking.getId();
		this.number = bulkRoomBooking.getNumber();
		this.arrival = bulkRoomBooking.getArrival();
		this.departure = bulkRoomBooking.getDeparture();
		this.buyerNif = bulkRoomBooking.getBuyerNif();
		this.buyerIban = bulkRoomBooking.getBuyerIban();
		this.actualNumber = bulkRoomBooking.getReferenceSet().size();
		this.cancelled = bulkRoomBooking.getCancelled();

		this.references = bulkRoomBooking.getReferenceSet().stream().map(r -> r.getValue())
				.collect(Collectors.toList());
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getBuyerNif() {
		return this.buyerNif;
	}

	public void setBuyerNif(String buyerNif) {
		this.buyerNif = buyerNif;
	}

	public String getBuyerIban() {
		return this.buyerIban;
	}

	public void setBuyerIban(String buyerIban) {
		this.buyerIban = buyerIban;
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

}
