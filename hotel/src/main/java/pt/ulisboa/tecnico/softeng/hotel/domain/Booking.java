package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Booking {
	private static int counter = 0;

	private final String reference;
	private String cancellation;
	private LocalDate cancellationDate;
	private final LocalDate arrival;
	private final LocalDate departure;

	private String paymentReference;
	private String invoiceReference;
	private boolean cancelledInvoice = false;
	private String cancelledPaymentReference = null;

	private final Hotel hotel;
	private final String clientNIF;
	private final String clientIBAN;
	private final String hotelNif;
	private final double amount;
	private static final String type = "ROOM";
	
	
	Booking(Hotel hotel, LocalDate arrival, LocalDate departure, String clientNIF, String clientIBAN) {
		checkArguments(hotel, arrival, departure, clientNIF, clientIBAN);

		this.reference = hotel.getCode() + Integer.toString(++Booking.counter);
		this.hotel = hotel;
		this.arrival = arrival;
		this.departure = departure;
		this.hotelNif = hotel.getNIF();
		this.clientNIF = clientNIF;
		this.clientIBAN = clientIBAN;
		this.amount = hotel.getSinglePrice(); 
	}

	private void checkArguments(Hotel hotel, LocalDate arrival, LocalDate departure, String NIF, String IBAN) {
		if (hotel == null || arrival == null || departure == null) {
			throw new HotelException();
		}

		if (NIF == null || NIF.length() != 9) {
			throw new HotelException();
		}

		if (IBAN == null || IBAN.length() < 5) {
			throw new HotelException();
		}
		
		if (departure.isBefore(arrival)) {
			throw new HotelException();
		}
	}

	public String getReference() {
		return this.reference;
	}

	public String getCancellation() {
		return this.cancellation;
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

	boolean conflict(LocalDate arrival, LocalDate departure) {
		if (isCancelled()) {
			return false;
		}

		if (arrival.equals(departure)) {
			return true;
		}

		if (departure.isBefore(arrival)) {
			throw new HotelException();
		}

		if ((arrival.equals(this.arrival) || arrival.isAfter(this.arrival)) && arrival.isBefore(this.departure)) {
			return true;
		}

		if ((departure.equals(this.departure) || departure.isBefore(this.departure))
				&& departure.isAfter(this.arrival)) {
			return true;
		}

		if ((arrival.isBefore(this.arrival) && departure.isAfter(this.departure))) {
			return true;
		}

		return false;
	}

	public String cancel() {
		this.cancellation = this.reference + "CANCEL";
		this.cancellationDate = new LocalDate();
		
		this.hotel.getProcessor().submitBooking(this);
		
		return this.cancellation;
	}

	public boolean isCancelled() {
		return this.cancellation != null;
	}

	
	public String getClientNIF() {
		return this.clientNIF;
	}

	public String getClientIBAN() {
		return this.clientIBAN;
	}

	public double getAmount() {
		return this.amount;
	}
	
	public String getPaymentReference() {
		return this.paymentReference;
	}
	
	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}
	
	public String getInvoiceReference() {
		return this.invoiceReference;
	}
	
	public void setInvoiceReference(String invoiceReference) {
		this.invoiceReference = invoiceReference;
	}
	
	public String getHotelNif() {
		return this.hotelNif;
	}

	
	public String getType() {
		return this.type;
	}
	
	public String getCancelledPaymentReference() {
		return this.cancelledPaymentReference;
	}

	public void setCancelledPaymentReference(String cancelledPaymentReference) {
		this.cancelledPaymentReference = cancelledPaymentReference;
	}
	
	public boolean isCancelledInvoice() {
		return this.cancelledInvoice;
	}

	public void setCancelledInvoice(boolean cancelledInvoice) {
		this.cancelledInvoice = cancelledInvoice;
	}

}
