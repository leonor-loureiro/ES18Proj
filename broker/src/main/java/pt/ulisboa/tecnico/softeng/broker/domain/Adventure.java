package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;

public class Adventure {
	private static Logger logger = LoggerFactory.getLogger(Adventure.class);

	public enum State {
		PROCESS_PAYMENT, RESERVE_ACTIVITY, BOOK_ROOM, RENT_VEHICLE, UNDO, CONFIRMED, CANCELLED
	}

	private static int counter = 0;

	private final String ID;
	private final Broker broker;
	private final LocalDate begin;
	private final LocalDate end;
	private final Client client;
	private final double margin;
	private final boolean rentVehicle;
	private String paymentConfirmation;
	private String paymentCancellation;
	private String roomConfirmation;
	private String roomCancellation;
	private String rentingConfirmation;
	private String rentingCancellation;
	private String activityConfirmation;
	private String activityCancellation;

    private String invoiceReference;

    private double currentAmount;


    private AdventureState state;

    public Adventure(Broker broker, LocalDate begin, LocalDate end, Client client, double margin) {
    	this(broker, begin, end, client, margin, false);
	}

	public Adventure(Broker broker, LocalDate begin, LocalDate end, Client client, double margin, boolean rentVehicle) {
		checkArguments(broker, begin, end, client, margin);

		this.ID = broker.getCode() + Integer.toString(++counter);
		this.broker = broker;
		this.begin = begin;
		this.end = end;
		this.client = client;
		this.margin = margin;
		this.rentVehicle = rentVehicle;
		this.currentAmount = 0.0;

		broker.addAdventure(this);

		setState(State.RESERVE_ACTIVITY);
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

	public String getID() {
		return this.ID;
	}

	public Broker getBroker() {
		return this.broker;
	}

	public LocalDate getBegin() {
		return this.begin;
	}

	public LocalDate getEnd() {
		return this.end;
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

	public double getMargin() { return this.margin; }

	public void incAmountToPay(double toPay) {
        this.currentAmount += toPay;
    }

	public double getAmount() {
		return this.currentAmount * (1 + this.margin);
	}

	public boolean shouldRentVehicle() {
		return rentVehicle;
	}

	public String getPaymentConfirmation() {
		return this.paymentConfirmation;
	}

	public void setPaymentConfirmation(String paymentConfirmation) {
		this.paymentConfirmation = paymentConfirmation;
	}

	public String getPaymentCancellation() {
		return this.paymentCancellation;
	}

	public void setPaymentCancellation(String paymentCancellation) {
		this.paymentCancellation = paymentCancellation;
	}

	public String getActivityConfirmation() {
		return this.activityConfirmation;
	}

	public void setActivityConfirmation(String activityConfirmation) {
		this.activityConfirmation = activityConfirmation;
	}

	public String getActivityCancellation() {
		return this.activityCancellation;
	}

	public void setActivityCancellation(String activityCancellation) {
		this.activityCancellation = activityCancellation;
	}

	public String getRoomConfirmation() {
		return this.roomConfirmation;
	}

	public void setRoomConfirmation(String roomConfirmation) {
		this.roomConfirmation = roomConfirmation;
	}

	public String getRoomCancellation() {
		return this.roomCancellation;
	}

	public void setRoomCancellation(String roomCancellation) {
		this.roomCancellation = roomCancellation;
	}

	public String getRentingConfirmation() {
		return rentingConfirmation;
	}

	public void setRentingConfirmation(String rentingConfirmation) {
		this.rentingConfirmation = rentingConfirmation;
	}

	public String getRentingCancellation() {
		return rentingCancellation;
	}

	public void setRentingCancellation(String rentingCancellation) {
		this.rentingCancellation = rentingCancellation;
	}

	public String getInvoiceReference() {
		return invoiceReference;
	}

	public State getState() {
		return this.state.getState();
	}

	public void setState(State state) {
		switch (state) {
		case PROCESS_PAYMENT:
			this.state = new ProcessPaymentState();
			break;
		case RESERVE_ACTIVITY:
			this.state = new ReserveActivityState();
			break;
		case BOOK_ROOM:
			this.state = new BookRoomState();
			break;
		case RENT_VEHICLE:
			this.state = new RentVehicleState();
			break;
		case UNDO:
			this.state = new UndoState();
			break;
		case CONFIRMED:
			this.state = new ConfirmedState();
			break;
		case CANCELLED:
			this.state = new CancelledState();
			break;
		default:
			new BrokerException();
			break;
		}
	}

	public void process() {
		// logger.debug("process ID:{}, state:{} ", this.ID, getState().name());
		this.state.process(this);
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

    public void setInvoiceReference(String invoiceReference) {
        this.invoiceReference = invoiceReference;
    }

}
