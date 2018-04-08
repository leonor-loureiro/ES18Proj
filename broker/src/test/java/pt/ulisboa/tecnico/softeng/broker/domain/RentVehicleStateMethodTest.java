package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.car.domain.Car;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;

@RunWith(JMockit.class)
public class RentVehicleStateMethodTest {
	private static final String IBAN = "BK01987654321";
	private static final int MARGIN = 300;
	private static final int AGE = 20;
	private static final String RENT_CONFIRMATION = "RentConfirmation";
	private static final LocalDate arrival = new LocalDate(2016, 12, 19);
	private static final LocalDate departure = new LocalDate(2016, 12, 21);
	private static final String NIF = "444444444";
	private static final String DR_L = "A1";
	private static final boolean RENTV_T = true;
	private Adventure adventure;
	private Client client;

	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
		this.client = new Client(this.broker, IBAN, NIF, DR_L, AGE);
		this.adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN, RENTV_T);
		this.adventure.setState(State.RENT_VEHICLE);
	}

	@Test
	public void successRentVehicle(@Mocked final CarInterface carInterface) {
		new Expectations() {
			{
				CarInterface.rentVehicle(Car.class, anyString, arrival, departure, anyString, anyString);
				this.result = RENT_CONFIRMATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.PROCESS_PAYMENT, this.adventure.getState());
	}

	@Test
	public void carException(@Mocked final CarInterface carInterface) {
		new Expectations() {
			{
				CarInterface.rentVehicle(Car.class, anyString, arrival, departure, anyString, anyString);
				this.result = new CarException();
			}
		};

		this.adventure.process(); 
	

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void singleRemoteAccessException(@Mocked final CarInterface carInterface) {
		new Expectations() {
			{
				CarInterface.rentVehicle(Car.class, anyString, arrival, departure, anyString, anyString);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.RENT_VEHICLE, this.adventure.getState());
	}

	@Test
	public void maxRemoteAccessException(@Mocked final CarInterface carInterface) {
		new Expectations() {
			{
				CarInterface.rentVehicle(Car.class, anyString, arrival, departure, anyString, anyString);
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
				CarInterface.rentVehicle(Car.class, anyString, arrival, departure, anyString, anyString);
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
	public void threeRemoteAccessExceptionOneSuccess(@Mocked final CarInterface carInterface) {
		new Expectations() {
			{
				CarInterface.rentVehicle(Car.class, anyString, arrival, departure, anyString, anyString);
				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 3) {
							this.i++;
							throw new RemoteAccessException();
						} else {
							return RENT_CONFIRMATION;
						}
					}
				};
				this.times = 4;
			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();


		Assert.assertEquals(State.PROCESS_PAYMENT, this.adventure.getState());
	}

	@Test
	public void oneRemoteAccessExceptionOneCarException(@Mocked final CarInterface carInterface) {
		new Expectations() {
			{
				CarInterface.rentVehicle(Car.class, anyString, arrival, departure, anyString, anyString);
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

}