package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.domain.Booking;

public class ActivityBookingData {
	private String reference;
	private String nif;
	private String iban;
	private String cancellation;
	private String name;
	private String code;
	private LocalDate begin;
	private LocalDate end;
	private LocalDate cancellationDate;
	private Double price;
	private String paymentReference;
	private String invoiceReference;

	public ActivityBookingData() {
	}

	public ActivityBookingData(Booking booking) {
		this.reference = booking.getReference();
		this.cancellation = booking.getCancel();
		this.name = booking.getActivityOffer().getActivity().getActivityProvider().getName();
		this.code = booking.getActivityOffer().getActivity().getActivityProvider().getCode();
		this.nif = booking.getBuyerNif();
		this.iban = booking.getIban();
		this.begin = booking.getActivityOffer().getBegin();
		this.end = booking.getActivityOffer().getEnd();
		this.cancellationDate = booking.getCancellationDate();
		this.price = booking.getAmount();
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

	public String getNif() {
		return this.nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
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
