package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;

public class Adventure {
	private static Logger logger = LoggerFactory.getLogger(Adventure.class);

	public static enum State {
		PROCESS_PAYMENT, RESERVE_ACTIVITY, BOOK_ROOM, RENT_VEHICLE, UNDO, CONFIRMED, CANCELLED
	}
	
	private int amount = 0;
	private static int counter = 0;
	
	private final int margin;
	private final String ID;
	private final Broker broker;
	private final LocalDate begin;
	private final LocalDate end;
	private final Client client;
	private final boolean rentVehicle;
	private String paymentConfirmation;
	private String paymentCancellation;
	private String roomConfirmation;
	private String roomCancellation;
	private String activityConfirmation;
	private String activityCancellation;
	private String rentingConfirmation;
	private String rentingCancellation;
	
	private AdventureState state;

	public Adventure(Broker broker, LocalDate begin, LocalDate end, Client client, int margin, boolean rentCar) {
		checkArguments(broker, begin, end, client, margin);
		
		this.ID = broker.getCode() + Integer.toString(++counter);
		this.end     = end;
		this.client  = client;
		this.broker  = broker;
		this.begin   = begin;
		this.margin  = margin;
		this.rentVehicle = rentCar;

		broker.addAdventure(this);

		setState(State.RESERVE_ACTIVITY);
	}

	private void checkArguments(Broker broker, LocalDate begin, LocalDate end, Client client, int margin) {
		if (broker == null || begin == null || end == null) {
			throw new BrokerException();
		}

		if (end.isBefore(begin)) {
			throw new BrokerException();
		}

		if ( client == null) {
			throw new BrokerException();
		}

		if (margin < 1) {
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
	
	public String getClientNIF() {
		return this.client.getNif();
	}
	
	public String getClientIBAN() {
		return this.client.getIban();
	}
	
	public int getAge() {
		return this.client.getAge();
	}

	public String getIBAN() {
		return this.client.getIban();
	}
	
	public String getDriversLicense() {
		return this.client.getDrivingLicense();
	}

	public void updateAmount() {
		int sum = 0;
		if (activityConfirmation != null)
			sum += ActivityInterface.getActivityReservationData(activityConfirmation).getAmount();
		if(roomConfirmation != null)
			sum += HotelInterface.getRoomBookingData(roomConfirmation).getAmount();
		if(rentingConfirmation != null)
			sum += CarInterface.getRentingData(rentingConfirmation).getAmount();
		amount = sum;
	}
	
	public int getAmount() {
		
		return this.amount * (1 + margin);
	}
	
	public boolean getRentVehicle() {
		return this.rentVehicle;
	}

	public String getPaymentConfirmation() {
		return this.paymentConfirmation;
	}

	public int getMargin() {
		return this.margin;
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
		this.state.process(this);
	}

	public boolean cancelRoom() {
		return getRoomConfirmation() != null && getRoomCancellation() == null;
	}

	public boolean cancelActivity() {
		return getActivityConfirmation() != null && getActivityCancellation() == null;
	}

	public boolean cancelPayment() {
		return getPaymentConfirmation() != null && getPaymentCancellation() == null;
	}
	
	public boolean cancelRenting() {
		return getRentingConfirmation() != null && getRentingCancellation() == null;
	}

}
