package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Delegate;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.car.domain.Car;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;

@RunWith(JMockit.class)
public class RentVehicleStateMethodTest {
	private static final String BROKER_IBAN = "BROKER_IBAN";
	private static final String NIF_AS_BUYER = "buyerNIF";
	private static final String NIF_AS_SELLER = "sellerNIF";
	private static final String NIF = "123456789";
	private static final String IBAN = "BK01987654321";
	private static final String IBAN_CUSTOMER = "BK01987654321";
	private static final String DRIVING_LICENSE = "IMT1234";
	private static final double MARGIN = 0.3;
	private static final int AGE = 20;
	private static final String RENT_CONFIRMATION = "1541";
	private static final LocalDate BEGIN = new LocalDate(2016, 12, 19);
	private static final LocalDate END = new LocalDate(2016, 12, 21);
	private Adventure adventure;

	@Mocked
	private TaxInterface taxInterface;

	@Before
	public void setUp() {
		Broker broker = new Broker("Br013", "HappyWeek", NIF_AS_SELLER, NIF_AS_BUYER, BROKER_IBAN);
		Client client = new Client(broker, IBAN, NIF, DRIVING_LICENSE, AGE);

		this.adventure = new Adventure(broker, BEGIN, END, client, MARGIN, true);
		this.adventure.setState(State.RENT_VEHICLE);
	}

	@Test
	public void successRentVehicle(@Mocked final CarInterface carInterface) {
		new Expectations() {
			{
				CarInterface.rentCar(Car.class, DRIVING_LICENSE, NIF_AS_BUYER, BROKER_IBAN, BEGIN, END);
				this.result = RENT_CONFIRMATION;
				this.times = 1;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.PROCESS_PAYMENT, this.adventure.getState());
	}

	@Test
	public void carException(@Mocked final CarInterface carInterface) {
		new Expectations() {
			{
				CarInterface.rentCar(Car.class, DRIVING_LICENSE, NIF_AS_BUYER, BROKER_IBAN, BEGIN, END);
				this.result = new CarException();
				this.times = 1;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void singleRemoteAccessException(@Mocked final CarInterface carInterface) {
		new Expectations() {
			{
				CarInterface.rentCar(Car.class, DRIVING_LICENSE, NIF_AS_BUYER, BROKER_IBAN, BEGIN, END);
				this.result = new RemoteAccessException();
				this.times = 1;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.RENT_VEHICLE, this.adventure.getState());
	}

	@Test
	public void maxRemoteAccessException(@Mocked final CarInterface carInterface) {
		new Expectations() {
			{
				CarInterface.rentCar(Car.class, DRIVING_LICENSE, NIF_AS_BUYER, BROKER_IBAN, BEGIN, END);
				this.result = new RemoteAccessException();
				this.times = RentVehicleState.MAX_REMOTE_ERRORS;
			}
		};

		for (int i = 0; i < RentVehicleState.MAX_REMOTE_ERRORS; i++) {
			this.adventure.process();
		}

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void maxMinusOneRemoteAccessException(@Mocked final CarInterface carInterface) {
		new Expectations() {
			{
				CarInterface.rentCar(Car.class, DRIVING_LICENSE, NIF_AS_BUYER, BROKER_IBAN, BEGIN, END);
				this.result = new RemoteAccessException();
				this.times = RentVehicleState.MAX_REMOTE_ERRORS - 1;
			}
		};

		for (int i = 0; i < RentVehicleState.MAX_REMOTE_ERRORS - 1; i++) {
			this.adventure.process();
		}

		Assert.assertEquals(State.RENT_VEHICLE, this.adventure.getState());
	}

	@Test
	public void twoRemoteAccessExceptionOneSuccess(@Mocked final CarInterface carInterface) {
		new Expectations() {
			{
				CarInterface.rentCar(Car.class, DRIVING_LICENSE, NIF_AS_BUYER, BROKER_IBAN, BEGIN, END);
				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 2) {
							this.i++;
							throw new RemoteAccessException();
						} else {
							return RENT_CONFIRMATION;
						}
					}
				};
				this.times = 3;
			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.PROCESS_PAYMENT, this.adventure.getState());
	}

	@Test
	public void oneRemoteAccessExceptionOneCarException(@Mocked final CarInterface carInterface) {
		new Expectations() {
			{
				CarInterface.rentCar(Car.class, DRIVING_LICENSE, NIF_AS_BUYER, BROKER_IBAN, BEGIN, END);
				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 1) {
							this.i++;
							throw new RemoteAccessException();
						} else {
							throw new CarException();
						}
					}
				};
				this.times = 2;
			}
		};

		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@After
	public void tearDown() {
		Broker.brokers.clear();
	}
}