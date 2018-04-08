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
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;

@RunWith(JMockit.class)
public class ProcessPaymentStateProcessMethodTest {
	private static final String IBAN = "BK01987654321";
	private static final int MARGIN = 300;
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String INVOICE_LOG = "INVOICE";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Adventure adventure;
	private Client client;

	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
		this.client = new Client(broker, IBAN, "444444444", "A1", 20);
		this.adventure = new Adventure(this.broker, this.begin, this.end, this.client, MARGIN, true);
		this.adventure.setState(State.PROCESS_PAYMENT);
		

		adventure.setActivityConfirmation("act");
		adventure.setRoomConfirmation("room"); 
		adventure.setRentingConfirmation("rent");
	}

	@Test
	public void success(@Mocked final BankInterface bankInterface, @Mocked final TaxInterface taxInterface, 
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface,
			@Mocked final CarInterface carInterface) {
		
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, anyInt);
				this.result = PAYMENT_CONFIRMATION;
			
				ActivityInterface.getActivityReservationData(anyString).getAmount();
				HotelInterface.getRoomBookingData(anyString).getAmount();
				CarInterface.getRentingData(anyString).getAmount();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void bankException(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface,
			@Mocked final CarInterface carInterface) {
		
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, anyInt);
				this.result = new BankException();
			
				ActivityInterface.getActivityReservationData(anyString).getAmount();
				HotelInterface.getRoomBookingData(anyString).getAmount();
				CarInterface.getRentingData(anyString).getAmount();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void singleRemoteAccessException(@Mocked final BankInterface bankInterface,	
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface,
			@Mocked final CarInterface carInterface) {
		new Expectations() {
			{				
				ActivityInterface.getActivityReservationData(anyString).getAmount();
				HotelInterface.getRoomBookingData(anyString).getAmount();
				CarInterface.getRentingData(anyString).getAmount();

				BankInterface.processPayment(anyString, anyInt);
				this.result = new RemoteAccessException();

			}
		};

		this.adventure.process();

		Assert.assertEquals(State.PROCESS_PAYMENT, this.adventure.getState());
	}

	@Test
	public void maxRemoteAccessException(@Mocked final BankInterface bankInterface,			
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface,
			@Mocked final CarInterface carInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, anyInt);
				this.result = new RemoteAccessException();
				
				ActivityInterface.getActivityReservationData(anyString).getAmount();
				HotelInterface.getRoomBookingData(anyString).getAmount();
				CarInterface.getRentingData(anyString).getAmount();
			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void maxMinusOneRemoteAccessException(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface,
			@Mocked final CarInterface carInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, anyInt);
				this.result = new RemoteAccessException();
				
				ActivityInterface.getActivityReservationData(anyString).getAmount();
				HotelInterface.getRoomBookingData(anyString).getAmount();
				CarInterface.getRentingData(anyString).getAmount();
			}
		};

		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.PROCESS_PAYMENT, this.adventure.getState());
	}

	@Test
	public void twoRemoteAccessExceptionOneSuccess(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface,
			@Mocked final CarInterface carInterface) {
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(anyString).getAmount();
				HotelInterface.getRoomBookingData(anyString).getAmount();
				CarInterface.getRentingData(anyString).getAmount();
				
				BankInterface.processPayment(IBAN, anyInt);
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

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}
	

	@Test
	public void oneRemoteAccessExceptionOneBankException(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface,
			@Mocked final CarInterface carInterface) {
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(anyString).getAmount();
				HotelInterface.getRoomBookingData(anyString).getAmount();
				CarInterface.getRentingData(anyString).getAmount();
				
				BankInterface.processPayment(IBAN, anyInt);

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

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}	
}