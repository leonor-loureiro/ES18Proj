package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Delegate;
import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.services.remote.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.RemoteAccessException;

@RunWith(JMockit.class)
public class ReserveActivityStateProcessMethodTest extends RollbackTestAbstractClass {
	private static final String IBAN = "BK01987654321";
	private static final int AMOUNT = 300;
	private static final int AGE = 20;
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final LocalDate begin = new LocalDate(2016, 12, 19);
	private static final LocalDate end = new LocalDate(2016, 12, 21);
	private Adventure adventure;

	@Injectable
	private Broker broker;

	@Override
	public void populate4Test() {
		this.adventure = new Adventure(this.broker, begin, end, AGE, IBAN, AMOUNT);
		this.adventure.setState(State.RESERVE_ACTIVITY);
	}

	@Test
	public void successNoBookRoom(@Mocked final ActivityInterface activityInterface) {
		Adventure sameDayAdventure = new Adventure(this.broker, begin, begin, AGE, IBAN, AMOUNT);
		sameDayAdventure.setState(State.RESERVE_ACTIVITY);

		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity(begin, begin, AGE);
				this.result = ACTIVITY_CONFIRMATION;
			}
		};

		sameDayAdventure.process();

		Assert.assertEquals(State.CONFIRMED, sameDayAdventure.getState().getValue());
	}

	@Test
	public void successBookRoom(@Mocked final ActivityInterface activityInterface) {
		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity(begin, end, AGE);
				this.result = ACTIVITY_CONFIRMATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.BOOK_ROOM, this.adventure.getState().getValue());
	}

	@Test
	public void activityException(@Mocked final ActivityInterface activityInterface) {
		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity(begin, end, AGE);
				this.result = new ActivityException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState().getValue());
	}

	@Test
	public void singleRemoteAccessException(@Mocked final ActivityInterface activityInterface) {
		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity(begin, end, AGE);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.RESERVE_ACTIVITY, this.adventure.getState().getValue());
	}

	@Test
	public void maxRemoteAccessException(@Mocked final ActivityInterface activityInterface) {
		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity(begin, end, AGE);
				this.result = new RemoteAccessException();
				this.times = ReserveActivityState.MAX_REMOTE_ERRORS;
			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState().getValue());
	}

	@Test
	public void maxMinusOneRemoteAccessException(@Mocked final ActivityInterface activityInterface) {
		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity(begin, end, AGE);
				this.result = new RemoteAccessException();
				this.times = ReserveActivityState.MAX_REMOTE_ERRORS - 1;
			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.RESERVE_ACTIVITY, this.adventure.getState().getValue());
	}

	@Test
	public void twoRemoteAccessExceptionOneSuccess(@Mocked final ActivityInterface activityInterface) {
		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity(begin, end, AGE);
				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 2) {
							this.i++;
							throw new RemoteAccessException();
						} else {
							return ACTIVITY_CONFIRMATION;
						}
					}
				};
				this.times = 3;

			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.BOOK_ROOM, this.adventure.getState().getValue());
	}

	@Test
	public void oneRemoteAccessExceptionOneActivityException(@Mocked final ActivityInterface activityInterface) {
		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity(begin, end, AGE);
				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 1) {
							this.i++;
							throw new RemoteAccessException();
						} else {
							throw new ActivityException();
						}
					}
				};
				this.times = 2;
			}
		};

		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState().getValue());
	}
}
