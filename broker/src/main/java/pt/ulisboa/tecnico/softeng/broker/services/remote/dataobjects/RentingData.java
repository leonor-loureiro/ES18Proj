package pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public class RentingData {
	private String reference;
	private String plate;
	private String drivingLicense;
	private String rentACarCode;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate begin;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate end;
	private String paymentReference;
	private String invoiceReference;
	private double price;
	private String nif;
	private String iban;
	private int kilometers;
	private String cancel;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate cancelDate;

	/**
	 * @return the renting reference
	 */
	public String getReference() {
		return this.reference;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public int getKilometers() {
		return kilometers;
	}

	public void setKilometers(int kilometers) {
		this.kilometers = kilometers;
	}

	public String getCancel() {
		return cancel;
	}

	public void setCancel(String cancel) {
		this.cancel = cancel;
	}

	public LocalDate getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}

	public void setRentACarCode(String rentACarCode) {
		this.rentACarCode = rentACarCode;
	}

	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}

	public void setInvoiceReference(String invoiceReference) {
		this.invoiceReference = invoiceReference;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @return the vehicle plate
	 */
	public String getPlate() {
		return this.plate;
	}

	/**
	 * @return the drivingLicense
	 */
	public String getDrivingLicense() {
		return this.drivingLicense;
	}

	/**
	 * @return the rentACarCode
	 */
	public String getRentACarCode() {
		return this.rentACarCode;
	}

	/**
	 * @return the begin
	 */
	public LocalDate getBegin() {
		return this.begin;
	}

	/**
	 * @return the end
	 */
	public LocalDate getEnd() {
		return this.end;
	}

	public String getPaymentReference() {
		return this.paymentReference;
	}

	public String getInvoiceReference() {
		return this.invoiceReference;
	}

	public double getPrice() {
		return this.price;
	}

}
