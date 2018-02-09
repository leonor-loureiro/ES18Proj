package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;

import static org.junit.Assert.assertEquals;

public class RoomBookingDataTest {
    private final LocalDate arrival = new LocalDate(2016, 12, 19);
    private final LocalDate departure = new LocalDate(2016, 12, 24);
    private Hotel hotel;
    private Room room;
    private Booking booking;

    @Before
    public void setUp() {
        this.hotel = new Hotel("XPTO123", "Lisboa");
        this.room = new Room(this.hotel, "01", Room.Type.SINGLE);
        this.booking = this.room.reserve(Room.Type.SINGLE, this.arrival, this.departure);
    }

    @Test
    public void GetsAndSetters() {
        RoomBookingData d0 = Hotel.getRoomBookingData(this.booking.getReference());
        RoomBookingData d1 = Hotel.getRoomBookingData(this.booking.getReference());

        d1.setArrival(d1.getArrival());
        d1.setCancellation(d1.getCancellation());
        d1.setCancellationDate(d1.getCancellationDate());
        d1.setDeparture(d1.getDeparture());
        d1.setHotelCode(d1.getHotelCode());
        d1.setHotelName(d1.getHotelName());
        d1.setReference(d1.getReference());
        d1.setRoomNumber(d1.getRoomNumber());
        d1.setRoomType(d1.getRoomType());

        assertEquals(d0.getArrival(), d1.getArrival());
        assertEquals(d0.getCancellation(), d1.getCancellation());
        assertEquals(d0.getCancellationDate(), d1.getCancellationDate());
        assertEquals(d0.getDeparture(), d1.getDeparture());
        assertEquals(d0.getHotelCode(), d1.getHotelCode());
        assertEquals(d0.getHotelName(), d1.getHotelName());
        assertEquals(d0.getReference(), d1.getReference());
        assertEquals(d0.getRoomNumber(), d1.getRoomNumber());
        assertEquals(d0.getRoomType(), d1.getRoomType());
    }


    @After
    public void tearDown() {
        Hotel.hotels.clear();
    }


}
