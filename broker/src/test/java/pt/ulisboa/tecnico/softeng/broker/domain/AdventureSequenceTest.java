package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.services.remote.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.services.remote.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.services.remote.CarInterface;
import pt.ulisboa.tecnico.softeng.broker.services.remote.HotelInterface;
import pt.ulisboa.tecnico.softeng.broker.services.remote.HotelInterface.Type;
import pt.ulisboa.tecnico.softeng.broker.services.remote.TaxInterface;
import pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.CarException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.HotelException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.TaxException;

@RunWith(JMockit.class)

public class AdventureSequenceTest extends RollbackTestAbstractClass {

	@Mocked
	private ActivityReservationData activityReservationData;

	@Mocked
	private RentingData rentingData;

	@Mocked
	private RoomBookingData roomBookingData;

	@Override
	public void populate4Test() {
		this.broker = new Broker("BR01", "eXtremeADVENTURE", BROKER_NIF_AS_SELLER, BROKER_NIF_AS_BUYER, BROKER_IBAN);
		this.client = new Client(this.broker, CLIENT_IBAN, CLIENT_NIF, DRIVING_LICENSE, AGE);
	}

	@Test
	public void successSequence(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface,
			@Mocked final CarInterface carInterface) {
		// Testing: book activity, hotel, car, pay, tax, confirm
		new Expectations() {
			{

				ActivityInterface.reserveActivity(arrival, departure, AGE, this.anyString, this.anyString);
				this.result = ACTIVITY_CONFIRMATION;

				HotelInterface.reserveRoom(Type.SINGLE, arrival, departure, BROKER_NIF_AS_BUYER, BROKER_IBAN);
				this.result = ROOM_CONFIRMATION;

				CarInterface.rentCar((CarInterface.Type) this.any, this.anyString, this.anyString, this.anyString,
						(LocalDate) this.any, (LocalDate) this.any);
				this.result = RENTING_CONFIRMATION;

				BankInterface.processPayment(CLIENT_IBAN, this.anyDouble);
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

		Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN, true);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		assertEquals(State.CONFIRMED, adventure.getState().getValue());
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

				HotelInterface.reserveRoom(Type.SINGLE, arrival, departure, BROKER_NIF_AS_BUYER, BROKER_IBAN);

				this.result = ROOM_CONFIRMATION;

				BankInterface.processPayment(CLIENT_IBAN, this.anyDouble);
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

		Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CONFIRMED, adventure.getState().getValue());
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

				CarInterface.rentCar((CarInterface.Type) this.any, this.anyString, this.anyString, this.anyString,
						(LocalDate) this.any, (LocalDate) this.any);
				this.result = RENTING_CONFIRMATION;

				BankInterface.processPayment(CLIENT_IBAN, this.anyDouble);
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

		Adventure adventure = new Adventure(this.broker, arrival, arrival, this.client, MARGIN, true);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CONFIRMED, adventure.getState().getValue());
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

				BankInterface.processPayment(CLIENT_IBAN, this.anyDouble);
				this.result = PAYMENT_CONFIRMATION;

				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.result = INVOICE_DATA;

				AdventureSequenceTest.this.activityReservationData.getPaymentReference();
				this.result = REFERENCE;

				AdventureSequenceTest.this.activityReservationData.getInvoiceReference();
				this.result = REFERENCE;
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, arrival, this.client, MARGIN);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CONFIRMED, adventure.getState().getValue());
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

		Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN);

		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState().getValue());
	}

	@Test
	public void unsuccessSequenceFailHotel(@Mocked TaxInterface taxInterface, @Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		// Testing: activity, fail hotel, undo, cancelled
		new Expectations() {
			{
				ActivityInterface.reserveActivity(arrival, departure, this.anyInt, this.anyString, this.anyString);
				this.result = ACTIVITY_CONFIRMATION;

				HotelInterface.reserveRoom(Type.SINGLE, arrival, departure, BROKER_NIF_AS_BUYER, BROKER_IBAN);
				this.result = new HotelException();

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState().getValue());
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

				CarInterface.rentCar((CarInterface.Type) this.any, this.anyString, this.anyString, this.anyString,
						(LocalDate) this.any, (LocalDate) this.any);
				this.result = new CarException();

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, arrival, this.client, MARGIN, true);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState().getValue());
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

				HotelInterface.reserveRoom(Type.SINGLE, arrival, departure, BROKER_NIF_AS_BUYER, BROKER_IBAN);
				this.result = ROOM_CONFIRMATION;

				CarInterface.rentCar((CarInterface.Type) this.any, this.anyString, this.anyString, this.anyString,
						(LocalDate) this.any, (LocalDate) this.any);
				this.result = RENTING_CONFIRMATION;

				BankInterface.processPayment(CLIENT_IBAN, this.anyDouble);
				this.result = new BankException();

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;

				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;

				CarInterface.cancelRenting(RENTING_CONFIRMATION);
				this.result = RENTING_CANCELLATION;
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN, true);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState().getValue());
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

				HotelInterface.reserveRoom(Type.SINGLE, arrival, departure, BROKER_NIF_AS_BUYER, BROKER_IBAN);
				this.result = ROOM_CONFIRMATION;

				CarInterface.rentCar(CarInterface.Type.CAR, this.anyString, this.anyString, this.anyString,
						(LocalDate) this.any, (LocalDate) this.any);
				this.result = RENTING_CONFIRMATION;

				BankInterface.processPayment(CLIENT_IBAN, this.anyDouble);
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

		Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN, true);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState().getValue());
	}

}