package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class ProcessPaymentState extends AdventureState {
	public static final int MAX_REMOTE_ERRORS = 3;

	@Override
	public State getState() {
		return State.PROCESS_PAYMENT;
	}

	@Override
	public void process(Adventure adventure) {
		try {
			
			adventure.updateAmount();
			adventure.setPaymentConfirmation(BankInterface.processPayment(adventure.getClientIBAN(), adventure.getAmount()));
			try {
				InvoiceData invoice = new InvoiceData(adventure.getBroker().getNIFSeller(), adventure.getClientIBAN(), "Servico", adventure.getAmount(), LocalDate.now());
				adventure.setPayementInvoiceReference(TaxInterface.submitInvoice(invoice));
				
			} catch (TaxException te) {
				//implement handling of exception
			}
		} catch (BankException be) {
			adventure.setState(State.UNDO);
			return;
		} catch (RemoteAccessException rae) {
			incNumOfRemoteErrors();
			if (getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
				adventure.setState(State.UNDO);
			}
			return;
		}

		adventure.setState(State.CONFIRMED);
	}
	
}
