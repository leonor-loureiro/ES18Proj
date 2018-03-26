package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.TaxInterface;

public class Booking {
	private static int counter = 0;

	private static final String type = "HOUSING";
	private final Hotel hotel;
	private final String reference;
	private String cancel;
	private LocalDate cancellationDate;
	private final LocalDate arrival;
	private final LocalDate departure;
	private final double price;
	private final String nif;
	private final String providerNif;	
	private String paymentReference;
	private String invoiceReference;
	private boolean cancelledInvoice = false;
	private String cancelledPaymentReference = null;
	private final String buyerIban;

	public Booking(Hotel hotel, Room.Type type, LocalDate arrival, LocalDate departure, String buyerNIF, String buyerIban) {
		checkArguments(hotel, arrival, departure, buyerNIF, buyerIban);

		this.reference = hotel.getCode() + Integer.toString(++Booking.counter);
		this.hotel = hotel;
		this.arrival = arrival;
		this.departure = departure;
		this.price = hotel.getPrice(type);
		this.nif = buyerNIF;
		this.buyerIban = buyerIban;
		this.providerNif = hotel.getNIF();
	}

	private void checkArguments(Hotel hotel, LocalDate arrival, LocalDate departure, String buyerNIF, String buyerIban) {
		if (hotel == null || arrival == null || departure == null || buyerNIF == null || buyerNIF.trim().length() == 0 || buyerIban == null
				|| buyerIban.trim().length() == 0) {
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
		return this.cancel;
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

	public String getNif() {
		return this.nif;
	}

	public static String getType() {
		return type;
	}

	public String getProviderNif() {
		return this.providerNif;
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

	public boolean isCancelledInvoice() {
		return this.cancelledInvoice;
	}

	public void setCancelledInvoice(boolean cancelledInvoice) {
		this.cancelledInvoice = cancelledInvoice;
	}

	public String getCancelledPaymentReference() {
		return this.cancelledPaymentReference;
	}

	public void setCancelledPaymentReference(String cancelledPaymentReference) {
		this.cancelledPaymentReference = cancelledPaymentReference;
	}

	public String cancel() {
		this.cancel = "CANCEL" + this.reference;
		this.cancellationDate = new LocalDate();

		this.hotel.getProcessor().submitBooking(this);

		return this.cancel;
	}

	public boolean isCancelled() {
		return this.cancel != null;
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

	public String getIban() {
		return this.buyerIban;
	}
}
