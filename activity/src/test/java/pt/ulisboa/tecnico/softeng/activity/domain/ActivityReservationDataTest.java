package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ActivityReservationDataTest {

    private static final String NAME = "ExtremeAdventure";
    private static final String CODE = "XtremX";
    private final LocalDate begin = new LocalDate(2018, 12, 19);
    private final LocalDate end = new LocalDate(2018, 12, 21);
    private ActivityProvider provider;
    private ActivityOffer offer;
    private Booking booking;

    @Before
    public void setUp() {
        this.provider = new ActivityProvider(CODE, NAME);
        Activity activity = new Activity(this.provider, "Bush Walking", 18, 80, 3);

        this.offer = new ActivityOffer(activity, this.begin, this.end);
        this.booking = new Booking(this.provider, this.offer);
    }

    @Test
    public void GetsAndSetters() {
        ActivityReservationData data = ActivityProvider.getActivityReservationData(this.booking.getReference());

        data.setReference(data.getReference());
        data.setCancellation("CANCELED");
        data.setName(data.getName());
        data.setCode(data.getCode());
        data.setBegin(data.getBegin());
        data.setEnd(data.getEnd());

        assertEquals(this.booking.getReference(), data.getReference());
        assertEquals("CANCELED", data.getCancellation());
        assertEquals(NAME, data.getName());
        assertEquals(CODE, data.getCode());
        assertEquals(this.begin, data.getBegin());
        assertEquals(this.end, data.getEnd());
        assertNull(data.getCancellationDate());

        data.setCancellationDate(LocalDate.now());
        assertNotNull(data.getCancellationDate());

    }

    @After
    public void tearDown() {
        ActivityProvider.providers.clear();
    }


}
