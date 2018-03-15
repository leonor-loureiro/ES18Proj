package pt.ulisboa.tecnico.softeng.hotel.dataobjects;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.domain.Booking;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;

public class RoomBookingData {
	private String reference;
	private String cancellation;
	private String hotelName;
	private String hotelCode;
	private String roomNumber;
	private String roomType;
	private LocalDate arrival;
	private LocalDate departure;
	private LocalDate cancellationDate;
	private double price;

	public RoomBookingData() {
	}

	public RoomBookingData(Room room, Booking booking) {
		this.reference = booking.getReference();
		this.cancellation = booking.getCancellation();
		this.hotelName = room.getHotel().getName();
		this.hotelCode = room.getHotel().getCode();
		this.roomNumber = room.getNumber();
		this.roomType = room.getType().name();
		this.arrival = booking.getArrival();
		this.departure = booking.getDeparture();
		this.cancellationDate = booking.getCancellationDate();
		this.price = booking.getPrice();
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
