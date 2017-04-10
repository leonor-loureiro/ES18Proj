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
import pt.ulisboa.tecnico.softeng.broker.services.remote.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.RemoteAccessException;

@RunWith(JMockit.class)
public class ProcessPaymentStateProcessMethodTest extends RollbackTestAbstractClass {
	private static final String IBAN = "BK01987654321";
	private static final int AMOUNT = 300;
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Adventure adventure;

	@Injectable
	private Broker broker;

	@Override
	public void populate4Test() {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, AMOUNT);
		this.adventure.setState(State.PROCESS_PAYMENT);
	}

	@Test
	public void success(@Mocked final BankInterface bankInterface) {
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.RESERVE_ACTIVITY, this.adventure.getState().getValue());
	}

	@Test
	public void bankException(@Mocked final BankInterface bankInterface) {
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = new BankException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState().getValue());
	}

	@Test
	public void singleRemoteAccessException(@Mocked final BankInterface bankInterface) {
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.PROCESS_PAYMENT, this.adventure.getState().getValue());
	}

	@Test
	public void maxRemoteAccessException(@Mocked final BankInterface bankInterface) {
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = new RemoteAccessException();
				this.times = ProcessPaymentState.MAX_REMOTE_ERRORS;
			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState().getValue());
	}

	@Test
	public void maxMinusOneRemoteAccessException(@Mocked final BankInterface bankInterface) {
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = new RemoteAccessException();
				this.times = ProcessPaymentState.MAX_REMOTE_ERRORS - 1;
			}
		};

		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.PROCESS_PAYMENT, this.adventure.getState().getValue());
	}

	@Test
	public void twoRemoteAccessExceptionOneSuccess(@Mocked final BankInterface bankInterface) {
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 2) {
							this.i++;
							throw new RemoteAccessException();
						} else {
							return PAYMENT_CONFIRMATION;
						}
					}
				};
				this.times = 3;

			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.RESERVE_ACTIVITY, this.adventure.getState().getValue());
	}

	@Test
	public void oneRemoteAccessExceptionOneBankException(@Mocked final BankInterface bankInterface) {
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);

				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 1) {
							this.i++;
							throw new RemoteAccessException();
						} else {
							throw new BankException();
						}
					}
				};
				this.times = 2;

			}
		};

		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState().getValue());
	}

}
