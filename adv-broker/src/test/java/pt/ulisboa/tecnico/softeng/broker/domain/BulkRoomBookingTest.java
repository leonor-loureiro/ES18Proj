package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BulkRoomBookingTest {

    private final LocalDate begin = new LocalDate(2016, 12, 19);
    private final LocalDate end = new LocalDate(2016, 12, 21);

    private Broker broker;

    @Before
    public void setUp() {
        Broker.brokers.clear();

        this.broker = new Broker("BR01", "WeExplore");
    }

    @Test
    public void success() {
        BulkRoomBooking bulk = new BulkRoomBooking(2, this.begin, this.end);

        assertEquals(0, bulk.getReferences().size());
        assertEquals(2, bulk.getNumber());
        assertEquals(this.begin, bulk.getArrival());
        assertEquals(this.end, bulk.getDeparture());
    }

    @Test
    public void processBulkBooking() {
        BulkRoomBooking bulk = new BulkRoomBooking(2, this.begin, this.end);

        bulk.processBooking();

        assertEquals(0, bulk.getReferences().size());
        assertEquals(2, bulk.getNumber());
        assertEquals(this.begin, bulk.getArrival());
        assertEquals(this.end, bulk.getDeparture());
    }

    @Test
    public void getRefBooking() {
        BulkRoomBooking bulk = new BulkRoomBooking(2, this.begin, this.end);

        bulk.processBooking();

        assertEquals(0, bulk.getReferences().size());
        assertEquals(2, bulk.getNumber());
        assertEquals(this.begin, bulk.getArrival());
        assertEquals(this.end, bulk.getDeparture());
        assertNull(bulk.getReference("CR7"));
    }

    @Test
    public void endBeforeBegin() {
        BulkRoomBooking bulk = new BulkRoomBooking(2, this.end, this.begin);

        bulk.processBooking();
        bulk.processBooking();
        bulk.processBooking();
        bulk.processBooking();

        assertEquals(0, bulk.getReferences().size());
    }

    @Test
    public void datesAreNull() {
        BulkRoomBooking bulk = new BulkRoomBooking(2, null, null);

        bulk.processBooking();

        assertEquals(0, bulk.getReferences().size());
    }

    @After
    public void tearDown() {
        Broker.brokers.clear();
    }
}