package pt.ulisboa.tecnico.softeng.activity.dataobjects;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.activity.domain.Booking;

public class ActivityReservationData {
	private final String reference;
	private final String cancellation;
	private final String name;
	private final String code;
	private final LocalDate begin;
	private final LocalDate end;
	private final LocalDate cancellationDate;
	private final double amount;
	private final String paymentReference;
	private final String invoiceReference;

	public ActivityReservationData(ActivityProvider provider, ActivityOffer offer, Booking booking) {
		this.reference = booking.getReference();
		this.cancellation = booking.getCancellation();
		this.name = provider.getName();
		this.code = provider.getCode();
		this.begin = offer.getBegin();
		this.end = offer.getEnd();
		this.cancellationDate = booking.getCancellationDate();
		this.amount = offer.getAmount();
		this.paymentReference = booking.getPaymentReference();
		this.invoiceReference = booking.getInvoiceReference();
	}

	public String getReference() {
		return this.reference;
	}

	public String getCancellation() {
		return this.cancellation;
	}

	public String getName() {
		return this.name;
	}

	public String getCode() {
		return this.code;
	}

	public LocalDate getBegin() {
		return this.begin;
	}

	public LocalDate getEnd() {
		return this.end;
	}

	public LocalDate getCancellationDate() {
		return this.cancellationDate;
	}

	public double getAmount() {
		return this.amount;
	}

	public String getPaymentReference() {
		return this.paymentReference;
	}

	public String getInvoiceReference() {
		return this.invoiceReference;
	}

}
