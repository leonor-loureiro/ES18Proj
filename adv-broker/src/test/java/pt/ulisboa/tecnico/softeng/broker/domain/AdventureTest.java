package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;

import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class AdventureTest {
    private final LocalDate begin = new LocalDate(2016, 12, 19);
    private final LocalDate end = new LocalDate(2016, 12, 21);
    private Broker broker;
    private String IBAN;

    @Before
    public void setUp() {
        this.broker = new Broker("BR01", "eXtremeADVENTURE");

        String randName = UUID.randomUUID().toString();

        Bank bank = new Bank("Bank", randName.substring(0,4));
        Client client = new Client(bank, "António");
        Account account = new Account(bank, client);
        this.IBAN = account.getIBAN();
        account.deposit(1000);

        Hotel hotel = new Hotel("XPTO123", "Paris");
        new Room(hotel, "01", Room.Type.SINGLE);

        ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure");
        Activity activity = new Activity(provider, "Bush Walking", 18, 80, 10);
        new ActivityOffer(activity, this.begin, this.end);
        new ActivityOffer(activity, this.begin, this.begin);
    }

    @Test
    public void GetsAndSetters() {
        Adventure adventure = new Adventure(this.broker, this.begin, this.begin, 20, this.IBAN, 300);

        String pcan = adventure.getPaymentCancellation();
        adventure.setPaymentCancellation(adventure.getPaymentCancellation());
        assertEquals(pcan, adventure.getPaymentCancellation());

        String aacn = adventure.getActivityCancellation();
        adventure.setActivityCancellation(adventure.getActivityCancellation());
        assertEquals(aacn, adventure.getActivityCancellation());

        String aconf = adventure.getActivityConfirmation();
        adventure.setActivityConfirmation(adventure.getActivityConfirmation());
        assertEquals(aconf, adventure.getActivityConfirmation());

        String rcan = adventure.getRoomCancellation();
        adventure.setRoomCancellation(adventure.getRoomCancellation());
        assertEquals(rcan, adventure.getRoomCancellation());

        assertTrue(adventure.getID().startsWith("BR"));
    }

    @Test
    public void setUndo() {
        Adventure adventure = new Adventure(this.broker, this.begin, this.begin, 20, this.IBAN, 300);
        adventure.setState(Adventure.State.UNDO);

        assertEquals(Adventure.State.UNDO, adventure.getState());
    }

    @Test
    public void activityCancellation() {
        Adventure adventure = new Adventure(this.broker, this.begin, this.begin, 20, this.IBAN, 300);
        assertFalse(adventure.cancelActivity());

        adventure.setActivityCancellation(null);
        assertFalse(adventure.cancelActivity());

        adventure.setActivityConfirmation("Cancel");
        assertTrue(adventure.cancelActivity());
    }

    @Test
    public void roomCancellation() {
        Adventure adventure = new Adventure(this.broker, this.begin, this.begin, 20, this.IBAN, 300);
        assertFalse(adventure.cancelRoom());

        adventure.setRoomCancellation(null);
        assertFalse(adventure.cancelRoom());

        adventure.setRoomConfirmation("Cancel");
        assertTrue(adventure.cancelRoom());
    }

    @Test
    public void paymentCancellation() {
        Adventure adventure = new Adventure(this.broker, this.begin, this.begin, 20, this.IBAN, 300);
        assertFalse(adventure.cancelPayment());

        adventure.setPaymentCancellation(null);
        assertFalse(adventure.cancelPayment());

        adventure.setPaymentConfirmation("Cancel");
        assertTrue(adventure.cancelPayment());
    }

    @After
    public void tearDown() {
        Hotel.hotels.clear();
        ActivityProvider.providers.clear();
        Broker.brokers.clear();
    }

}