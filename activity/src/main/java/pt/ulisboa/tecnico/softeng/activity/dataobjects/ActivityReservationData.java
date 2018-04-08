package pt.ulisboa.tecnico.softeng.activity.dataobjects;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.activity.domain.Booking;

public class ActivityReservationData {
	private String reference;
	private String cancellation;
	private String name;
	private String code;
	private LocalDate begin;
	private LocalDate end;
	private LocalDate cancellationDate;
    private double amount;
    private String invoiceReference;
    
	public ActivityReservationData() {
	}

	public ActivityReservationData(ActivityProvider provider, ActivityOffer offer, Booking booking) {
		this.reference = booking.getReference();
		this.cancellation = booking.getCancellation();
		this.name = provider.getName();
		this.code = provider.getCode();
		this.begin = offer.getBegin();
		this.end = offer.getEnd();
		this.cancellationDate = booking.getCancellationDate();
		this.amount = offer.getAmount();
		this.invoiceReference = booking.getInvoiceReference();
	}

	public String getReference() {
		return this.reference;
	}
	
	public String getInvoiceReference() {
		return this.invoiceReference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getCancellation() {
		return this.cancellation;
	}

	public void setCancellation(String cancellation) {
		this.cancellation = cancellation;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public LocalDate getBegin() {
		return this.begin;
	}

	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}

	public LocalDate getEnd() {
		return this.end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public LocalDate getCancellationDate() {
		return this.cancellationDate;
	}

	public void setCancellationDate(LocalDate cancellationDate) {
		this.cancellationDate = cancellationDate;
	}

    public double getAmount() {
        return amount;
    }
}
