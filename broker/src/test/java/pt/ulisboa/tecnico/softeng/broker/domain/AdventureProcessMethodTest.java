package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.car.domain.Car;
import pt.ulisboa.tecnico.softeng.car.domain.RentACar;
import pt.ulisboa.tecnico.softeng.car.domain.Vehicle;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

public class AdventureProcessMethodTest {
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private final String NIF = "444444444";
	private final String DR_L = "A1";
	
	private final boolean RENTV_F = false;
	private final int MARGIN   = 300;
	private final int GOOD_AGE = 20;
	
	private pt.ulisboa.tecnico.softeng.broker.domain.Client adClient;
	private Broker broker;
	private String IBAN;

	@Before
	public void setUp() {
		this.broker = new Broker("BR01", "eXtremeADVENTURE");

		Bank bank = new Bank("Money", "BK01");
		Client client = new Client(bank, "António");
		Account account = new Account(bank, client);
		this.IBAN = account.getIBAN();
		this.adClient = new pt.ulisboa.tecnico.softeng.broker.domain.Client(this.broker,this.IBAN, NIF, DR_L, GOOD_AGE);
		account.deposit(1000);

		Hotel hotel = new Hotel("XPTO123", "Paris", "123456789", "ES061", 10, 10);
		new Room(hotel, "01", Type.SINGLE);
		
		
		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure", "NIF", "IBAN");
		Activity activity = new Activity(provider, "Bush Walking", 18, 80, 10);
		new ActivityOffer(activity, this.begin, this.end, 30);
		new ActivityOffer(activity, this.begin, this.begin, 30);
	}

	@Test
	public void success() {
		
		RentACar rentACar = new RentACar("eartz","123456789","ES061");
		new Car("aa-00-11", 10, 50, rentACar);
		
		
		RentACar rentACar = new RentACar("eartz","123456789","ES061");
		new Car("aa-00-11", 10, 50, rentACar);
		
		
		RentACar rentACar = new RentACar("eartz","123456789","ES061");
		new Car("aa-00-11", 10, 50, rentACar);
		
		
		RentACar rentACar = new RentACar("eartz","123456789","ES061");
		new Car("aa-00-11", 10, 50, rentACar);
		
		
		RentACar rentACar = new RentACar("eartz","123456789","ES061");
		new Car("aa-00-11", 10, 50, rentACar);
		
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, this.adClient, MARGIN, RENTV_F);

		adventure.process(); //reserveActivity
		adventure.process(); //reserveHotel
		adventure.process(); //reserveCar
		adventure.process(); //payment

		assertEquals(Adventure.State.CONFIRMED, adventure.getState());
		assertNotNull(adventure.getPaymentConfirmation());
		assertNotNull(adventure.getRoomConfirmation());
		assertNotNull(adventure.getActivityConfirmation());
	}

	@Test
	public void successNoHotelBooking() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, this.adClient, MARGIN, RENTV_F);

		adventure.process(); //reserveActivity
		adventure.process(); //payment

		assertEquals(Adventure.State.CONFIRMED, adventure.getState());
		assertNotNull(adventure.getPaymentConfirmation());
		assertNotNull(adventure.getActivityConfirmation());
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
