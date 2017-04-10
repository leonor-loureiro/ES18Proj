package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.services.remote.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.services.remote.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.services.remote.HotelInterface;
import pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.HotelException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.RemoteAccessException;

public class ConfirmedState extends ConfirmedState_Base {
	public static int MAX_REMOTE_ERRORS = 20;
	public static int MAX_BANK_EXCEPTIONS = 5;

	public ConfirmedState() {
		super();
		setNumberOfBankExceptions(0);
	}

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
			setNumberOfBankExceptions(getNumberOfBankExceptions() + 1);
			if (getNumberOfBankExceptions() == MAX_BANK_EXCEPTIONS) {
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
		setNumberOfBankExceptions(0);

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
