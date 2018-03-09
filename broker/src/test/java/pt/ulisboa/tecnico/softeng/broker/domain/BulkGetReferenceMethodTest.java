package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.joda.time.LocalDate;

import java.util.HashSet;
import java.util.Set;

public class BulkGetReferenceMethodTest {

	private static int numberOfRooms = 4;
	private static final LocalDate arrival = new LocalDate(2018, 3, 5);
	private static final LocalDate departure = new LocalDate(2018, 3, 5);
	private Hotel h1;
	private Room r1;
	private Room r2;
	private Hotel h2;
	private Room ra;
	private Room rb;
	private BulkRoomBooking bulk;

	@Before
	public void setUp() {
		h1 = new Hotel("Hotel01", "Angola Hotel");
		r1 = new Room(h1, "1", Room.Type.SINGLE);
		r2 = new Room(h1, "2", Room.Type.SINGLE);
		
		h2 = new Hotel("Hotel02", "S.Roque do Pico Hotel");
		ra = new Room(h2, "1", Room.Type.DOUBLE);
		rb = new Room(h2, "2", Room.Type.DOUBLE);
				
		bulk = new BulkRoomBooking(numberOfRooms, arrival, departure);

	}

	@Test
	public void successWithRooms() {
		bulk.processBooking();
		
		String refSingle1 = bulk.getReference(Room.Type.SINGLE.name());
		String refSingle2 = bulk.getReference(Room.Type.SINGLE.name());
		String refSingle3 = bulk.getReference(Room.Type.SINGLE.name());
		
		String refDouble1 = bulk.getReference(Room.Type.DOUBLE.name());
		String refDouble2 = bulk.getReference(Room.Type.DOUBLE.name());
		String refDouble3 = bulk.getReference(Room.Type.DOUBLE.name());
		
		Assert.assertNotNull(refSingle1);
		Assert.assertNotNull(refSingle2);
		Assert.assertNull(refSingle3);
		
		Assert.assertNotNull(refDouble1);
		Assert.assertNotNull(refDouble2);
		Assert.assertNull(refDouble3);
	}
	
	@Test
	public void nullArgument() {
		bulk.processBooking();
		
		Assert.assertNull(bulk.getReference(null));		
	}
	

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}