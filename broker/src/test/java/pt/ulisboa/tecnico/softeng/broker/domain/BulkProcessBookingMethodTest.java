package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.domain.Booking;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;


public class BulkProcessBookingMethodTest {
	
	private int numberOfRooms;
	private LocalDate arrival = new LocalDate(2018, 3, 5);
	private LocalDate departure = new LocalDate(2018, 3, 15);
	private BulkRoomBooking bulk;
	private Hotel h1;
	private Room r1;
	private Room r2;
	
	@Before
	public void setUp() {
		h1 = new Hotel("Hotel01", "Angola Hotel");
		r1 = new Room(h1, "1", Room.Type.SINGLE);
		r2 = new Room(h1, "2", Room.Type.SINGLE);
	}
	
	@Test
	public void successFewRooms() {
		numberOfRooms = 1;
		bulk = new BulkRoomBooking(numberOfRooms, arrival, departure);
		
		//Gets all bookings made
		bulk.processBooking();

		Set<String> result = new HashSet<>();
		result.addAll(bulk.getReferences());
		
		//Checks if each room has a booking
		boolean hasReferenceR1 = false;
		boolean hasReferenceR2 = false;
		
		for (String ref : result) {
			if (r1.getBooking(ref) != null) {
				hasReferenceR1 = true;
			}
			if (r2.getBooking(ref) != null){
				hasReferenceR2 = true;
			}
		}
		
		Assert.assertTrue(result.size() == 1); //Must be made 2 Bookings
		Assert.assertTrue(hasReferenceR1 || hasReferenceR2); //Room1 must have a reference
		
	}
	
	@Test
	public void successAllRooms() {
		
		numberOfRooms = 2;
		bulk = new BulkRoomBooking(numberOfRooms, arrival, departure);
		
		bulk.processBooking();

		Set<String> result = new HashSet<>();
		result.addAll(bulk.getReferences());
		
		//Checks if each room has a booking
		boolean hasReferenceR1 = false;
		boolean hasReferenceR2 = false;
		
		for (String ref : result) {
			if (r1.getBooking(ref) != null) {
				hasReferenceR1 = true;
			}
			if (r2.getBooking(ref) != null){
				hasReferenceR2 = true;
			}
		}
		
		Assert.assertTrue(result.size() == 2); //Must be made 2 Bookings
		Assert.assertTrue(hasReferenceR1); //Room1 must have a reference
		Assert.assertTrue(hasReferenceR2); //Room2 must have a reference		
		
	}
	
	@Test 
	public void failTooManyBooked() {
		numberOfRooms = 3;
		bulk = new BulkRoomBooking(numberOfRooms, arrival, departure);
		
		bulk.processBooking();
		
		Assert.assertTrue(bulk.getReferences().size() == 0);
		Assert.assertTrue(bulk.getReferences().equals(new HashSet<String>()));
		
	}
	
	@Test
	public void processBookingCanceled() {
		
		numberOfRooms = 3; //To generate HotelExceptions
		bulk = new BulkRoomBooking(numberOfRooms, arrival, departure);
		
		for(int i = 0; i < BulkRoomBooking.MAX_HOTEL_EXCEPTIONS; i++) {
			bulk.processBooking();
		}
		bulk.processBooking(); //Does MAX_HOTEL_EXCEPTIONS + 1 processes to changed bulk's state to cancelled
		
		Assert.assertTrue(bulk.getReferences().equals(new HashSet<String>())); //References must be empty
	}
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
}




