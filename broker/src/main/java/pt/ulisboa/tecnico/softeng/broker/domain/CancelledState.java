package pt.ulisboa.tecnico.softeng.broker.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class CancelledState extends AdventureState {
	private static Logger logger = LoggerFactory.getLogger(CancelledState.class);

	@Override
	public State getState() {
		return State.CANCELLED;
	}

	@Override
	public void process(Adventure adventure) {
		if (adventure.getPaymentCancellation() != null) {
			try {
				BankInterface.getOperationData(adventure.getPaymentConfirmation());
			} catch (BankException | RemoteAccessException e) {
				return;
			}

			try {
				BankInterface.getOperationData(adventure.getPaymentCancellation());
			} catch (BankException | RemoteAccessException e) {
				return;
			}
		}

		if (adventure.getActivityCancellation() != null) {
			try {
				ActivityInterface.getActivityReservationData(adventure.getActivityCancellation());
			} catch (ActivityException | RemoteAccessException e) {
				return;
			}
		}

		if (adventure.getRoomCancellation() != null) {
			try {
				HotelInterface.getRoomBookingData(adventure.getRoomCancellation());
			} catch (HotelException | RemoteAccessException e) {
				return;
			}
		}

	}

}
