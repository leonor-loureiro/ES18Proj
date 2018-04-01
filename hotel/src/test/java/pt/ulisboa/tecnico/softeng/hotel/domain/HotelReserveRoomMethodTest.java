package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

import static junit.framework.TestCase.assertTrue;

public class HotelReserveRoomMethodTest {
    private final LocalDate arrival = new LocalDate(2016, 12, 19);
    private final LocalDate departure = new LocalDate(2016, 12, 24);
    private Room room;
    private Hotel hotel;

	private static final String NIF = "123456789"; // novo
	private static final String IBAN = "ES061"; // novo
	private static final String clientNIF = "135792468"; // novo
	private static final String clientIBAN = "ES063"; // novo
    
    @Before
    public void setUp() {
        hotel = new Hotel("XPTO123", "Lisboa", NIF, IBAN); // novo
        this.room = new Room(hotel, "01", Room.Type.SINGLE);
    }

    @Test
    public void success() {
        String ref = Hotel.reserveRoom(Room.Type.SINGLE, arrival, departure, clientNIF, clientIBAN); // novo
        assertTrue(ref.startsWith("XPTO12"));
    }

    @Test(expected = HotelException.class)
    public void noHotels() {
        Hotel.hotels.clear();
        Hotel.reserveRoom(Room.Type.SINGLE, arrival, departure, clientNIF, clientIBAN); // novo
    }

    @Test(expected = HotelException.class)
    public void noVacancy() {
        hotel.removeRooms();
        String ref = Hotel.reserveRoom(Room.Type.SINGLE, arrival, new LocalDate(2016, 12, 25), clientNIF, clientIBAN); // novo
        System.out.println(ref);
    }

    @Test(expected = HotelException.class)
    public void noRooms() {
        hotel.removeRooms();
        Hotel.reserveRoom(Room.Type.SINGLE, arrival, new LocalDate(2016, 12, 25), clientNIF, clientIBAN); // novo
    }

    @After
    public void tearDown() {
        Hotel.hotels.clear();
    }

}