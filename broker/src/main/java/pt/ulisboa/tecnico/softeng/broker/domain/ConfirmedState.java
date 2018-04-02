package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
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
		try {
			BankInterface.getOperationData(getAdventure().getPaymentConfirmation());
		} catch (BankException be) {
			this.numberOfBankExceptions++;
			if (this.numberOfBankExceptions == MAX_BANK_EXCEPTIONS) {
				getAdventure().setState(State.UNDO);
			}
			return;
		} catch (final RemoteAccessException rae) {
			return;
		}
		this.numberOfBankExceptions = 0;

		ActivityReservationData reservation;
		try {
			reservation = ActivityInterface.getActivityReservationData(getAdventure().getActivityConfirmation());
		} catch (ActivityException ae) {
			getAdventure().setState(State.UNDO);
			return;
		} catch (final RemoteAccessException rae) {
			return;
		}
		if (reservation.getPaymentReference() == null || reservation.getInvoiceReference() == null) {
			getAdventure().setState(State.UNDO);
			return;
		}

		if (getAdventure().getRentingConfirmation() != null) {
			RentingData rentingData;
			try {
				rentingData = CarInterface.getRentingData(getAdventure().getRentingConfirmation());
			} catch (CarException he) {
				getAdventure().setState(State.UNDO);
				return;
			} catch (RemoteAccessException rae) {
				return;
			}
			if (rentingData.getPaymentReference() == null || rentingData.getInvoiceReference() == null) {
				getAdventure().setState(State.UNDO);
				return;
			}
		}

		if (getAdventure().getRoomConfirmation() != null) {
			RoomBookingData booking;
			try {
				booking = HotelInterface.getRoomBookingData(getAdventure().getRoomConfirmation());
			} catch (final HotelException he) {
				getAdventure().setState(State.UNDO);
				return;
			} catch (final RemoteAccessException rae) {
				return;
			}
			if (booking.getPaymentReference() == null || booking.getInvoiceReference() == null) {
				getAdventure().setState(State.UNDO);
				return;
			}
		}
	}

}
