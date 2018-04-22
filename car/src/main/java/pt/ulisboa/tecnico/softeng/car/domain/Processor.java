package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.car.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.car.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.car.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class Processor extends Processor_Base{

	public void submitRenting(Renting renting) {
		addRenting(renting);
		processInvoices();
	}

	private void processInvoices() {
		Set<Renting> failedToProcess = new HashSet<>();
		for (Renting renting : getRentingSet()) {
			if (!renting.isCancelled()) {
				if (renting.getPaymentReference() == null) {
					try {
						renting.setPaymentReference(
								BankInterface.processPayment(renting.getClientIBAN(), renting.getPrice()));
					} catch (BankException | RemoteAccessException ex) {
						failedToProcess.add(renting);
						continue;
					}
				}

				InvoiceData invoiceData = new InvoiceData(renting.getVehicle().getRentACar().getNIF(),
						renting.getClientNIF(), renting.getType(), renting.getPrice(), renting.getBegin());
				try {
					renting.setInvoiceReference(TaxInterface.submitInvoice(invoiceData));
				} catch (TaxException | RemoteAccessException ex) {
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
				} catch (BankException | TaxException | RemoteAccessException ex) {
					failedToProcess.add(renting);
				}

			}
			renting.setProcessor(null);
		}

		for(Renting renting : failedToProcess)
			addRenting(renting);

	}

	public void clean() {
		for(Renting renting : getRentingSet())
			renting.setProcessor(null);
	}
	
	public void delete() {
		clean();
		setRentACar(null);
		deleteDomainObject();
	}
}
