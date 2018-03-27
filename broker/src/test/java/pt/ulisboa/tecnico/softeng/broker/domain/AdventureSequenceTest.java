package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.activity.domain.Booking;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.domain.Vehicle;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

@RunWith(JMockit.class)
public class AdventureSequenceTest {
	private static final String REFERENCE = "REFERENCE";
	private static final String BROKER_IBAN = "BK01987600000";
	private static final String NIF_AS_BUYER = "123456000";
	private static final String NIF_AS_SELLER = "sellerNIF";
	private static final String NIF_CUSTOMER = "123456789";
	private static final String IBAN = "BK01987654321";
	private static final String DRIVING_LICENSE = "IMT1234";
	private static final double MARGIN = 0.3;
	private static final int AGE = 20;
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String PAYMENT_CANCELLATION = "PaymentCancellation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ACTIVITY_CANCELLATION = "ActivityCancellation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private static final String ROOM_CANCELLATION = "RoomCancellation";
	private static final String RENTING_CONFIRMATION = "RentingConfirmation";
	private static final String RENTING_CANCELLATION = "RentingCancellation";
	private static final String INVOICE_DATA = "InvoiceData";
	private static final LocalDate arrival = new LocalDate(2016, 12, 19);
	private static final LocalDate departure = new LocalDate(2016, 12, 21);

	@Mocked
	private ActivityReservationData activityReservationData;

	@Mocked
	private RentingData rentingData;

	@Mocked
	private RoomBookingData roomBookingData;

	private Broker broker;
	private Client client;

	private static ActivityProvider aprov;
	private static ActivityOffer offer;
	private static Booking book;

	@Before
	public void setUp() {
		this.broker = new Broker("Br013", "HappyWeek", NIF_AS_SELLER, NIF_AS_BUYER, BROKER_IBAN);
		this.client = new Client(this.broker, IBAN, NIF_CUSTOMER, DRIVING_LICENSE, AGE);
	}

	@Test
	public void successSequence(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface,
			@Mocked CarInterface carInterface) {
		// Testing: book activity, hotel, car, pay, tax, confirm
		new Expectations() {
			{

				ActivityInterface.reserveActivity(arrival, departure, AGE, this.anyString, this.anyString);
				this.result = ACTIVITY_CONFIRMATION;

				HotelInterface.reserveRoom(Type.SINGLE, arrival, departure, NIF_AS_BUYER, BROKER_IBAN);
				this.result = ROOM_CONFIRMATION;

				CarInterface.rentCar((Class<? extends Vehicle>) this.any, this.anyString, this.anyString,
						this.anyString, (LocalDate) this.any, (LocalDate) this.any);
				this.result = RENTING_CONFIRMATION;

				BankInterface.processPayment(IBAN, this.anyDouble);
				this.result = PAYMENT_CONFIRMATION;

				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.result = INVOICE_DATA;

				AdventureSequenceTest.this.activityReservationData.getPaymentReference();
				this.result = REFERENCE;

				AdventureSequenceTest.this.activityReservationData.getInvoiceReference();
				this.result = REFERENCE;

				AdventureSequenceTest.this.rentingData.getPaymentReference();
				this.result = REFERENCE;

				AdventureSequenceTest.this.rentingData.getInvoiceReference();
				this.result = REFERENCE;

				AdventureSequenceTest.this.roomBookingData.getPaymentReference();
				this.result = REFERENCE;

				AdventureSequenceTest.this.roomBookingData.getInvoiceReference();
				this.result = REFERENCE;
			}
		};

		final Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN, true);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		assertEquals(State.CONFIRMED, adventure.getState());
	}

	@Test
	public void successSequenceOneNoCar(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked CarInterface carInterface) {
		// Testing: book activity, hotel, pay, tax, confirm
		new Expectations() {
			{

				ActivityInterface.reserveActivity(arrival, departure, AGE, this.anyString, this.anyString);
				this.result = ACTIVITY_CONFIRMATION;

				HotelInterface.reserveRoom(Type.SINGLE, arrival, departure, NIF_AS_BUYER, BROKER_IBAN);
				this.result = ROOM_CONFIRMATION;

				BankInterface.processPayment(IBAN, this.anyDouble);
				this.result = PAYMENT_CONFIRMATION;

				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.result = INVOICE_DATA;

				AdventureSequenceTest.this.activityReservationData.getPaymentReference();
				this.result = REFERENCE;

				AdventureSequenceTest.this.activityReservationData.getInvoiceReference();
				this.result = REFERENCE;

				AdventureSequenceTest.this.roomBookingData.getPaymentReference();
				this.result = REFERENCE;

				AdventureSequenceTest.this.roomBookingData.getInvoiceReference();
				this.result = REFERENCE;
			}
		};

		final Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CONFIRMED, adventure.getState());
	}

	@Test
	public void successSequenceNoHotel(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked CarInterface carInterface) {

		// Testing: book activity, car, pay, tax, confirm
		new Expectations() {
			{
				ActivityInterface.reserveActivity(arrival, arrival, AGE, this.anyString, this.anyString);
				this.result = ACTIVITY_CONFIRMATION;

				CarInterface.rentCar((Class<? extends Vehicle>) this.any, this.anyString, this.anyString,
						this.anyString, (LocalDate) this.any, (LocalDate) this.any);
				this.result = RENTING_CONFIRMATION;

				BankInterface.processPayment(IBAN, this.anyDouble);
				this.result = PAYMENT_CONFIRMATION;

				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.result = INVOICE_DATA;

				AdventureSequenceTest.this.activityReservationData.getPaymentReference();
				this.result = REFERENCE;

				AdventureSequenceTest.this.activityReservationData.getInvoiceReference();
				this.result = REFERENCE;

				AdventureSequenceTest.this.rentingData.getPaymentReference();
				this.result = REFERENCE;

				AdventureSequenceTest.this.rentingData.getInvoiceReference();
				this.result = REFERENCE;
			}
		};

		final Adventure adventure = new Adventure(this.broker, arrival, arrival, this.client, MARGIN, true);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CONFIRMED, adventure.getState());
	}

	@Test
	public void successSequenceNoHotelNoCar(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface) {
		// Testing: book activity, pay, tax, confirm
		new Expectations() {
			{
				ActivityInterface.reserveActivity(arrival, arrival, AGE, this.anyString, this.anyString);
				this.result = ACTIVITY_CONFIRMATION;

				BankInterface.processPayment(IBAN, this.anyDouble);
				this.result = PAYMENT_CONFIRMATION;

				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.result = INVOICE_DATA;

				AdventureSequenceTest.this.activityReservationData.getPaymentReference();
				this.result = REFERENCE;

				AdventureSequenceTest.this.activityReservationData.getInvoiceReference();
				this.result = REFERENCE;
			}
		};

		final Adventure adventure = new Adventure(this.broker, arrival, arrival, this.client, MARGIN);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CONFIRMED, adventure.getState());
	}

	@Test
	public void unsuccessSequenceFailActivity(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface) {
		// Testing: fail activity, undo, cancelled
		new Expectations() {
			{
				ActivityInterface.reserveActivity(arrival, departure, AGE, this.anyString, this.anyString);
				this.result = new ActivityException();
			}
		};

		final Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN);

		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}

	@Test
	public void unsuccessSequenceFailHotel(@Mocked TaxInterface taxInterface, @Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		// Testing: activity, fail hotel, undo, cancelled
		new Expectations() {
			{
				ActivityInterface.reserveActivity(arrival, departure, this.anyInt, this.anyString, this.anyString);
				this.result = ACTIVITY_CONFIRMATION;

				HotelInterface.reserveRoom(Type.SINGLE, arrival, departure, NIF_AS_BUYER, BROKER_IBAN);
				this.result = new HotelException();

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
			}
		};

		final Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}

	@Test
	public void unsuccessSequenceFailCar(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final CarInterface carInterface) {
		// Testing: activity, fail car, undo, cancelled
		new Expectations() {
			{
				ActivityInterface.reserveActivity(arrival, arrival, AGE, this.anyString, this.anyString);
				this.result = ACTIVITY_CONFIRMATION;

				CarInterface.rentCar((Class<? extends Vehicle>) this.any, this.anyString, this.anyString,
						this.anyString, (LocalDate) this.any, (LocalDate) this.any);
				this.result = new CarException();

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
			}
		};

		final Adventure adventure = new Adventure(this.broker, arrival, arrival, this.client, MARGIN, true);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}

	@Test
	public void unsuccessSequenceFailPayment(@Mocked TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final CarInterface carInterface) {
		// Testing: activity, room, car, fail payment, undo, cancelled
		new Expectations() {
			{

				ActivityInterface.reserveActivity(arrival, departure, AGE, this.anyString, this.anyString);
				this.result = ACTIVITY_CONFIRMATION;

				HotelInterface.reserveRoom(Type.SINGLE, arrival, departure, NIF_AS_BUYER, BROKER_IBAN);
				this.result = ROOM_CONFIRMATION;

				CarInterface.rentCar((Class<? extends Vehicle>) this.any, this.anyString, this.anyString,
						this.anyString, (LocalDate) this.any, (LocalDate) this.any);
				this.result = RENTING_CONFIRMATION;

				BankInterface.processPayment(IBAN, this.anyDouble);
				this.result = new BankException();

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;

				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;

				CarInterface.cancelRenting(RENTING_CONFIRMATION);
				this.result = RENTING_CANCELLATION;
			}
		};

		final Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN, true);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}

	@Test
	public void unsuccessSequenceFailTax(@Mocked TaxInterface taxInterface, @Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface,
			@Mocked final CarInterface carInterface) {
		// Testing: activity, room, car, payment, fail tax, undo, cancelled
		new Expectations() {
			{

				ActivityInterface.reserveActivity(arrival, departure, AGE, this.anyString, this.anyString);
				this.result = ACTIVITY_CONFIRMATION;

				HotelInterface.reserveRoom(Type.SINGLE, arrival, departure, NIF_AS_BUYER, BROKER_IBAN);
				this.result = ROOM_CONFIRMATION;

				CarInterface.rentCar((Class<? extends Vehicle>) this.any, this.anyString, this.anyString,
						this.anyString, (LocalDate) this.any, (LocalDate) this.any);
				this.result = RENTING_CONFIRMATION;

				BankInterface.processPayment(IBAN, this.anyDouble);
				this.result = PAYMENT_CONFIRMATION;

				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.result = new TaxException();

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;

				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;

				CarInterface.cancelRenting(RENTING_CONFIRMATION);
				this.result = RENTING_CANCELLATION;

				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
			}
		};

		final Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN, true);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}

	@After
	public void tearDown() {
		Broker.brokers.clear();
	}
}