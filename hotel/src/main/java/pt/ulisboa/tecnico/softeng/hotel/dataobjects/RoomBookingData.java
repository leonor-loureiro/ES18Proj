package pt.ulisboa.tecnico.softeng.hotel.dataobjects;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.domain.Booking;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;

public class RoomBookingData {
	private final String reference;
	private final String cancellation;
	private final String hotelName;
	private final String hotelCode;
	private final String roomNumber;
	private final String roomType;
	private final LocalDate arrival;
	private final LocalDate departure;
	private final LocalDate cancellationDate;
	private final double price;
	private final String paymentReference;
	private final String invoiceReference;

	public RoomBookingData(String roomType) {
		this.reference = null;
		this.cancellation = null;
		this.hotelName = null;
		this.hotelCode = null;
		this.roomNumber = null;
		this.roomType = roomType;
		this.arrival = null;
		this.departure = null;
		this.cancellationDate = null;
		this.price = 0;
		this.paymentReference = null;
		this.invoiceReference = null;

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
		this.paymentReference = booking.getPaymentReference();
		this.invoiceReference = booking.getInvoiceReference();
	}

	public String getReference() {
		return this.reference;
	}

	public String getCancellation() {
		return this.cancellation;
	}

	public String getHotelName() {
		return this.hotelName;
	}

	public String getHotelCode() {
		return this.hotelCode;
	}

	public String getRoomNumber() {
		return this.roomNumber;
	}

	public String getRoomType() {
		return this.roomType;
	}

	public LocalDate getArrival() {
		return this.arrival;
	}

	public LocalDate getDeparture() {
		return this.departure;
	}

	public LocalDate getCancellationDate() {
		return this.cancellationDate;
	}

	public double getPrice() {
		return this.price;
	}

	public String getPaymentReference() {
		return this.paymentReference;
	}

	public String getInvoiceReference() {
		return this.invoiceReference;
	}

}
