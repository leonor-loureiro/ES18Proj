package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class AdventureSequenceTest {
	private static final String IBAN = "BK01987654321";
	private static final int MARGIN = 300;
	private static final int AGE = 20;
	private static final boolean RENTV_F = false;
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String PAYMENT_CANCELLATION = "PaymentCancellation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ACTIVITY_CANCELLATION = "ActivityCancellation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private static final String ROOM_CANCELLATION = "RoomCancellation";
	private static final LocalDate arrival = new LocalDate(2016, 12, 19);
	private static final LocalDate departure = new LocalDate(2016, 12, 21);
	private static final String NIF = "444444444";
	private static final String DR_L = "A1";
	private Client client;

	@Injectable
	private Broker broker;

	@Before
	public void setUp() {	
		this.client = new Client(this.broker, IBAN, NIF, DR_L, AGE);
	}
	
	@Test
	public void successSequenceOne(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, MARGIN);
				this.result = PAYMENT_CONFIRMATION;

				ActivityInterface.reserveActivity(arrival, departure, AGE);
				this.result = ACTIVITY_CONFIRMATION;

				HotelInterface.reserveRoom(Type.SINGLE, arrival, departure);
				this.result = ROOM_CONFIRMATION;

				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN, RENTV_F);

		adventure.process();  //reserveActivity
		adventure.process();  //reserveHotel
		adventure.process();  //payment
		adventure.process();  //confirm

		Assert.assertEquals(State.CONFIRMED, adventure.getState());
	}

	@Test
	public void successSequenceTwo(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, MARGIN);
				this.result = PAYMENT_CONFIRMATION;

				ActivityInterface.reserveActivity(arrival, arrival, AGE);
				this.result = ACTIVITY_CONFIRMATION;

				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, arrival, this.client, MARGIN, RENTV_F);

		adventure.process(); //reserveActivity
		adventure.process(); //payment
		adventure.process(); //confirmation

		Assert.assertEquals(State.CONFIRMED, adventure.getState());
	}

	//falha banco
	@Test
	public void unsuccessSequenceOne(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{

				ActivityInterface.reserveActivity(arrival, departure, AGE);
				this.result = ACTIVITY_CONFIRMATION;

				HotelInterface.reserveRoom(Type.SINGLE, arrival, departure);
				this.result = ROOM_CONFIRMATION;
				
				BankInterface.processPayment(IBAN, MARGIN);
				this.result = new BankException();
				
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
				
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN, RENTV_F);

		adventure.process(); //reserveActivity
		adventure.process(); //reserveHotel
		adventure.process(); //payment
		adventure.process(); //undo
		adventure.process(); //cancel

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}

	//falha activity
	@Test
	public void unsuccessSequenceTwo(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{

				ActivityInterface.reserveActivity(arrival, departure, AGE);
				this.result = new ActivityException();

			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN, RENTV_F);

		adventure.process(); //reserveActivity
		adventure.process(); //cancel

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}

	//falha hotel
	@Test
	public void unsuccessSequenceThree(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{


				ActivityInterface.reserveActivity(arrival, departure, AGE);
				this.result = ACTIVITY_CONFIRMATION;

				HotelInterface.reserveRoom(Type.SINGLE, arrival, departure);
				this.result = new HotelException();

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN, RENTV_F);

		adventure.process(); //reserveActivity
		adventure.process(); //reserveHotel
		adventure.process(); //undo
		adventure.process(); //cancel

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}


	@Test
	public void unsuccessSequenceFour(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, MARGIN);
				this.result = PAYMENT_CONFIRMATION;

				ActivityInterface.reserveActivity(arrival, departure, AGE);
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

		Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN, RENTV_F);

		adventure.process(); //reserveActivity
		adventure.process(); //reserveRoom
		adventure.process(); //payment
		for (int i = 0; i < ConfirmedState.MAX_BANK_EXCEPTIONS; i++) {
			adventure.process(); //confirm
		}
		adventure.process(); //cancel

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}

}