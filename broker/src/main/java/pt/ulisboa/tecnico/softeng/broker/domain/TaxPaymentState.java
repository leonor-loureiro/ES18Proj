package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class TaxPaymentState extends AdventureState {
	public static final int MAX_REMOTE_ERRORS = 3;

	@Override
	public State getState() {
		return State.TAX_PAYMENT;
	}

	@Override
	public void process(Adventure adventure) {
		try {
			InvoiceData invoiceData = new InvoiceData(adventure.getBroker().getNifAsSeller(),
					adventure.getClient().getNIF(), "ADVENTURE", adventure.getAmount(), adventure.getBegin());
			adventure.setInvoiceReference(TaxInterface.submitInvoice(invoiceData));
		} catch (TaxException be) {
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
