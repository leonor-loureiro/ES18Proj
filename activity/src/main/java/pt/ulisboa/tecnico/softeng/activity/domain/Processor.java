package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.activity.services.remote.BankInterface;
import pt.ulisboa.tecnico.softeng.activity.services.remote.TaxInterface;
import pt.ulisboa.tecnico.softeng.activity.services.remote.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.activity.services.remote.exceptions.BankException;
import pt.ulisboa.tecnico.softeng.activity.services.remote.exceptions.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.activity.services.remote.exceptions.TaxException;

public class Processor extends Processor_Base {
	public void delete() {
		setActivityProvider(null);

		for (Booking booking : getBookingSet()) {
			booking.delete();
		}

		deleteDomainObject();
	}

	public void submitBooking(Booking booking) {
		addBooking(booking);
		processInvoices();
	}

	private void processInvoices() {
		Set<Booking> failedToProcess = new HashSet<>();
		for (Booking booking : getBookingSet()) {
			if (!booking.isCancelled()) {
				if (booking.getPaymentReference() == null) {
					try {
						booking.setPaymentReference(
								BankInterface.processPayment(booking.getIban(), booking.getAmount()));
					} catch (BankException | RemoteAccessException ex) {
						failedToProcess.add(booking);
						continue;
					}
				}
				InvoiceData invoiceData = new InvoiceData(booking.getProviderNif(), booking.getBuyerNif(),
						booking.getType(), booking.getAmount(), booking.getDate());
				try {
					booking.setInvoiceReference(TaxInterface.submitInvoice(invoiceData));
				} catch (TaxException | RemoteAccessException ex) {
					failedToProcess.add(booking);
				}
			} else {
				try {
					if (booking.getCancelledPaymentReference() == null) {
						booking.setCancelledPaymentReference(
								BankInterface.cancelPayment(booking.getPaymentReference()));
					}
					TaxInterface.cancelInvoice(booking.getInvoiceReference());
					booking.setCancelledInvoice(true);
				} catch (BankException | TaxException | RemoteAccessException ex) {
					failedToProcess.add(booking);
				}

			}
		}

		for (Booking booking : getBookingSet()) {
			removeBooking(booking);
		}

		for (Booking booking : failedToProcess) {
			addBooking(booking);
		}
	}

}
