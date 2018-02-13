package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BulkRoomBookingTest {

    private final LocalDate begin = new LocalDate(2016, 12, 19);
    private final LocalDate end = new LocalDate(2016, 12, 21);

    private Broker broker;

    @Before
    public void setUp() {
        Broker.brokers.clear();

        broker = new Broker("BR01", "WeExplore");
    }

    @Test
    public void success() {
        BulkRoomBooking bulk = new BulkRoomBooking(2, begin, end);

        assertEquals(0, bulk.getReferences().size());
        assertEquals(2, bulk.getNumber());
        assertEquals(begin, bulk.getArrival());
        assertEquals(end, bulk.getDeparture());
    }

    @Test
    public void processBulkBooking() {
        BulkRoomBooking bulk = new BulkRoomBooking(2, begin, end);

        bulk.processBooking();

        assertEquals(0, bulk.getReferences().size());
        assertEquals(2, bulk.getNumber());
        assertEquals(begin, bulk.getArrival());
        assertEquals(end, bulk.getDeparture());
    }

    @Test
    public void getRefBooking() {
        BulkRoomBooking bulk = new BulkRoomBooking(2, begin, end);

        bulk.processBooking();

        assertEquals(0, bulk.getReferences().size());
        assertEquals(2, bulk.getNumber());
        assertEquals(begin, bulk.getArrival());
        assertEquals(end, bulk.getDeparture());
        assertNull(bulk.getReference("CR7"));
    }
}
