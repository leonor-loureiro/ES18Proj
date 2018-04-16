package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.hotel.services.remote.BankInterface;
import pt.ulisboa.tecnico.softeng.hotel.services.remote.TaxInterface;
import pt.ulisboa.tecnico.softeng.hotel.services.remote.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.hotel.services.remote.exceptions.BankException;
import pt.ulisboa.tecnico.softeng.hotel.services.remote.exceptions.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.hotel.services.remote.exceptions.TaxException;

public class Processor extends Processor_Base {
	private static Logger logger = LoggerFactory.getLogger(Processor.class);

	public void delete() {
		setHotel(null);

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
								BankInterface.processPayment(booking.getBuyerIban(), booking.getPrice()));
					} catch (BankException | RemoteAccessException ex) {
						failedToProcess.add(booking);
						continue;
					}
				}
				InvoiceData invoiceData = new InvoiceData(booking.getProviderNif(), booking.getBuyerNif(),
						Booking.getType(), booking.getPrice(), booking.getArrival());
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
					if (!booking.getCancelledInvoice()) {
						TaxInterface.cancelInvoice(booking.getInvoiceReference());
						booking.setCancelledInvoice(true);
					}
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