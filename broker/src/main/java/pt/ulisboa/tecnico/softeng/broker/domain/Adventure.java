package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class Adventure extends Adventure_Base {
	private static Logger logger = LoggerFactory.getLogger(Adventure.class);

	public enum State {
		PROCESS_PAYMENT, RESERVE_ACTIVITY, BOOK_ROOM, RENT_VEHICLE, UNDO, CONFIRMED, CANCELLED, TAX_PAYMENT
	}

	public Adventure(Broker broker, LocalDate begin, LocalDate end, Client client, double margin) {
		this(broker, begin, end, client, margin, false);
	}

	public Adventure(Broker broker, LocalDate begin, LocalDate end, Client client, double margin, boolean rentVehicle) {
		checkArguments(broker, begin, end, client, margin);

		setID(broker.getCode() + Integer.toString(broker.getCounter()));

        setBroker(broker);
		setBegin(begin);
		setEnd(end);
		setClient(client);
        setMargin(margin);
        setRentVehicle(rentVehicle);
        setCurrentAmount(0.0);

		broker.addAdventure(this);

		setState(State.RESERVE_ACTIVITY);
	}

	public void delete() {
		setBroker(null);
        setClient(null);

		getState().delete();

		deleteDomainObject();
	}

	private void checkArguments(Broker broker, LocalDate begin, LocalDate end, Client client, double margin) {
		if (broker == null || begin == null || end == null) {
			throw new BrokerException();
		}

		if (end.isBefore(begin)) {
			throw new BrokerException();
		}

		if (client.getAge() < 18 || client.getAge() > 100) {
			throw new BrokerException();
		}

		if (margin <= 0 || margin > 1) {
			throw new BrokerException();
		}
	}

	public int getAge() {
		return super.getClient().getAge();
	}

	public String getIBAN() {
		return super.getClient().getIBAN();
	}

	public Client getClient() {
		return super.getClient();
	}

	public double getMargin() {
		return super.getMargin();
	}

	public void incAmountToPay(double toPay) { super.setCurrentAmount(super.getCurrentAmount() + toPay); }

	public double getAmount() { return super.getCurrentAmount() * (1 + super.getMargin()); }

	public boolean shouldRentVehicle() { return super.getRentVehicle(); }

	public String getRentingConfirmation() {
		return super.getRentingConfirmation();
	}

	public void setRentingConfirmation(String rentingConfirmation) {
		super.setRentingConfirmation(rentingConfirmation);
	}

	public String getRentingCancellation() {
		return super.getRentingCancellation();
	}

	public void setRentingCancellation(String rentingCancellation) { super.setRentingCancellation(rentingCancellation); }

	public String getInvoiceReference() {
		return super.getInvoiceReference();
	}

	public void setInvoiceReference(String invoiceReference) {
		super.setInvoiceReference(invoiceReference);
	}

	public void setInvoiceCancelled(boolean value) {
		super.setInvoiceCancelled(value);
	}

	public void setState(State state) {
		if (getState() != null) {
			getState().delete();
		}

		switch (state) {
		case RESERVE_ACTIVITY:
			setState(new ReserveActivityState());
			break;
		case BOOK_ROOM:
			setState(new BookRoomState());
			break;
		case RENT_VEHICLE:
			setState(new RentVehicleState());
			break;
		case PROCESS_PAYMENT:
			setState(new ProcessPaymentState());
			break;
		case TAX_PAYMENT:
			setState(new TaxPaymentState());
			break;
		case UNDO:
			setState(new UndoState());
			break;
		case CONFIRMED:
			setState(new ConfirmedState());
			break;
		case CANCELLED:
			setState(new CancelledState());
			break;
		default:
			new BrokerException();
			break;
		}
	}

	public void process() {
		// logger.debug("process ID:{}, state:{} ", this.ID, getState().name());
		getState().process();
	}

	public boolean shouldCancelRoom() {
		return getRoomConfirmation() != null && getRoomCancellation() == null;
	}

	public boolean roomIsCancelled() {
		return !shouldCancelRoom();
	}

	public boolean shouldCancelActivity() {
		return getActivityConfirmation() != null && getActivityCancellation() == null;
	}

	public boolean activityIsCancelled() {
		return !shouldCancelActivity();
	}

	public boolean shouldCancelPayment() {
		return getPaymentConfirmation() != null && getPaymentCancellation() == null;
	}

	public boolean paymentIsCancelled() {
		return !shouldCancelPayment();
	}

	public boolean shouldCancelVehicleRenting() {
		return getRentingConfirmation() != null && getRentingCancellation() == null;
	}

	public boolean rentingIsCancelled() {
		return !shouldCancelVehicleRenting();
	}

	public boolean shouldCancelInvoice() {
		return getInvoiceReference() != null && !super.getInvoiceCancelled();
	}

	public boolean invoiceIsCancelled() {
		return !shouldCancelInvoice();
	}

}
