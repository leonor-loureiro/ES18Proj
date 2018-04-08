package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.car.domain.Car;
import pt.ulisboa.tecnico.softeng.car.domain.RentACar;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

@RunWith(JMockit.class)
public class AdventureProcessMethodTest {
	private static final String CODE = "BR01";
	private static final String NAME_BROKER = "eXtremeADVENTURE";
	private static final String NIF_AS_BUYER  = "111111111";
	private static final String NIF_AS_SELLER = "222222222";
	private static final String IBAN_BROKER = "BK011234567";
	
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private static final String RENT_CONFIRMATION = "RentConfirmation";
	
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private final String DR_L = "A1";
	private static final String NIF_OF_CLIENT = "333333333";
	
	private final boolean RENTV_T = true;
	private final boolean RENTV_F = false;
	private final int MARGIN   = 300;
	private final int GOOD_AGE = 20;
	
	private pt.ulisboa.tecnico.softeng.broker.domain.Client adClient;
	@Injectable
	private Broker broker;
	private String IBAN_CLIENT;

	@Before
	public void setUp() {
		this.broker = new Broker(CODE, NAME_BROKER, NIF_AS_SELLER, NIF_AS_BUYER, IBAN_BROKER);

		Bank bank = new Bank("Money", "BK01");
		Client client = new Client(bank, "António");
		Account account = new Account(bank, client);
		this.IBAN_CLIENT = account.getIBAN();
		this.adClient = new pt.ulisboa.tecnico.softeng.broker.domain.Client(this.broker,this.IBAN_CLIENT, NIF_OF_CLIENT, DR_L, GOOD_AGE);
		account.deposit(1000);

		Hotel hotel = new Hotel("XPTO123", "Paris", "123456789", "ES061", 10, 10);
		new Room(hotel, "01", Type.SINGLE);
		
		
		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure", "NIF", "IBAN");
		Activity activity = new Activity(provider, "Bush Walking", 18, 80, 10);
		new ActivityOffer(activity, this.begin, this.end, 30);
		new ActivityOffer(activity, this.begin, this.begin, 30);
	}

	@Test
	public void success(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface, @Mocked final CarInterface carInterface) {
		
		new Expectations() {
			{
				BankInterface.processPayment(anyString, MARGIN);
				this.result = PAYMENT_CONFIRMATION;

				ActivityInterface.reserveActivity(begin, end, anyInt, anyString, anyString);
				this.result = ACTIVITY_CONFIRMATION;

				HotelInterface.reserveRoom(Type.SINGLE, begin, end, anyString, anyString);
				this.result = ROOM_CONFIRMATION;
				
				CarInterface.rentVehicle(Car.class, anyString, begin, end, anyString, anyString);
				this.result = RENT_CONFIRMATION;
			}
		};
	
		
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, this.adClient, MARGIN, RENTV_T);

		adventure.process(); //reserveActivity
		adventure.process(); //reserveHotel
		adventure.process(); //reserveCar
		adventure.process(); //process payment
		adventure.process(); //tax payment
		
		assertEquals(Adventure.State.CONFIRMED, adventure.getState());
		assertNotNull(adventure.getPaymentConfirmation());
		assertNotNull(adventure.getRoomConfirmation());
		assertNotNull(adventure.getActivityConfirmation());
		assertNotNull(adventure.getRentingConfirmation());
	}

	@Test
	public void successNoHotelBooking() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.begin, this.adClient, MARGIN, RENTV_T);

		adventure.process(); //reserveActivity
		adventure.process(); //process payment
		adventure.process(); //tax payment

		assertEquals(Adventure.State.CONFIRMED, adventure.getState());
		assertNotNull(adventure.getPaymentConfirmation());	
		assertNotNull(adventure.getActivityConfirmation());
	}
	
	@Test
	public void successNoVehicleRenting(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface, @Mocked final CarInterface carInterface) {
		
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, this.adClient, MARGIN, RENTV_F);

		new Expectations() {
			{
				BankInterface.processPayment(anyString, MARGIN);
				this.result = PAYMENT_CONFIRMATION;

				ActivityInterface.reserveActivity(begin, end, anyInt, anyString, anyString);
				this.result = ACTIVITY_CONFIRMATION;

				HotelInterface.reserveRoom(Type.SINGLE, begin, end , anyString, anyString);
				this.result = ROOM_CONFIRMATION;
				
			}
		};
		
		
		adventure.process(); //reserveActivity
		adventure.process(); //reserveHotel
		adventure.process(); //process payment
		adventure.process(); //tax payment
		
		assertEquals(Adventure.State.CONFIRMED, adventure.getState());
		assertNotNull(adventure.getPaymentConfirmation());	
		assertNotNull(adventure.getActivityConfirmation());
		assertNotNull(adventure.getRoomConfirmation());
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
		Hotel.hotels.clear();
		ActivityProvider.providers.clear();
		Broker.brokers.clear();
		RentACar.rentACars.clear();
		
	}
	
	
}
