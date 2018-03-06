package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelReserveRoomMethodTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private Hotel hotel;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Paris");
		Room room = new Room(this.hotel, "1", Type.SINGLE);
	}

	@Test
	public void success() {
		String reference = Hotel.reserveRoom(Type.SINGLE, arrival, departure);

		Assert.assertNotNull(reference);
	}
	
	@Test(expected = HotelException.class)
	public void roomUnavailable() {
		String reference = Hotel.reserveRoom(Type.SINGLE, arrival, departure);
		String newReference = Hotel.reserveRoom(Type.SINGLE, arrival, departure);
	}
	
	@Test(expected = HotelException.class)
	public void roomTypeNotOffered() {
		String reference = Hotel.reserveRoom(Type.DOUBLE, arrival, departure);		
	}
	
	@Test(expected = HotelException.class)
	public void nullType() {
		Hotel.reserveRoom(null,arrival, departure);
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
}
