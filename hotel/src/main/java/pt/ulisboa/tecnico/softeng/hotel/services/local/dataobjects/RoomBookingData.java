package pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import pt.ulisboa.tecnico.softeng.hotel.domain.Booking;

public class RoomBookingData {
	private String reference;
	private String cancellation;
	private String hotelName;
	private String hotelCode;
	private String roomNumber;
	private String roomType;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate arrival;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate departure;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate cancellationDate;

	public RoomBookingData() {
	}

	public RoomBookingData(Booking booking) {
		this.reference = booking.getReference();
		this.cancellation = booking.getCancellation();
		this.hotelName = booking.getRoom().getHotel().getName();
		this.hotelCode = booking.getRoom().getHotel().getCode();
		this.roomNumber = booking.getRoom().getNumber();
		this.roomType = booking.getRoom().getType().name();
		this.arrival = booking.getArrival();
		this.departure = booking.getDeparture();
		this.cancellationDate = booking.getCancellationDate();
	}

	public String getReference() {
		return this.reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getCancellation() {
		return this.cancellation;
	}

	public void setCancellation(String cancellation) {
		this.cancellation = cancellation;
	}

	public String getHotelName() {
		return this.hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getHotelCode() {
		return this.hotelCode;
	}

	public void setHotelCode(String hotelCode) {
		this.hotelCode = hotelCode;
	}

	public String getRoomNumber() {
		return this.roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getRoomType() {
		return this.roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
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

	public LocalDate getCancellationDate() {
		return this.cancellationDate;
	}

	public void setCancellationDate(LocalDate cancellationDate) {
		this.cancellationDate = cancellationDate;
	}

}
