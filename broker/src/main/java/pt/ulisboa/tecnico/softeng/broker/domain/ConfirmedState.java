package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class ConfirmedState extends AdventureState {
	public static int MAX_REMOTE_ERRORS = 20;
	public static int MAX_BANK_EXCEPTIONS = 5;

	private int numberOfBankExceptions = 0;

	@Override
	public State getState() {
		return State.CONFIRMED;
	}

	@Override
	public void process(Adventure adventure) {
		try {
			BankInterface.getOperationData(adventure.getPaymentConfirmation());
			String invoiceReference = adventure.getPaymentInvoiceReference();
			if(!TaxInterface.invoiceSubmited(adventure.getBroker().getNIFBuyer(),invoiceReference)) {
				adventure.setState(State.UNDO);
			}
		} catch (BankException be) {
			this.numberOfBankExceptions++;
			if (this.numberOfBankExceptions == MAX_BANK_EXCEPTIONS) {
				adventure.setState(State.UNDO);
			}
			return;
		} catch (RemoteAccessException rae) {
			incNumOfRemoteErrors();
			if (getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
				adventure.setState(State.UNDO);
			}
			return;
		}
		resetNumOfRemoteErrors();
		this.numberOfBankExceptions = 0;

		try {
			String invoiceReference = ActivityInterface.getActivityReservationData(adventure.getActivityConfirmation()).getInvoiceReference();
			if(!TaxInterface.invoiceSubmited(adventure.getBroker().getNIFBuyer(),invoiceReference)) {
				adventure.setState(State.UNDO);
				return;
			}
		} catch (ActivityException ae) {
			adventure.setState(State.UNDO);
			return;
		} catch (RemoteAccessException rae) {
			incNumOfRemoteErrors();
			if (getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
				adventure.setState(State.UNDO);
			}
			return;
		}
		resetNumOfRemoteErrors();

		if (adventure.getRoomConfirmation() != null) {
			try {
				String invoiceReference = HotelInterface.getRoomBookingData(adventure.getRoomConfirmation()).getInvoiceReference();
				if(!TaxInterface.invoiceSubmited(adventure.getBroker().getNIFBuyer(),invoiceReference)) {
					adventure.setState(State.UNDO);
					return;
				}
			} catch (HotelException he) {
				adventure.setState(State.UNDO);
				return;
			} catch (RemoteAccessException rae) {
				incNumOfRemoteErrors();
				if (getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
					adventure.setState(State.UNDO);
				}
				return;
			}
			resetNumOfRemoteErrors();
		}
		
		if (adventure.getRentingConfirmation() != null) {
			try {
				String invoiceReference = CarInterface.getRentingData(adventure.getRentingConfirmation()).getInvoiceReference();
				if(!TaxInterface.invoiceSubmited(adventure.getBroker().getNIFBuyer(),invoiceReference)) {
					adventure.setState(State.UNDO);
					return;
				}
			} catch (CarException he) {
				adventure.setState(State.UNDO);
				return;
			} catch (RemoteAccessException rae) {
				incNumOfRemoteErrors();
				if (getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
					adventure.setState(State.UNDO);
				}
				return;
			}
			resetNumOfRemoteErrors();
		}

		// TODO: prints the complete Adventure file, the info in operation,
		// reservation and booking

	}

}
