package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.services.remote.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.services.remote.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.services.remote.HotelInterface;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.HotelException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.RemoteAccessException;

public class UndoState extends UndoState_Base {

	@Override
	public State getValue() {
		return State.UNDO;
	}

	@Override
	public void process() {
		if (requiresCancelPayment()) {
			try {
				getAdventure()
						.setPaymentCancellation(BankInterface.cancelPayment(getAdventure().getPaymentConfirmation()));
			} catch (BankException | RemoteAccessException ex) {
				// does not change state
			}
		}

		if (requiresCancelActivity()) {
			try {
				getAdventure().setActivityCancellation(
						ActivityInterface.cancelReservation(getAdventure().getActivityConfirmation()));
			} catch (ActivityException | RemoteAccessException ex) {
				// does not change state
			}
		}

		if (requiresCancelRoom()) {
			try {
				getAdventure().setRoomCancellation(HotelInterface.cancelBooking(getAdventure().getRoomConfirmation()));
			} catch (HotelException | RemoteAccessException ex) {
				// does not change state
			}
		}

		if (!requiresCancelPayment() && !requiresCancelActivity() && !requiresCancelRoom()) {
			getAdventure().setState(State.CANCELLED);
		}
	}

	public boolean requiresCancelRoom() {
		return getAdventure().getRoomConfirmation() != null && getAdventure().getRoomCancellation() == null;
	}

	public boolean requiresCancelActivity() {
		return getAdventure().getActivityConfirmation() != null && getAdventure().getActivityCancellation() == null;
	}

	public boolean requiresCancelPayment() {
		return getAdventure().getPaymentConfirmation() != null && getAdventure().getPaymentCancellation() == null;
	}

}
