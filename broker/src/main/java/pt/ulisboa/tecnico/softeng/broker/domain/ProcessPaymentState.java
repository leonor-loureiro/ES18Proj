package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.services.remote.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.RemoteAccessException;

public class ProcessPaymentState extends ProcessPaymentState_Base {
	public static final int MAX_REMOTE_ERRORS = 3;

	@Override
	public State getValue() {
		return State.PROCESS_PAYMENT;
	}

	@Override
	public void process() {
		try {
			getAdventure().setPaymentConfirmation(
					BankInterface.processPayment(getAdventure().getIBAN(), getAdventure().getAmount()));
		} catch (BankException be) {
			getAdventure().setState(State.CANCELLED);
			return;
		} catch (RemoteAccessException rae) {
			incNumOfRemoteErrors();
			if (getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
				getAdventure().setState(State.CANCELLED);
			}
			return;
		}

		getAdventure().setState(State.RESERVE_ACTIVITY);
	}

}
