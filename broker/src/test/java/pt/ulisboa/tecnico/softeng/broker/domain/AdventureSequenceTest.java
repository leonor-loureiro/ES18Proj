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
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;

@RunWith(JMockit.class)
public class AdventureSequenceTest {
	private static final String BROKER_IBAN = "BROKER_IBAN";
	private static final String NIF_AS_BUYER = "buyerNIF";
	private static final String NIF_AS_SELLER = "sellerNIF";
	private static final String NIF = "123456789";
	private static final String IBAN = "BK01987654321";
	private static final String DRIVING_LICENSE = "IMT1234";
	private static final int AMOUNT = 300;
	private static final int AGE = 20;
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String PAYMENT_CANCELLATION = "PaymentCancellation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ACTIVITY_CANCELLATION = "ActivityCancellation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private static final String ROOM_CANCELLATION = "RoomCancellation";
	private static final LocalDate arrival = new LocalDate(2016, 12, 19);
	private static final LocalDate departure = new LocalDate(2016, 12, 21);

	private Broker broker;
	private Client client;

	@Before
	public void setUp() {
		this.broker = new Broker("Br013", "HappyWeek", NIF_AS_SELLER, NIF_AS_BUYER, BROKER_IBAN);
		this.client = new Client(this.broker, IBAN, NIF, DRIVING_LICENSE, AGE);
	}

	@Test
	public void successSequenceOne(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{

				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				ActivityInterface.reserveActivity(arrival, departure, AGE, this.anyString, this.anyString);
				this.result = ACTIVITY_CONFIRMATION;

				HotelInterface.reserveRoom(Type.SINGLE, arrival, departure);
				this.result = ROOM_CONFIRMATION;

				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, AMOUNT);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CONFIRMED, adventure.getState());
	}

	@Test
	public void successSequenceTwo(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{
                TaxInterface.submitInvoice((InvoiceData) any);

                BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				ActivityInterface.reserveActivity(arrival, arrival, AGE, this.anyString, this.anyString);
				this.result = ACTIVITY_CONFIRMATION;

				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, arrival, this.client, AMOUNT);

		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CONFIRMED, adventure.getState());
	}

	@Test
	public void unsuccessSequenceOne(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{
                TaxInterface.submitInvoice((InvoiceData) any);

                BankInterface.processPayment(IBAN, AMOUNT);
				this.result = new BankException();
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, AMOUNT);

		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}

	@Test
	public void unsuccessSequenceTwo(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{
                TaxInterface.submitInvoice((InvoiceData) any);

                BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				ActivityInterface.reserveActivity(arrival, departure, AGE, this.anyString, this.anyString);
				this.result = new ActivityException();

				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, AMOUNT);

		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}

	@Test
	public void unsuccessSequenceThree(@Mocked TaxInterface taxInterface, @Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				ActivityInterface.reserveActivity(arrival, departure, this.anyInt, this.anyString, this.anyString);
				this.result = ACTIVITY_CONFIRMATION;

				HotelInterface.reserveRoom(Type.SINGLE, arrival, departure);
				this.result = new HotelException();

				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, AMOUNT);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}

	@Test
	public void unsuccessSequenceFour(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{
			    TaxInterface.submitInvoice((InvoiceData) any);

				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				ActivityInterface.reserveActivity(arrival, departure, AGE, this.anyString, this.anyString);
				this.result = ACTIVITY_CONFIRMATION;

				HotelInterface.reserveRoom(Type.SINGLE, arrival, departure);
				this.result = ROOM_CONFIRMATION;

				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
				this.times = ConfirmedState.MAX_BANK_EXCEPTIONS;

				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;

				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, AMOUNT);

		adventure.process();
		adventure.process();
		adventure.process();
		for (int i = 0; i < ConfirmedState.MAX_BANK_EXCEPTIONS; i++) {
			adventure.process();
		}
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}

	@After
	public void tearDown() {
		Broker.brokers.clear();
	}
}