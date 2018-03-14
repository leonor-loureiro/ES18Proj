package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.activity.interfaces.TaxInterface;

public class Booking {
	private static int counter = 0;

	private final String reference;
	private final String nif;
	private final String iban;
	private final int amount;
	private final String paymentReference;
	private final String invoiceReference;
	private String cancel;
	private LocalDate cancellationDate;

	public Booking(ActivityProvider provider, ActivityOffer offer, String buyerNif, String buyerIban) {
		checkArguments(provider, offer, buyerNif, buyerIban);

		this.reference = provider.getCode() + Integer.toString(++Booking.counter);
		this.nif = buyerNif;
		this.iban = buyerIban;
		this.amount = offer.getAmount();

		this.paymentReference = BankInterface.processPayment(this.nif, this.amount);

		InvoiceData invoiceData = new InvoiceData(provider.getNif(), this.nif, "SPORT", this.amount, offer.getBegin());
		this.invoiceReference = TaxInterface.submitInvoice(invoiceData);

		offer.addBooking(this);
	}

	private void checkArguments(ActivityProvider provider, ActivityOffer offer, String buyerNIF, String buyerIban) {
		if (provider == null || offer == null || buyerNIF == null || buyerNIF.trim().length() == 0 || buyerIban == null
				|| buyerIban.trim().length() == 0) {
			throw new ActivityException();
		}
	}

	public String getReference() {
		return this.reference;
	}

	public String getNif() {
		return this.nif;
	}

	public String getIban() {
		return this.iban;
	}

	public int getAmount() {
		return this.amount;
	}

	public String getPaymentReference() {
		return this.paymentReference;
	}

	public String getInvoiceReference() {
		return this.invoiceReference;
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
