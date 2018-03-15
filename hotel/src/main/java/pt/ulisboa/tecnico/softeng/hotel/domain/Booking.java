package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.TaxInterface;

public class Booking {
	private static int counter = 0;

	private final String reference;
	private String cancellation;
	private LocalDate cancellationDate;
	private final LocalDate arrival;
	private final LocalDate departure;
	private final double price;

	private String invoiceReference;

	Booking(Hotel hotel, Room.Type type, LocalDate arrival, LocalDate departure, String buyerNIF) {
		checkArguments(hotel, arrival, departure);

		this.reference = hotel.getCode() + Integer.toString(++Booking.counter);
		this.arrival = arrival;
		this.departure = departure;
		this.price = hotel.getPrice(type);

		InvoiceData invoiceData = new InvoiceData(hotel.getNIF(), buyerNIF, "Room", price, arrival);
		this.invoiceReference = TaxInterface.submitInvoice(invoiceData);
	}

	private void checkArguments(Hotel hotel, LocalDate arrival, LocalDate departure) {
		if (hotel == null || arrival == null || departure == null) {
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

	public double getPrice() {
		return this.price;
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
		
		TaxInterface.cancelInvoice(this.invoiceReference);
		
		return this.cancellation;
	}

	public boolean isCancelled() {
		return this.cancellation != null;
	}

}
