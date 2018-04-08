package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertEquals;

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
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class AdventureUpdateAmountMethodTest {
	private static final String IBAN = "BK01987654321";
	private static final int MARGIN = 300;
	private static final int AGE = 20;
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private static final String RENT_CONFIRMATION = "RentingConfirmation";
	private static final LocalDate arrival = new LocalDate(2016, 12, 19);
	private static final LocalDate departure = new LocalDate(2016, 12, 21);
	private static final double ACTIVITY_AMOUNT = 10;
	private static final double ROOM_AMOUNT = 20;
	private static final double RENT_AMOUNT = 30;
	
	private Adventure adventure;
	private Client client;

	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
		this.client = new Client(broker, IBAN, "444444444", "A1", AGE);
		this.adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN, true);
	}
	
	@Test
	public void activityOnly(@Mocked final ActivityInterface activityInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION).getAmount();
				this.result = ACTIVITY_AMOUNT;
			}
			
		};
		
		this.adventure.updateAmount();
		assertEquals(ACTIVITY_AMOUNT*(1+MARGIN),this.adventure.getAmount(),0);
	}
	
	@Test
	public void hotelOnly(@Mocked final HotelInterface roomInterface) {
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new Expectations() {
			{
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION).getAmount();
				this.result = ROOM_AMOUNT;
			}
			
		};
		
		this.adventure.updateAmount();
		assertEquals(ROOM_AMOUNT*(1+MARGIN),this.adventure.getAmount(),0);
	}

	@Test
	public void rentOnly(@Mocked final CarInterface carInterface) {
		this.adventure.setRentingConfirmation(RENT_CONFIRMATION);
		
		new Expectations() {
			{
				CarInterface.getRentingData(RENT_CONFIRMATION).getAmount();
				this.result = RENT_AMOUNT;
			}
		};
		
		this.adventure.updateAmount();
		assertEquals(RENT_AMOUNT*(1+MARGIN),this.adventure.getAmount(),0);
	}
	
	@Test
	public void hotelAndActivity(@Mocked final HotelInterface roomInterface, @Mocked final ActivityInterface activityInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new Expectations() {
			{
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION).getAmount();
				this.result = ROOM_AMOUNT;
				
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION).getAmount();
				this.result = ACTIVITY_AMOUNT;
			}
			
		};
		
		this.adventure.updateAmount();
		assertEquals((ROOM_AMOUNT+ACTIVITY_AMOUNT)*(1+MARGIN),this.adventure.getAmount(),0);
	}
	
	@Test
	public void rentAndActivity(@Mocked final ActivityInterface activityInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setRentingConfirmation(RENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		
		new Expectations() {
			{
				CarInterface.getRentingData(RENT_CONFIRMATION).getAmount();
				this.result = RENT_AMOUNT;
				
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION).getAmount();
				this.result = ACTIVITY_AMOUNT;
			}
		};
		
		this.adventure.updateAmount();
		assertEquals((ACTIVITY_AMOUNT + RENT_AMOUNT) * (1 + MARGIN),this.adventure.getAmount(),0);
	}
	
	@Test
	public void rentAndHotel(@Mocked final HotelInterface hotelInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setRentingConfirmation(RENT_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new Expectations() {
			{
				CarInterface.getRentingData(RENT_CONFIRMATION).getAmount();
				this.result = RENT_AMOUNT;
				
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION).getAmount();
				this.result = ROOM_AMOUNT;
			}
		};
		
		this.adventure.updateAmount();
		assertEquals((ROOM_AMOUNT + RENT_AMOUNT) * (1 + MARGIN),this.adventure.getAmount(),0);
	}
	
	@Test
	public void all(@Mocked final HotelInterface hotelInterface, 
			@Mocked final CarInterface carInterface, @Mocked final ActivityInterface activityInterface) {
		this.adventure.setRentingConfirmation(RENT_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		
		new Expectations() {
			{
				CarInterface.getRentingData(RENT_CONFIRMATION).getAmount();
				this.result = RENT_AMOUNT;
				
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION).getAmount();
				this.result = ROOM_AMOUNT;
				
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION).getAmount();
				this.result = ACTIVITY_AMOUNT;
			}
		};
		
		this.adventure.updateAmount();
		assertEquals((ROOM_AMOUNT + RENT_AMOUNT + ACTIVITY_AMOUNT) * (1 + MARGIN),this.adventure.getAmount(),0);
	}
	
	

}