package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class Renting {
	private static String drivingLicenseFormat = "^[a-zA-Z]+\\d+$";
	private static int counter;

	private final String reference;
	private final String drivingLicense;
	private final LocalDate begin;
	private final LocalDate end;
	private int kilometers = -1;
	private final Vehicle vehicle;

	private String cancellation;
	private LocalDate cancellationDate;
	private String paymentReference;
	private String invoiceReference;
	private boolean cancelledInvoice = false;
	private String cancelledPaymentReference = null;

	private final String clientNIF;
	private final String clientIBAN;
	private final RentACar rentACar;
	private final String rentACarNif;
	private final double amount;
	private static final String type = "VEHICLE";

	public Renting(String drivingLicense, LocalDate begin, LocalDate end, Vehicle vehicle, String clientNIF, String clientIBAN) {
		checkArguments(drivingLicense, begin, end, vehicle, clientNIF, clientIBAN);
		this.reference = Integer.toString(++Renting.counter);
		this.drivingLicense = drivingLicense;
		this.begin = begin;
		this.end = end;

		this.vehicle = vehicle;
		this.clientNIF = clientNIF;
		this.clientIBAN = clientIBAN;
		this.rentACar = vehicle.getRentACar();
		this.rentACarNif = vehicle.getRentACar().getNIF();
		this.amount = vehicle.getPrice();
	}

	private void checkArguments(String drivingLicense, LocalDate begin, LocalDate end, Vehicle vehicle, String NIF, String IBAN) {
		if (drivingLicense == null || !drivingLicense.matches(drivingLicenseFormat) || begin == null || end == null || vehicle == null
				|| end.isBefore(begin))
			throw new CarException();
		
		if (NIF == null || NIF.length() != 9) {
			throw new CarException();
		}

		if (IBAN == null || IBAN.length() < 5) {
			throw new CarException();
		}
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @return the drivingLicense
	 */
	public String getDrivingLicense() {
		return drivingLicense;
	}

	/**
	 * @return the begin
	 */
	public LocalDate getBegin() {
		return begin;
	}

	/**
	 * @return the end
	 */
	public LocalDate getEnd() {
		return end;
	}

	/**
	 * @return the vehicle
	 */
	public Vehicle getVehicle() {
		return vehicle;
	}
	
	public String getClientNIF() {
		return this.clientNIF;
	}
	
	public String getClientIBAN() {
		return this.clientIBAN;
	}	

	/**
	 * @param begin
	 * @param end
	 * @return <code>true</code> if this Renting conflicts with the given date
	 *         range.
	 */
	public boolean conflict(LocalDate begin, LocalDate end) {
		if (end.isBefore(begin)) {
			throw new CarException("Error: end date is before begin date.");
		} else if ((begin.equals(this.getBegin()) || begin.isAfter(this.getBegin()))
				&& (begin.isBefore(this.getEnd()) || begin.equals(this.getEnd()))) {
			return true;
		} else if ((end.equals(this.getEnd()) || end.isBefore(this.getEnd()))
				&& (end.isAfter(this.getBegin()) || end.isEqual(this.getBegin()))) {
			return true;
		} else if ((begin.isBefore(this.getBegin()) && end.isAfter(this.getEnd()))) {
			return true;
		}

		return false;
	}

	/**
	 * Settle this renting and update the kilometers in the vehicle.
	 * 
	 * @param kilometers
	 */
	public void checkout(int kilometers) {
		this.kilometers = kilometers;
		this.vehicle.addKilometers(this.kilometers);
	}

	public String cancel() {
		this.cancellation = this.reference + "CANCEL";
		this.cancellationDate = new LocalDate();
		
		this.rentACar.getProcessor().submitRenting(this);
		
		return this.cancellation;
	}

	public boolean isCancelled() {
		return this.cancellation != null;
	}

	public String getCancellation() {
		return this.cancellation;
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
	
	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}
	
	public String getInvoiceReference() {
		return this.invoiceReference;
	}
	
	public void setInvoiceReference(String invoiceReference) {
		this.invoiceReference = invoiceReference;
	}
	
	public String getRentACarNif() {
		return this.rentACarNif;
	}

	
	public String getType() {
		return this.type;
	}
	
	public String getCancelledPaymentReference() {
		return this.cancelledPaymentReference;
	}

	public void setCancelledPaymentReference(String cancelledPaymentReference) {
		this.cancelledPaymentReference = cancelledPaymentReference;
	}
	
	public boolean isCancelledInvoice() {
		return this.cancelledInvoice;
	}

	public void setCancelledInvoice(boolean cancelledInvoice) {
		this.cancelledInvoice = cancelledInvoice;
	}

}
