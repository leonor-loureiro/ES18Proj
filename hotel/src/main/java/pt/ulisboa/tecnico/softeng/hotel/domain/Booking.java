package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Booking extends Booking_Base {
	private static final String HOUSING_TYPE = "HOUSING";
	private final double price;
	private final String nif;
	private final String providerNif;
	private String paymentReference;
	private String invoiceReference;
	private boolean cancelledInvoice = false;
	private String cancelledPaymentReference = null;
	private final String buyerIban;

	public Booking(Room room, LocalDate arrival, LocalDate departure, String buyerNIF, String buyerIban) {
		checkArguments(room, arrival, departure, buyerNIF, buyerIban);

		setReference(room.getHotel().getCode() + Integer.toString(room.getHotel().getCounter()));
		setArrival(arrival);
		setDeparture(departure);

		this.price = room.getHotel().getPrice(room.getType()) * Days.daysBetween(arrival, departure).getDays();
		this.nif = buyerNIF;
		this.buyerIban = buyerIban;
		this.providerNif = room.getHotel().getNIF();

		setRoom(room);
	}

	public void delete() {
		setRoom(null);

		deleteDomainObject();
	}

	private void checkArguments(Room room, LocalDate arrival, LocalDate departure, String buyerNIF, String buyerIban) {
		if (room == null || arrival == null || departure == null || buyerNIF == null || buyerNIF.trim().length() == 0
				|| buyerIban == null || buyerIban.trim().length() == 0) {
			throw new HotelException();
		}

		if (departure.isBefore(arrival)) {
			throw new HotelException();
		}
	}

	public double getPrice() {
		return this.price;
	}

	public String getNif() {
		return this.nif;
	}

	public static String getType() {
		return HOUSING_TYPE;
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

		if ((arrival.equals(getArrival()) || arrival.isAfter(getArrival())) && arrival.isBefore(getDeparture())) {
			return true;
		}

		if ((departure.equals(getDeparture()) || departure.isBefore(getDeparture()))
				&& departure.isAfter(getArrival())) {
			return true;
		}

		if (arrival.isBefore(getArrival()) && departure.isAfter(getDeparture())) {
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
		setCancellation(getReference() + "CANCEL");
		setCancellationDate(new LocalDate());

		getRoom().getHotel().getProcessor().submitBooking(this);

		return getCancellation();
	}

	public boolean isCancelled() {
		return getCancellation() != null;
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
