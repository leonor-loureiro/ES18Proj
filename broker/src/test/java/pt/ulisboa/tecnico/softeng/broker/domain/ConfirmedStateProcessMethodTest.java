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
import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class ConfirmedStateProcessMethodTest {
	private static final String REFERENCE = "REFERENCE";
	private static final String BROKER_IBAN = "BROKER_IBAN";
	private static final String NIF_AS_BUYER = "buyerNIF";
	private static final String NIF_AS_SELLER = "sellerNIF";
	private static final String NIF = "123456789";
	private static final String IBAN = "BK01987654321";
	private static final String DRIVING_LICENSE = "IMT1234";
	private static final double MARGIN = 0.3;
	private static final int AGE = 20;
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String RENTING_CONFIRMATION = "rentingConfirmation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private static final LocalDate arrival = new LocalDate(2016, 12, 19);
	private static final LocalDate departure = new LocalDate(2016, 12, 21);
	private Adventure adventure;

	private Broker broker;
	private Client client;

	@Mocked
	private ActivityReservationData activityReservationData;
	@Mocked
	private RentingData rentingData;
	@Mocked
	private RoomBookingData roomBookingData;
	@Mocked
	private BankInterface bankInterface;
	@Mocked
	private ActivityInterface activityInterface;
	@Mocked
	private HotelInterface roomInterface;
	@Mocked
	private CarInterface carInterface;
	@Mocked
	private TaxInterface taxInterface;

	@Before
	public void setUp() {
		this.broker = new Broker("Br013", "HappyWeek", NIF_AS_SELLER, NIF_AS_BUYER, BROKER_IBAN);
		this.client = new Client(this.broker, IBAN, NIF, DRIVING_LICENSE, AGE);

		this.adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN);
		this.adventure.setState(State.CONFIRMED);
	}

	@Test
	public void successAll() {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRentingConfirmation(RENTING_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				CarInterface.getRentingData(RENTING_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);

				ConfirmedStateProcessMethodTest.this.activityReservationData.getPaymentReference();
				this.result = REFERENCE;

				ConfirmedStateProcessMethodTest.this.activityReservationData.getInvoiceReference();
				this.result = REFERENCE;

				ConfirmedStateProcessMethodTest.this.rentingData.getPaymentReference();
				this.result = REFERENCE;

				ConfirmedStateProcessMethodTest.this.rentingData.getInvoiceReference();
				this.result = REFERENCE;

				ConfirmedStateProcessMethodTest.this.roomBookingData.getPaymentReference();
				this.result = REFERENCE;

				ConfirmedStateProcessMethodTest.this.roomBookingData.getInvoiceReference();
				this.result = REFERENCE;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void successActivityAndHotel() {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);

				ConfirmedStateProcessMethodTest.this.activityReservationData.getPaymentReference();
				this.result = REFERENCE;

				ConfirmedStateProcessMethodTest.this.activityReservationData.getInvoiceReference();
				this.result = REFERENCE;

				ConfirmedStateProcessMethodTest.this.roomBookingData.getPaymentReference();
				this.result = REFERENCE;

				ConfirmedStateProcessMethodTest.this.roomBookingData.getInvoiceReference();
				this.result = REFERENCE;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void successActivityAndCar() {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRentingConfirmation(RENTING_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				CarInterface.getRentingData(RENTING_CONFIRMATION);

				ConfirmedStateProcessMethodTest.this.activityReservationData.getPaymentReference();
				this.result = REFERENCE;

				ConfirmedStateProcessMethodTest.this.activityReservationData.getInvoiceReference();
				this.result = REFERENCE;

				ConfirmedStateProcessMethodTest.this.rentingData.getPaymentReference();
				this.result = REFERENCE;

				ConfirmedStateProcessMethodTest.this.rentingData.getInvoiceReference();
				this.result = REFERENCE;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void successActivity() {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				ConfirmedStateProcessMethodTest.this.activityReservationData.getPaymentReference();
				this.result = REFERENCE;

				ConfirmedStateProcessMethodTest.this.activityReservationData.getInvoiceReference();
				this.result = REFERENCE;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void oneBankException() {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void maxBankException() {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
			}
		};

		for (int i = 0; i < ConfirmedState.MAX_BANK_EXCEPTIONS; i++) {
			this.adventure.process();
		}

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void maxMinusOneBankException() {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
			}
		};

		for (int i = 0; i < ConfirmedState.MAX_BANK_EXCEPTIONS - 1; i++) {
			this.adventure.process();
		}

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void oneRemoteAccessExceptionInPayment() {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void activityException() {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new ActivityException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void oneRemoteAccessExceptionInActivity() {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void activityNoPaymentConfirmation() {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				ConfirmedStateProcessMethodTest.this.activityReservationData.getPaymentReference();
				this.result = null;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void activityNoInvoiceReference() {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				ConfirmedStateProcessMethodTest.this.activityReservationData.getPaymentReference();
				this.result = REFERENCE;

				ConfirmedStateProcessMethodTest.this.activityReservationData.getInvoiceReference();
				this.result = null;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void carException() {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRentingConfirmation(RENTING_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				ConfirmedStateProcessMethodTest.this.activityReservationData.getPaymentReference();
				this.result = REFERENCE;

				ConfirmedStateProcessMethodTest.this.activityReservationData.getInvoiceReference();
				this.result = REFERENCE;

				CarInterface.getRentingData(RENTING_CONFIRMATION);
				this.result = new CarException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void oneRemoteExceptionInCar() {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRentingConfirmation(RENTING_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				ConfirmedStateProcessMethodTest.this.activityReservationData.getPaymentReference();
				this.result = REFERENCE;

				ConfirmedStateProcessMethodTest.this.activityReservationData.getInvoiceReference();
				this.result = REFERENCE;

				CarInterface.getRentingData(RENTING_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void carNoPaymentConfirmation() {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRentingConfirmation(RENTING_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				ConfirmedStateProcessMethodTest.this.activityReservationData.getPaymentReference();
				this.result = REFERENCE;

				ConfirmedStateProcessMethodTest.this.activityReservationData.getInvoiceReference();
				this.result = REFERENCE;

				CarInterface.getRentingData(RENTING_CONFIRMATION);

				ConfirmedStateProcessMethodTest.this.rentingData.getPaymentReference();
				this.result = null;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void carNoInvoiceReference() {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRentingConfirmation(RENTING_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				ConfirmedStateProcessMethodTest.this.activityReservationData.getPaymentReference();
				this.result = REFERENCE;

				ConfirmedStateProcessMethodTest.this.activityReservationData.getInvoiceReference();
				this.result = REFERENCE;

				CarInterface.getRentingData(RENTING_CONFIRMATION);

				ConfirmedStateProcessMethodTest.this.rentingData.getPaymentReference();
				this.result = REFERENCE;

				ConfirmedStateProcessMethodTest.this.rentingData.getInvoiceReference();
				this.result = null;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void hotelException() {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				ConfirmedStateProcessMethodTest.this.activityReservationData.getPaymentReference();
				this.result = REFERENCE;

				ConfirmedStateProcessMethodTest.this.activityReservationData.getInvoiceReference();
				this.result = REFERENCE;

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = new HotelException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void oneRemoteAccessExceptionInHotel() {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				ConfirmedStateProcessMethodTest.this.activityReservationData.getPaymentReference();
				this.result = REFERENCE;

				ConfirmedStateProcessMethodTest.this.activityReservationData.getInvoiceReference();
				this.result = REFERENCE;

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void hotelNoPaymentConfirmation() {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				ConfirmedStateProcessMethodTest.this.activityReservationData.getPaymentReference();
				this.result = REFERENCE;

				ConfirmedStateProcessMethodTest.this.activityReservationData.getInvoiceReference();
				this.result = REFERENCE;

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);

				ConfirmedStateProcessMethodTest.this.roomBookingData.getPaymentReference();
				this.result = null;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void hotelNoInvoiceReference() {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				ConfirmedStateProcessMethodTest.this.activityReservationData.getPaymentReference();
				this.result = REFERENCE;

				ConfirmedStateProcessMethodTest.this.activityReservationData.getInvoiceReference();
				this.result = REFERENCE;

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);

				ConfirmedStateProcessMethodTest.this.roomBookingData.getPaymentReference();
				this.result = REFERENCE;

				ConfirmedStateProcessMethodTest.this.roomBookingData.getInvoiceReference();
				this.result = null;
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
