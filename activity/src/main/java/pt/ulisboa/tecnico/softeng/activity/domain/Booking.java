package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.interfaces.BankInterface;

public class Booking {
	private static int counter = 0;

	private static final String type = "SPORT";
	private final String reference;
	private final String providerNif;
	private final String nif;
	private final String iban;
	private final double amount;
	private final LocalDate date;
	private final String paymentReference;
	private String invoiceReference;
	private String cancel;
	private LocalDate cancellationDate;

	public Booking(ActivityProvider provider, ActivityOffer offer, String buyerNif, String buyerIban) {
		checkArguments(provider, offer, buyerNif, buyerIban);

		this.reference = provider.getCode() + Integer.toString(++Booking.counter);
		this.providerNif = provider.getNif();
		this.nif = buyerNif;
		this.iban = buyerIban;
		this.amount = offer.getAmount();
		this.date = offer.getBegin();

		this.paymentReference = BankInterface.processPayment(this.nif, this.amount);

		InvoiceProcessor.getInvoiceProcessor().submitInvoice(this);

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

	public String getType() {
		return this.type;
	}

	public String getProviderNif() {
		return this.providerNif;
	}

	public String getNif() {
		return this.nif;
	}

	public String getIban() {
		return this.iban;
	}

	public double getAmount() {
		return this.amount;
	}

	public LocalDate getDate() {
		return this.date;
	}

	public String getPaymentReference() {
		return this.paymentReference;
	}

	public String getInvoiceReference() {
		return this.invoiceReference;
	}

	public void setInvoiceReference(String invoiceReference) {
		this.invoiceReference = invoiceReference;
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
