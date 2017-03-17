package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class UndoState extends AdventureState {

	@Override
	public State getState() {
		return State.UNDO;
	}

	@Override
	public void process(Adventure adventure) {
		if (requiresCancelPayment(adventure)) {
			try {
				adventure.setPaymentCancellation(BankInterface.cancelPayment(adventure.getPaymentConfirmation()));
			} catch (BankException | RemoteAccessException ex) {
				// does not change state
			}
		}

		if (requiresCancelActivity(adventure)) {
			try {
				adventure.setActivityCancellation(
						ActivityInterface.cancelReservation(adventure.getActivityConfirmation()));
			} catch (ActivityException | RemoteAccessException ex) {
				// does not change state
			}
		}

		if (requiresCancelRoom(adventure)) {
			try {
				adventure.setRoomCancellation(HotelInterface.cancelBooking(adventure.getRoomConfirmation()));
			} catch (HotelException | RemoteAccessException ex) {
				// does not change state
			}
		}

		if (!requiresCancelPayment(adventure) && !requiresCancelActivity(adventure) && !requiresCancelRoom(adventure)) {
			adventure.setState(State.CANCELLED);
		}
	}

	public boolean requiresCancelRoom(Adventure adventure) {
		return adventure.getRoomConfirmation() != null && adventure.getRoomCancellation() == null;
	}

	public boolean requiresCancelActivity(Adventure adventure) {
		return adventure.getActivityConfirmation() != null && adventure.getActivityCancellation() == null;
	}

	public boolean requiresCancelPayment(Adventure adventure) {
		return adventure.getPaymentConfirmation() != null && adventure.getPaymentCancellation() == null;
	}

}
