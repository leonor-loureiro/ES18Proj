package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.interfaces.TaxInterface;

public class Booking {
	private static int counter = 0;

	private final String reference;
	private String cancel;
	private LocalDate cancellationDate;

	public Booking(ActivityProvider provider, ActivityOffer offer, String buyerNIF) {
		checkArguments(provider, offer, buyerNIF);

		this.reference = provider.getCode() + Integer.toString(++Booking.counter);

		offer.addBooking(this);

		InvoiceData invoiceData = new InvoiceData(provider.getNif(), buyerNIF, "SPORT", offer.getAmount(),
				offer.getBegin());
		TaxInterface.submitInvoice(invoiceData);
	}

	private void checkArguments(ActivityProvider provider, ActivityOffer offer, String buyerNIF) {
		if (provider == null || offer == null || buyerNIF == null || buyerNIF.trim().length() == 0) {
			throw new ActivityException();
		}
	}

	public String getReference() {
		return this.reference;
	}

	public String getCancellation() {
		return this.cancel;
	}

	public LocalDate getCancellationDate() {
		return this.cancellationDate;
	}

	public String cancel() {
		this.cancel = "CANCEL" + this.reference;
		this.cancellationDate = new LocalDate();
		return this.cancel;
	}

	public boolean isCancelled() {
		return this.cancel != null;
	}
}
