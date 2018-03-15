package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;

@RunWith(JMockit.class)
public class CancelledStateProcessMethodTest {
	private static final String BROKER_IBAN = "BROKER_IBAN";
	private static final String NIF_AS_BUYER = "buyerNIF";
	private static final String NIF_AS_SELLER = "sellerNIF";
	private static final String NIF = "123456789";
	private static final int AMOUNT = 300;
	private static final int AGE = 20;
	private static final LocalDate arrival = new LocalDate(2016, 12, 19);
	private static final LocalDate departure = new LocalDate(2016, 12, 21);
	private static final String IBAN = "BK01987654321";
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
		this.adventure.setState(State.CANCELLED);
	}

	@Test
	public void didNotPayed(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());

		new Verifications() {
			{
				BankInterface.getOperationData(this.anyString);
				this.times = 0;

				ActivityInterface.getActivityReservationData(this.anyString);
				this.times = 0;

				HotelInterface.getRoomBookingData(this.anyString);
				this.times = 0;
			}
		};
	}

	@Test
	public void cancelledPaymentFirstBankException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setPaymentCancellation(PAYMENT_CANCELLATION);

		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void cancelledPaymentFirstRemoteAccessException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setPaymentCancellation(PAYMENT_CANCELLATION);

		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void cancelledPaymentSecondBankException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setPaymentCancellation(PAYMENT_CANCELLATION);

		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankOperationData();
				this.result = new BankException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void cancelledPaymentSecondRemoteAccessException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setPaymentCancellation(PAYMENT_CANCELLATION);

		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankOperationData();
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void cancelledPayment(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setPaymentCancellation(PAYMENT_CANCELLATION);

		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				BankInterface.getOperationData(PAYMENT_CANCELLATION);
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void cancelledActivity(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setPaymentCancellation(PAYMENT_CANCELLATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setActivityCancellation(ACTIVITY_CANCELLATION);

		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				BankInterface.getOperationData(PAYMENT_CANCELLATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CANCELLATION);
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void cancelledRoom(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setPaymentCancellation(PAYMENT_CANCELLATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setActivityCancellation(ACTIVITY_CANCELLATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setRoomCancellation(ROOM_CANCELLATION);

		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				BankInterface.getOperationData(PAYMENT_CANCELLATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CANCELLATION);

				HotelInterface.getRoomBookingData(ROOM_CANCELLATION);
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	@After
	public void tearDown() {
		Broker.brokers.clear();
	}
}