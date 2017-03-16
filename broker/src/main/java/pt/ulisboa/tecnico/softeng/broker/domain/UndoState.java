package pt.ulisboa.tecnico.softeng.broker.domain;

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
		if (cancelPayment(adventure)) {
			try {
				adventure.setPaymentCancellation(BankInterface.cancelPayment(adventure.getPaymentConfirmation()));
			} catch (HotelException | RemoteAccessException ex) {
				// does not change state
			}
		}

		if (cancelActivity(adventure)) {
			try {
				adventure.setActivityCancellation(
						ActivityInterface.cancelReservation(adventure.getActivityConfirmation()));
			} catch (HotelException | RemoteAccessException ex) {
				// does not change state
			}
		}

		if (cancelRoom(adventure)) {
			try {
				adventure.setRoomCancellation(HotelInterface.cancelBooking(adventure.getRoomConfirmation()));
			} catch (HotelException | RemoteAccessException ex) {
				// does not change state
			}
		}

		if (!cancelPayment(adventure) && !cancelActivity(adventure) && !cancelRoom(adventure)) {
			adventure.setState(State.CANCELLED);
		}
	}

	public boolean cancelRoom(Adventure adventure) {
		return adventure.getRoomConfirmation() != null && adventure.getRoomCancellation() == null;
	}

	public boolean cancelActivity(Adventure adventure) {
		return adventure.getActivityConfirmation() != null && adventure.getActivityCancellation() == null;
	}

	public boolean cancelPayment(Adventure adventure) {
		return adventure.getPaymentConfirmation() != null && adventure.getPaymentCancellation() == null;
	}

}
