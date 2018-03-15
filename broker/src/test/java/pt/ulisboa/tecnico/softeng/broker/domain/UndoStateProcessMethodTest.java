package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class UndoStateProcessMethodTest {
	private static final String BROKER_IBAN = "BROKER_IBAN";
	private static final String NIF_AS_BUYER = "buyerNIF";
	private static final String NIF_AS_SELLER = "sellerNIF";
	private static final String NIF = "123456789";
	private static final String IBAN = "BK01987654321";
	private static final int AMOUNT = 300;
	private static final int AGE = 20;
	private static final LocalDate arrival = new LocalDate(2016, 12, 19);
	private static final LocalDate departure = new LocalDate(2016, 12, 21);
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String PAYMENT_CANCELLATION = "PaymentCancellation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ACTIVITY_CANCELLATION = "ActivityCancellation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private static final String ROOM_CANCELLATION = "RoomCancellation";
	private Adventure adventure;

	private Broker broker;
	private Client client;

    @Mocked private TaxInterface taxInterface;

    @Before
	public void setUp() {
		this.broker = new Broker("Br013", "HappyWeek", NIF_AS_SELLER, NIF_AS_BUYER, BROKER_IBAN);
		this.client = new Client(this.broker, IBAN, NIF, AGE);

		this.adventure = new Adventure(this.broker, arrival, departure, this.client, AMOUNT);
		this.adventure.setState(State.UNDO);
	}

	@Test
	public void successRevertPayment(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void successRevertPaymentButBankException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = new BankException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void successRevertPaymentButRemoteAccessException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void successRevertPaymentAndActivity(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void successRevertActivity(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		this.adventure.setPaymentCancellation(PAYMENT_CANCELLATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		new Expectations() {
			{
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void successRevertPaymentAndActivityButActivityException(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = new ActivityException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void successRevertPaymentAndActivityButRemoteAccessException(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void successRevertPaymentAndActivityAndRoom(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;

				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void successRevertActivityAndRoom(@Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface) {
		this.adventure.setPaymentCancellation(PAYMENT_CANCELLATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new Expectations() {
			{
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;

				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void successRevertPaymentAndRoom(@Mocked final BankInterface bankInterface,
			@Mocked final HotelInterface roomInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityCancellation(ACTIVITY_CANCELLATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;

				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void successRevertPaymentAndActivityAndRoomButHotelException(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;

				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = new HotelException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void successRevertPaymentAndActivityAndRoomButRemoteAccessException(
			@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;

				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@After
	public void tearDown() {
		Broker.brokers.clear();
	}
}