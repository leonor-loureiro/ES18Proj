package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.services.remote.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.services.remote.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.services.remote.HotelInterface;
import pt.ulisboa.tecnico.softeng.broker.services.remote.HotelInterface.RoomType;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.HotelException;

@RunWith(JMockit.class)
public class AdventureSequenceTest extends RollbackTestAbstractClass {
	private static final String IBAN = "BK01987654321";
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

	@Injectable
	private Broker broker;

	@Override
	public void populate4Test() {
	}

	@Test
	public void successSequenceOne(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				ActivityInterface.reserveActivity(arrival, departure, AGE);
				this.result = ACTIVITY_CONFIRMATION;

				HotelInterface.reserveRoom(RoomType.SINGLE, arrival, departure);
				this.result = ROOM_CONFIRMATION;

				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, departure, AGE, IBAN, AMOUNT);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CONFIRMED, adventure.getState().getValue());
	}

	@Test
	public void successSequenceTwo(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				ActivityInterface.reserveActivity(arrival, arrival, AGE);
				this.result = ACTIVITY_CONFIRMATION;

				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, arrival, AGE, IBAN, AMOUNT);

		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CONFIRMED, adventure.getState().getValue());
	}

	@Test
	public void unsuccessSequenceOne(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = new BankException();
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, departure, AGE, IBAN, AMOUNT);

		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState().getValue());
	}

	@Test
	public void unsuccessSequenceTwo(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				ActivityInterface.reserveActivity(arrival, departure, AGE);
				this.result = new ActivityException();

				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, departure, AGE, IBAN, AMOUNT);

		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState().getValue());
	}

	@Test
	public void unsuccessSequenceThree(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				ActivityInterface.reserveActivity(arrival, departure, AGE);
				this.result = ACTIVITY_CONFIRMATION;

				HotelInterface.reserveRoom(RoomType.SINGLE, arrival, departure);
				this.result = new HotelException();

				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, departure, AGE, IBAN, AMOUNT);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState().getValue());
	}

	@Test
	public void unsuccessSequenceFour(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				ActivityInterface.reserveActivity(arrival, departure, AGE);
				this.result = ACTIVITY_CONFIRMATION;

				HotelInterface.reserveRoom(RoomType.SINGLE, arrival, departure);
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

		Adventure adventure = new Adventure(this.broker, arrival, departure, AGE, IBAN, AMOUNT);

		adventure.process();
		adventure.process();
		adventure.process();
		for (int i = 0; i < ConfirmedState.MAX_BANK_EXCEPTIONS; i++) {
			adventure.process();
		}
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState().getValue());
	}

}
