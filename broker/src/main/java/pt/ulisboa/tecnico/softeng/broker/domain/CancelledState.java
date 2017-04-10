package pt.ulisboa.tecnico.softeng.broker.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class CancelledState extends CancelledState_Base {
	private static Logger logger = LoggerFactory.getLogger(CancelledState.class);

	@Override
	public State getValue() {
		return State.CANCELLED;
	}

	@Override
	public void process() {
		logger.debug("process");

		BankOperationData operation;
		if (getAdventure().getPaymentCancellation() != null) {
			try {
				operation = BankInterface.getOperationData(getAdventure().getPaymentConfirmation());
			} catch (BankException | RemoteAccessException e) {
				return;
			}
			System.out.println("Payment confirmation: " + operation.getReference());
			System.out.println("Type: " + operation.getType());
			System.out.println("Value: " + operation.getValue());

			try {
				operation = BankInterface.getOperationData(getAdventure().getPaymentCancellation());
			} catch (BankException | RemoteAccessException e) {
				return;
			}
			System.out.println("Payment cancellation " + operation.getReference());
			System.out.println("Type: " + operation.getType());
			System.out.println("Value: " + operation.getValue());
		}

		if (getAdventure().getActivityCancellation() != null) {
			ActivityReservationData data;
			try {
				data = ActivityInterface.getActivityReservationData(getAdventure().getActivityCancellation());
			} catch (ActivityException | RemoteAccessException e) {
				return;
			}
			System.out.println("Activity confirmation: " + data.getReference());
			System.out.println("Begin: " + data.getBegin());
			System.out.println("End: " + data.getEnd());
			System.out.println("Activity cancellation: " + data.getCancellation());
			System.out.println("Cancellation date: " + data.getCancellationDate());

		}

		if (getAdventure().getRoomCancellation() != null) {
			RoomBookingData data;
			try {
				data = HotelInterface.getRoomBookingData(getAdventure().getRoomCancellation());
			} catch (HotelException | RemoteAccessException e) {
				return;
			}
			System.out.println("Room confirmation: " + data.getReference());
			System.out.println("Arrival: " + data.getArrival());
			System.out.println("Departure: " + data.getDeparture());
			System.out.println("Room cancellation: " + data.getCancellation());
			System.out.println("Cancellation date: " + data.getCancellationDate());
		}

	}

}
