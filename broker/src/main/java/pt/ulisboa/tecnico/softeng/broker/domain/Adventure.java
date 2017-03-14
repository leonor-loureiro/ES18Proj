package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Adventure {
	private static Logger logger = LoggerFactory.getLogger(Adventure.class);

	public static enum State {
		INITIAL, PAYED, CONFIRMED, CANCELED
	}

	private static int counter = 0;

	private final String ID;
	private final Broker broker;
	private final LocalDate begin;
	private final LocalDate end;
	private final int age;
	private final String IBAN;
	private final int amount;
	private String bankPayment;
	private String roomBooking;
	private String activityBooking;

	private State state;

	public Adventure(Broker broker, LocalDate begin, LocalDate end, int age, String IBAN, int amount) {
		checkArguments(broker, begin, end, age, IBAN, amount);

		this.ID = broker.getCode() + Integer.toString(++counter);
		this.broker = broker;
		this.begin = begin;
		this.end = end;
		this.age = age;
		this.IBAN = IBAN;
		this.amount = amount;
		this.state = State.INITIAL;

		broker.addAdventure(this);
	}

	private void checkArguments(Broker broker, LocalDate begin, LocalDate end, int age, String IBAN, int amount) {
		if (broker == null || begin == null || end == null || IBAN == null || IBAN.trim().length() == 0) {
			throw new BrokerException();
		}

		if (end.isBefore(begin)) {
			throw new BrokerException();
		}

		if (age < 18 || age > 100) {
			throw new BrokerException();
		}

		if (amount < 1) {
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
		return this.age;
	}

	public String getIBAN() {
		return this.IBAN;
	}

	public int getAmount() {
		return this.amount;
	}

	public String getBankPayment() {
		return this.bankPayment;
	}

	public String getRoomBooking() {
		return this.roomBooking;
	}

	public String getActivityBooking() {
		return this.activityBooking;
	}

	public State getState() {
		return this.state;
	}

	public void process() {
		logger.debug("process ID:{} ", this.ID);

		switch (this.state) {
		case INITIAL:
			try {
				this.bankPayment = BankInterface.processPayment(this.IBAN, this.amount);
				this.state = State.PAYED;
			} catch (BankException be) {

			}
			break;
		case PAYED:
			if (this.roomBooking == null) {
				try {
					this.roomBooking = HotelInterface.reserveHotel(Room.Type.SINGLE, this.begin, this.end);
				} catch (HotelException e) {

				}
			}

			if (this.activityBooking == null) {
				try {
					this.activityBooking = ActivityInterface.reserveActivity(this.begin, this.end, this.age);
				} catch (ActivityException e) {

				}
			}

			if (this.roomBooking != null && this.activityBooking != null) {
				this.state = State.CONFIRMED;
			}
			break;
		case CONFIRMED:
			break;
		case CANCELED:
			break;
		default:
			throw new BrokerException();
		}
	}

}
