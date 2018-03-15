package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.activity.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.activity.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class InvoiceProcessor {
	private static InvoiceProcessor instance;

	public static InvoiceProcessor getInvoiceProcessor() {
		if (instance == null) {
			instance = new InvoiceProcessor();
		}
		return instance;
	}

	private final Set<Booking> invoicesToSend = new HashSet<>();

	private InvoiceProcessor() {
	}

	public void submitInvoice(Booking booking) {
		this.invoicesToSend.add(booking);
		processInvoices();
	}

	private void processInvoices() {
		Set<Booking> failedToSend = new HashSet<>();
		for (Booking booking : this.invoicesToSend) {
			InvoiceData invoiceData = new InvoiceData(booking.getProviderNif(), booking.getNif(), booking.getType(),
					booking.getAmount(), booking.getDate());
			try {
				booking.setInvoiceReference(TaxInterface.submitInvoice(invoiceData));
			} catch (TaxException | RemoteAccessException ex) {
				failedToSend.add(booking);
			}
		}

		this.invoicesToSend.clear();
		this.invoicesToSend.addAll(failedToSend);
	}

	public void clean() {
		this.invoicesToSend.clear();
	}

}
