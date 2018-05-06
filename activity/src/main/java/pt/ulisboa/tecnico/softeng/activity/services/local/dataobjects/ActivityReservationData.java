package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateSerializer;

import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.activity.domain.Booking;

public class ActivityReservationData {  
  private String reference;
	private String cancellation;
	private String name;
	private String code;
	
	@JsonSerialize(using = LocalDateSerializer.class)  
	private LocalDate begin;
	
	@JsonSerialize(using = LocalDateSerializer.class)  
	private LocalDate end;
	
	@JsonSerialize(using = LocalDateSerializer.class)  
	private LocalDate cancellationDate;
	private double price;
	private String paymentReference;
	private String invoiceReference;

  
  
  

	public ActivityReservationData(ActivityProvider provider, ActivityOffer offer, Booking booking) {
		this.reference = booking.getReference();
		this.cancellation = booking.getCancel();
		this.name = provider.getName();
		this.code = provider.getCode();
		this.begin = offer.getBegin();
		this.end = offer.getEnd();
		this.cancellationDate = booking.getCancellationDate();
		this.price = offer.getPrice();
		this.paymentReference = booking.getPaymentReference();
		this.invoiceReference = booking.getInvoiceReference();
	}

	public ActivityReservationData(Booking booking) {
		this.reference = booking.getReference();
		this.cancellation = booking.getCancel();
		this.name = booking.getActivityOffer().getActivity().getActivityProvider().getName();
		this.code = booking.getActivityOffer().getActivity().getActivityProvider().getCode();
		this.begin = booking.getActivityOffer().getBegin();
		this.end = booking.getActivityOffer().getEnd();
		this.cancellationDate = booking.getCancellationDate();
		this.price = booking.getAmount();
		this.paymentReference = booking.getPaymentReference();
		this.invoiceReference = booking.getInvoiceReference();
	}

	public ActivityReservationData() {}
	
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
