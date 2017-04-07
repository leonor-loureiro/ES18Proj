package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.broker.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.broker.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.broker.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.broker.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.exception.HotelException;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;

public class ConfirmedState extends ConfirmedState_Base {
	public static int MAX_REMOTE_ERRORS = 20;
	public static int MAX_BANK_EXCEPTIONS = 5;

	private int numberOfBankExceptions = 0;

	@Override
	public State getValue() {
		return State.CONFIRMED;
	}

	@Override
	public void process() {
		BankOperationData operation;
		try {
			operation = BankInterface.getOperationData(getAdventure().getPaymentConfirmation());
		} catch (BankException be) {
			this.numberOfBankExceptions++;
			if (this.numberOfBankExceptions == MAX_BANK_EXCEPTIONS) {
				getAdventure().setState(State.UNDO);
			}
			return;
		} catch (RemoteAccessException rae) {
			incNumOfRemoteErrors();
			if (getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
				getAdventure().setState(State.UNDO);
			}
			return;
		}
		resetNumOfRemoteErrors();
		this.numberOfBankExceptions = 0;

		ActivityReservationData reservation;
		try {
			reservation = ActivityInterface.getActivityReservationData(getAdventure().getActivityConfirmation());
		} catch (ActivityException ae) {
			getAdventure().setState(State.UNDO);
			return;
		} catch (RemoteAccessException rae) {
			incNumOfRemoteErrors();
			if (getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
				getAdventure().setState(State.UNDO);
			}
			return;
		}
		resetNumOfRemoteErrors();

		if (getAdventure().getRoomConfirmation() != null) {
			RoomBookingData booking;
			try {
				booking = HotelInterface.getRoomBookingData(getAdventure().getRoomConfirmation());
			} catch (HotelException he) {
				getAdventure().setState(State.UNDO);
				return;
			} catch (RemoteAccessException rae) {
				incNumOfRemoteErrors();
				if (getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
					getAdventure().setState(State.UNDO);
				}
				return;
			}
			resetNumOfRemoteErrors();
		}

		// TODO: prints the complete Adventure file, the info in operation,
		// reservation and booking

	}

}
