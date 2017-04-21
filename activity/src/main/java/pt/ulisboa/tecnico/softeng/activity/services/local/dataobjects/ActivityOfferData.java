package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;

public class ActivityOfferData {
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate begin;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate end;
	private Integer capacity;
	private List<ActivityReservationData> reservations;

	public ActivityOfferData() {
	}

	public ActivityOfferData(ActivityOffer offer) {
		this.begin = offer.getBegin();
		this.end = offer.getEnd();
		this.capacity = offer.getCapacity();
		this.reservations = offer.getBookingSet().stream().map(b -> new ActivityReservationData(b))
				.collect(Collectors.toList());
	}

	public LocalDate getBegin() {
		return this.begin;
	}

	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}

	public LocalDate getEnd() {
		return this.end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public Integer getCapacity() {
		return this.capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public List<ActivityReservationData> getReservations() {
		return this.reservations;
	}

	public void setReservations(List<ActivityReservationData> reservations) {
		this.reservations = reservations;
	}

}
