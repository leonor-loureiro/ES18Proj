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

	private final Client client;
	private final double margin;
	private final boolean rentVehicle;
	private double currentAmount;
	private String rentingConfirmation;
	private String rentingCancellation;
	private String invoiceReference;
	private boolean invoiceCancelled;

	private AdventureState state;

	public Adventure(Broker broker, LocalDate begin, LocalDate end, Client client, double margin) {
		this(broker, begin, end, client, margin, false);
	}

	public Adventure(Broker broker, LocalDate begin, LocalDate end, Client client, double margin, boolean rentVehicle) {
		checkArguments(broker, begin, end, client, margin);

		setID(broker.getCode() + Integer.toString(broker.getCounter()));
		setBegin(begin);
		setEnd(end);

		this.client = client;
		this.margin = margin;
		this.rentVehicle = rentVehicle;
		this.currentAmount = 0.0;

		broker.addAdventure(this);

		setBroker(broker);

		setState(State.RESERVE_ACTIVITY);
	}

	public void delete() {
		setBroker(null);

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
		return this.client.getAge();
	}

	public String getIBAN() {
		return this.client.getIBAN();
	}

	public Client getClient() {
		return this.client;
	}

	public double getMargin() {
		return this.margin;
	}

	public void incAmountToPay(double toPay) {
		this.currentAmount += toPay;
	}

	public double getAmount() {
		return this.currentAmount * (1 + this.margin);
	}

	public boolean shouldRentVehicle() {
		return this.rentVehicle;
	}

	public String getRentingConfirmation() {
		return this.rentingConfirmation;
	}

	public void setRentingConfirmation(String rentingConfirmation) {
		this.rentingConfirmation = rentingConfirmation;
	}

	public String getRentingCancellation() {
		return this.rentingCancellation;
	}

	public void setRentingCancellation(String rentingCancellation) {
		this.rentingCancellation = rentingCancellation;
	}

	public String getInvoiceReference() {
		return this.invoiceReference;
	}

	public void setInvoiceReference(String invoiceReference) {
		this.invoiceReference = invoiceReference;
	}

	public void setInvoiceCancelled(boolean value) {
		this.invoiceCancelled = value;
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
		return getInvoiceReference() != null && !this.invoiceCancelled;
	}

	public boolean invoiceIsCancelled() {
		return !shouldCancelInvoice();
	}

}
