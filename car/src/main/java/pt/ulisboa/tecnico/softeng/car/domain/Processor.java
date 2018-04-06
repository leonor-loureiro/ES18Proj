package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.car.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.car.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.car.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class Processor {
	// important to use a set to avoid double submission of the same booking when it
	// is cancelled while trying to pay or send invoice
	private final Set<Renting> rentingToProcess = new HashSet<>();

	public void submitRenting(Renting renting) {
		this.rentingToProcess.add(renting);
		processInvoices();
	}

	private void processInvoices() {
		Set<Renting> failedToProcess = new HashSet<>();
		for (Renting renting : this.rentingToProcess) {
			if (!renting.isCancelled()) {
				if (renting.getPaymentReference() == null) {
					try {
						renting.setPaymentReference(BankInterface.processPayment(renting.getClientIBAN(), renting.getAmount()));	
					} catch(BankException | RemoteAccessException ex) {
						failedToProcess.add(renting);
						continue;
					}
				}
				InvoiceData invoiceData = new InvoiceData(renting.getRentACarNif(), renting.getClientNIF(), renting.getType(),
						renting.getAmount(), renting.getEnd());
				try {
					renting.setInvoiceReference(TaxInterface.submitInvoice(invoiceData));					
				} catch(TaxException | RemoteAccessException ex) {
					failedToProcess.add(renting);
				}

			} else {
				try {	
					if (renting.getCancelledPaymentReference() == null) {
						renting.setCancelledPaymentReference(
							BankInterface.cancelPayment(renting.getPaymentReference()));
					}
					TaxInterface.cancelInvoice(renting.getInvoiceReference());
					renting.setCancelledInvoice(true);
				} catch (BankException | TaxException e) {
					failedToProcess.add(renting);
				}
			}
		}

		this.rentingToProcess.clear();
		this.rentingToProcess.addAll(failedToProcess);
	}

	public void clean() {
		this.rentingToProcess.clear();
	}

}
