package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.hotel.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;

public class Processor {
	// important to use a set to avoid double submission of the same booking when it
	// is cancelled while trying to pay or send invoice
	private final Set<Booking> bookingToProcess = new HashSet<>();

	public void submitBooking(Booking booking) {
		this.bookingToProcess.add(booking);
		processInvoices();
	}

	private void processInvoices() {
		for (Booking booking : this.bookingToProcess) {
			if (!booking.isCancelled()) {
				if (booking.getPaymentReference() == null) {
					booking.setPaymentReference(BankInterface.processPayment(booking.getClientIBAN(), booking.getAmount()));
				}
				InvoiceData invoiceData = new InvoiceData(booking.getHotelNif(), booking.getClientNIF(), booking.getType(),
						booking.getAmount(), booking.getArrival());
				
				booking.setInvoiceReference(TaxInterface.submitInvoice(invoiceData));
			} else {
				if (booking.getCancelledPaymentReference() == null) {
					booking.setCancelledPaymentReference(
							BankInterface.cancelPayment(booking.getPaymentReference()));
				}
				TaxInterface.cancelInvoice(booking.getInvoiceReference());
				booking.setCancelledInvoice(true);

			}
		}

		this.bookingToProcess.clear();

	}

	public void clean() {
		this.bookingToProcess.clear();
	}

}
