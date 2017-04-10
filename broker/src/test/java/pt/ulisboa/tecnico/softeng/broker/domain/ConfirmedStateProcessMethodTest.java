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
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.HotelException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.RemoteAccessException;

@RunWith(JMockit.class)
public class ConfirmedStateProcessMethodTest extends RollbackTestAbstractClass {
	private static final String IBAN = "BK01987654321";
	private static final int AMOUNT = 300;
	private static final int AGE = 20;
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private static final LocalDate arrival = new LocalDate(2016, 12, 19);
	private static final LocalDate departure = new LocalDate(2016, 12, 21);
	private Adventure adventure;

	@Injectable
	private Broker broker;

	@Override
	public void populate4Test() {
		this.adventure = new Adventure(this.broker, arrival, departure, AGE, IBAN, AMOUNT);
		this.adventure.setState(State.CONFIRMED);
	}

	@Test
	public void successAll(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState().getValue());
	}

	@Test
	public void successPaymentAndActivity(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState().getValue());
	}

	@Test
	public void oneBankException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState().getValue());
	}

	@Test
	public void maxBankException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
				this.times = ConfirmedState.MAX_BANK_EXCEPTIONS;
			}
		};

		for (int i = 0; i < ConfirmedState.MAX_BANK_EXCEPTIONS; i++) {
			this.adventure.process();
		}

		Assert.assertEquals(State.UNDO, this.adventure.getState().getValue());
	}

	@Test
	public void maxMinusOneBankException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
				this.times = ConfirmedState.MAX_BANK_EXCEPTIONS - 1;
			}
		};

		for (int i = 0; i < ConfirmedState.MAX_BANK_EXCEPTIONS - 1; i++) {
			this.adventure.process();
		}

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState().getValue());
	}

	@Test
	public void oneRemoteAccessExceptionStartingInPayment(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState().getValue());
	}

	@Test
	public void maxRemoteAccessExceptionStartingInPayment(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
				this.times = ConfirmedState.MAX_REMOTE_ERRORS;
			}
		};

		for (int i = 0; i < ConfirmedState.MAX_REMOTE_ERRORS; i++) {
			this.adventure.process();
		}

		Assert.assertEquals(State.UNDO, this.adventure.getState().getValue());
	}

	@Test
	public void maxMinusOneRemoteAccessExceptionStartingInPayment(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
				this.times = ConfirmedState.MAX_REMOTE_ERRORS - 1;
			}
		};

		for (int i = 0; i < ConfirmedState.MAX_REMOTE_ERRORS - 1; i++) {
			this.adventure.process();
		}

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState().getValue());
	}

	@Test
	public void oneRemoteAccessExceptionStartingInActivity(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState().getValue());
	}

	@Test
	public void maxRemoteAccessExceptionStartingInActivity(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new RemoteAccessException();

				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
				this.times = ConfirmedState.MAX_REMOTE_ERRORS - 1;
			}
		};

		for (int i = 0; i < ConfirmedState.MAX_REMOTE_ERRORS; i++) {
			this.adventure.process();
		}

		Assert.assertEquals(State.UNDO, this.adventure.getState().getValue());
	}

	@Test
	public void maxMinusOneRemoteAccessExceptionStartingInActivity(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new RemoteAccessException();

				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
				this.times = ConfirmedState.MAX_REMOTE_ERRORS - 2;
			}
		};

		for (int i = 0; i < ConfirmedState.MAX_REMOTE_ERRORS - 1; i++) {
			this.adventure.process();
		}

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState().getValue());
	}

	@Test
	public void oneRemoteAccessExceptionStartingInRoom(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState().getValue());
	}

	@Test
	public void maxRemoteAccessExceptionStartingInRoom(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = new RemoteAccessException();

				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
				this.times = ConfirmedState.MAX_REMOTE_ERRORS - 1;
			}
		};

		for (int i = 0; i < ConfirmedState.MAX_REMOTE_ERRORS; i++) {
			this.adventure.process();
		}

		Assert.assertEquals(State.UNDO, this.adventure.getState().getValue());
	}

	@Test
	public void maxMinusOneRemoteAccessExceptionStartingInRoom(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = new RemoteAccessException();

				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
				this.times = ConfirmedState.MAX_REMOTE_ERRORS - 2;
			}
		};

		for (int i = 0; i < ConfirmedState.MAX_REMOTE_ERRORS - 1; i++) {
			this.adventure.process();
		}

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState().getValue());
	}

	@Test
	public void activityException(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new ActivityException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState().getValue());
	}

	@Test
	public void hotelException(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = new HotelException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState().getValue());
	}

}
