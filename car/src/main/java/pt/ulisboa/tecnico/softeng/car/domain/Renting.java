package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

import pt.ulisboa.tecnico.softeng.car.interfaces.TaxInterface;

public class Renting {
	private static String drivingLicenseFormat = "^[a-zA-Z]+\\d+$";
	private static int counter;

    private static final String type = "RENTAL";
    private final String reference;
	private String cancellationReference;
	private final String drivingLicense;
	private final LocalDate begin;
	private final LocalDate end;
	private int kilometers = -1;
	private final Vehicle vehicle;
	private final String clientNIF;
	private final String clientIBAN;

    private String paymentReference;
    private String invoiceReference;
    private String cancel;
    private LocalDate cancellationDate;
    private boolean cancelledInvoice = false;
    private String cancelledPaymentReference = null;

	public Renting(String drivingLicense, LocalDate begin, LocalDate end, Vehicle vehicle, String buyerNIF, String buyerIBAN) {
		checkArguments(drivingLicense, begin, end, vehicle);
		this.reference = Integer.toString(++Renting.counter);
		this.drivingLicense = drivingLicense;
		this.begin = begin;
		this.end = end;
		this.vehicle = vehicle;
		this.clientNIF = buyerNIF;
		this.clientIBAN = buyerIBAN;
	}

	private void checkArguments(String drivingLicense, LocalDate begin, LocalDate end, Vehicle vehicle) {
		if (drivingLicense == null || !drivingLicense.matches(drivingLicenseFormat) || begin == null || end == null
				|| vehicle == null || end.isBefore(begin))
			throw new CarException();
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	public String getCancellationReference() {
		return cancellationReference;
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

	public LocalDate getCancellationDate() {
		return cancellationDate;
	}

	/**
	 * @return the vehicle
	 */
	public Vehicle getVehicle() {
		return vehicle;
	}

	public boolean isCancelled() {
		return cancellationReference != null && cancellationDate != null;
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
		this.cancellationReference = this.reference + "CANCEL";
		this.cancellationDate = LocalDate.now();

		TaxInterface.cancelInvoice(this.reference);

		this.getVehicle().getRentACar().getProcessor().submitRenting(this);

		return this.cancellationReference;
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

    public String getClientNIF() {
        return clientNIF;
    }

    public String getType() {
        return this.type;
    }

    public void setCancellationReference(String cancellationReference) {
        this.cancellationReference = cancellationReference;
    }

    public void setInvoiceReference(String invoiceReference) {
        this.invoiceReference = invoiceReference;
    }

    public void setCancelledInvoice(boolean cancelledInvoice) {
        this.cancelledInvoice = cancelledInvoice;
    }

    public String getCancelledPaymentReference() {
        return cancelledPaymentReference;
    }

    public boolean isCancelledInvoice() {
        return this.cancelledInvoice;
    }

    public String getCancellation() {
        return this.cancel;
    }


    public void setCancelledPaymentReference(String cancelledPaymentReference) {
        this.cancelledPaymentReference = cancelledPaymentReference;
    }


}
