package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class ConfirmedState extends AdventureState {
	public static int MAX_BANK_EXCEPTIONS = 5;

	private int numberOfBankExceptions = 0;

	@Override
	public State getState() {
		return State.CONFIRMED;
	}

	@Override
	public void process(Adventure adventure) {
		BankOperationData operation;
		try {
			operation = BankInterface.getOperationData(adventure.getPaymentConfirmation());
		} catch (final BankException be) {
			this.numberOfBankExceptions++;
			if (this.numberOfBankExceptions == MAX_BANK_EXCEPTIONS) {
				adventure.setState(State.UNDO);
			}
			return;
		} catch (final RemoteAccessException rae) {
			return;
		}
		this.numberOfBankExceptions = 0;

		ActivityReservationData reservation;
		try {
			reservation = ActivityInterface.getActivityReservationData(adventure.getActivityConfirmation());
		} catch (final ActivityException ae) {
			adventure.setState(State.UNDO);
			return;
		} catch (final RemoteAccessException rae) {
			return;
		}
		if (reservation.getPaymentReference() == null || reservation.getInvoiceReference() == null) {
			adventure.setState(State.UNDO);
			return;
		}
		resetNumOfRemoteErrors();

		if (adventure.getRentingConfirmation() != null) {
			RentingData rentingData;
			try {
				rentingData = CarInterface.getRentingData(adventure.getRentingConfirmation());
			} catch (final CarException he) {
				adventure.setState(State.UNDO);
				return;
			} catch (final RemoteAccessException rae) {
				return;
			}
			if (rentingData.getPaymentReference() == null || rentingData.getInvoiceReference() == null) {
				adventure.setState(State.UNDO);
				return;
			}
			resetNumOfRemoteErrors();
		}

		if (adventure.getRoomConfirmation() != null) {
			RoomBookingData booking;
			try {
				booking = HotelInterface.getRoomBookingData(adventure.getRoomConfirmation());
			} catch (final HotelException he) {
				adventure.setState(State.UNDO);
				return;
			} catch (final RemoteAccessException rae) {
				return;
			}
			if (booking.getPaymentReference() == null || booking.getInvoiceReference() == null) {
				adventure.setState(State.UNDO);
				return;
			}
			resetNumOfRemoteErrors();
		}

		// TODO: prints the complete Adventure file, the info in operation,
		// reservation and booking

	}

}
