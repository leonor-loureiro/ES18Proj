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

	public BulkData() {
	}

	public BulkData(BulkRoomBooking bulkRoomBooking) {
		this.number = bulkRoomBooking.getNumber();
		this.arrival = bulkRoomBooking.getArrival();
		this.departure = bulkRoomBooking.getDeparture();
		this.actualNumber = bulkRoomBooking.getReferenceSet().size();
		this.cancelled = bulkRoomBooking.getCancelled();

		this.references = bulkRoomBooking.getReferenceSet().stream().map(r -> r.getValue())
				.collect(Collectors.toList());

		this.references.add("fjhkdsl");
		this.references.add("jkfhglkfdsg");
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

}
