package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertNull;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

public class HotelHasVacancyMethodTest {
	private Hotel hotel;
	private Room room;
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Paris");
		this.room = new Room(this.hotel, "01", Type.DOUBLE);
	}

	@Test
	public void hasVacancy() {
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 21);

		Room room = this.hotel.hasVacancy(Type.DOUBLE, arrival, departure);

		Assert.assertNotNull(room);
		Assert.assertEquals("01", room.getNumber());
	}

	@Test
	public void noVacancy() {
		this.room.reserve(Type.DOUBLE, this.arrival, this.departure);

		assertNull(this.hotel.hasVacancy(Type.DOUBLE, this.arrival, this.departure));
	}

	@Test
	public void noVacancyEmptyRoomSet() {
		Hotel otherHotel = new Hotel("XPTO124", "Paris Germain");

		assertNull(otherHotel.hasVacancy(Type.DOUBLE, this.arrival, this.departure));
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
