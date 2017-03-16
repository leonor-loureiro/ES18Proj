package pt.ulisboa.tecnico.softeng.broker.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;

public class CancelledState extends AdventureState {
	private static Logger logger = LoggerFactory.getLogger(CancelledState.class);

	@Override
	public State getState() {
		return State.CANCELLED;
	}

	@Override
	public void process(Adventure adventure) {
		logger.debug("booking");

		if (adventure.getPaymentCancellation() != null) {
			BankOperationData operation = BankInterface.getOperationData(adventure.getPaymentConfirmation());
			System.out.println("Payment confirmation: " + adventure.getPaymentConfirmation());
			System.out.println("Type: " + operation.getType());
			System.out.println("Value: " + operation.getValue());

			operation = BankInterface.getOperationData(adventure.getPaymentCancellation());
			System.out.println("Payment cancellation " + adventure.getPaymentCancellation());
			System.out.println("Type: " + operation.getType());
			System.out.println("Value: " + operation.getValue());
		}

		if (adventure.getActivityCancellation() != null) {
			ActivityReservationData data = ActivityInterface
					.getActivityReservationData(adventure.getActivityCancellation());
			System.out.println("Activity confirmation: " + data.getReference());
			System.out.println("Begin: " + data.getBegin());
			System.out.println("End: " + data.getEnd());
			System.out.println("Activity cancellation: " + data.getCancellation());
			System.out.println("Cancellation date: " + data.getCancellationDate());

		}

		if (adventure.getRoomCancellation() != null) {
			RoomBookingData data = HotelInterface.getRoomBookingData(adventure.getRoomCancellation());
			System.out.println("Room confirmation: " + data.getReference());
			System.out.println("Arrival: " + data.getArrival());
			System.out.println("Departure: " + data.getDeparture());
			System.out.println("Room cancellation: " + data.getCancellation());
			System.out.println("Cancellation date: " + data.getCancellationDate());
		}

	}

}
