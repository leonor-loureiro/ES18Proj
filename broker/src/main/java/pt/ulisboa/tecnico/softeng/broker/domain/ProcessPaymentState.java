package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;

public class ProcessPaymentState extends AdventureState {
	public static final int MAX_REMOTE_ERRORS = 3;

	@Override
	public State getState() {
		return State.PROCESS_PAYMENT;
	}

	@Override
	public void process(Adventure adventure) {
		try {
			adventure.setPaymentConfirmation(BankInterface.processPayment(adventure.getIBAN(), adventure.getAmount()));
		} catch (BankException be) {
			adventure.setState(State.CANCELLED);
			return;
		} catch (RemoteAccessException rae) {
			incNumOfRemoteErrors();
			if (getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
				adventure.setState(State.CANCELLED);
			}
			return;
		}

		adventure.setState(State.RESERVE_ACTIVITY);
	}

}
