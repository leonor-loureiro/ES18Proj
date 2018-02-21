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

public class ProcessPaymentStateTest {

    private final LocalDate begin = new LocalDate(2016, 12, 19);
    private final LocalDate end = new LocalDate(2016, 12, 21);
    private Broker broker;
    private String IBAN;

    @Before
    public void setUp() {
        Broker.brokers.clear();
        Bank.banks.clear();
        ActivityProvider.providers.clear();

        this.broker = new Broker("BR01", "eXtremeADVENTURE");

        Bank bank = new Bank("Money", "BK01");
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
    public void noBanks() {
        Bank.banks.clear();

        Adventure adventure = new Adventure(this.broker, this.begin, this.begin, 20, this.IBAN, 300);
        adventure.process();

        assertEquals(Adventure.State.CANCELLED, adventure.getState());
    }

    @After
    public void tearDown() {
        Hotel.hotels.clear();
        ActivityProvider.providers.clear();
        Broker.brokers.clear();
    }