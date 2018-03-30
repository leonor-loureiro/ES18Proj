package pt.ulisboa.tecnico.softeng.broker.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class CancelledState extends CancelledState_Base {
	private static Logger logger = LoggerFactory.getLogger(CancelledState.class);

	@Override
	public State getValue() {
		return State.CANCELLED;
	}

	@Override
	public void process() {
		if (getAdventure().getPaymentCancellation() != null) {
			try {
				BankInterface.getOperationData(getAdventure().getPaymentConfirmation());
			} catch (BankException | RemoteAccessException e) {
				return;
			}

			try {
				BankInterface.getOperationData(getAdventure().getPaymentCancellation());
			} catch (BankException | RemoteAccessException e) {
				return;
			}
		}

		if (getAdventure().getActivityCancellation() != null) {
			try {
				ActivityInterface.getActivityReservationData(getAdventure().getActivityCancellation());
			} catch (ActivityException | RemoteAccessException e) {
				return;
			}
		}

		if (getAdventure().getRoomCancellation() != null) {
			try {
				HotelInterface.getRoomBookingData(getAdventure().getRoomCancellation());
			} catch (HotelException | RemoteAccessException e) {
				return;
			}
		}

		if (getAdventure().getRentingCancellation() != null) {
			try {
				CarInterface.getRentingData(getAdventure().getRentingCancellation());
			} catch (CarException | RemoteAccessException e) {
				return;
			}
		}

	}

}
