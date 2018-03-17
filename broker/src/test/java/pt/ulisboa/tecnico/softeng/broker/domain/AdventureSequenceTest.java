package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.*;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.activity.domain.Booking;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.interfaces.*;
import pt.ulisboa.tecnico.softeng.car.domain.Vehicle;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;

@RunWith(JMockit.class)
public class AdventureSequenceTest {
    private static final String BROKER_IBAN = "BROKER_IBAN";
    private static final String NIF_AS_BUYER = "buyerNIF";
    private static final String NIF_AS_SELLER = "sellerNIF";
    private static final String NIF = "123456789";
    private static final String IBAN = "BK01987654321";
    private static final String DRIVING_LICENSE = "IMT1234";
    private static final double MARGIN = 0.3;
    private static final int AGE = 20;
    private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
    private static final String PAYMENT_CANCELLATION = "PaymentCancellation";
    private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
    private static final String ACTIVITY_CANCELLATION = "ActivityCancellation";
    private static final String ROOM_CONFIRMATION = "RoomConfirmation";
    private static final String ROOM_CANCELLATION = "RoomCancellation";
    private static final String RENTING_CONFIRMATION = "RentingConfirmation";
    private static final String RENTING_CANCELLATION = "RentingCancellation";
    private static final LocalDate arrival = new LocalDate(2016, 12, 19);
    private static final LocalDate departure = new LocalDate(2016, 12, 21);

    private Broker broker;
    private Client client;

    private static ActivityProvider aprov;
    private static ActivityOffer offer;
    private static Booking book;
    
    @Before
    public void setUp() {
        this.broker = new Broker("Br013", "HappyWeek", NIF_AS_SELLER, NIF_AS_BUYER, BROKER_IBAN);
        this.client = new Client(this.broker, IBAN, NIF, DRIVING_LICENSE, AGE);
    }

    @Test
    public void successSequenceOneWithCar(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface,
                                   @Mocked final ActivityInterface activityInterface,
                                   @Mocked final HotelInterface roomInterface,
                                   @Mocked CarInterface carInterface) {
        // Testing: book activity, hotel, car, pay, confirm
        new Expectations() {
            {

                ActivityInterface.reserveActivity(arrival, departure, AGE, this.anyString, this.anyString);
                this.result = ACTIVITY_CONFIRMATION;

                HotelInterface.reserveRoom(Type.SINGLE, arrival, departure);
                this.result = ROOM_CONFIRMATION;

                ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

                HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);

                CarInterface.rentCar((Class<? extends Vehicle>)any, anyString, anyString, (LocalDate)any, (LocalDate)any);

                BankInterface.processPayment(IBAN, anyDouble);
                this.result = PAYMENT_CONFIRMATION;
            }
        };

        Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN, true);

        adventure.process();
        adventure.process();
        adventure.process();
        adventure.process();
        adventure.process();

        Assert.assertEquals(State.CONFIRMED, adventure.getState());
    }

    @Test
    public void successSequenceOneNoCar(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface,
                                          @Mocked final ActivityInterface activityInterface,
                                          @Mocked final HotelInterface roomInterface,
                                          @Mocked CarInterface carInterface) {
        // Testing: book activity, hotel, car, pay, confirm
        new Expectations() {
            {

                ActivityInterface.reserveActivity(arrival, departure, AGE, this.anyString, this.anyString);
                this.result = ACTIVITY_CONFIRMATION;

                HotelInterface.reserveRoom(Type.SINGLE, arrival, departure);
                this.result = ROOM_CONFIRMATION;

                ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

                HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);

                BankInterface.processPayment(IBAN, anyDouble);
                this.result = PAYMENT_CONFIRMATION;
            }
        };

        Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN);

        adventure.process();
        adventure.process();
        adventure.process();
        adventure.process();

        Assert.assertEquals(State.CONFIRMED, adventure.getState());
    }

    @Test
    public void successSequenceTwoWithCar(@Mocked final TaxInterface taxInterface,
                                   @Mocked final BankInterface bankInterface,
                                   @Mocked final ActivityInterface activityInterface,
                                   @Mocked final HotelInterface roomInterface,
                                   @Mocked CarInterface carInterface) {

        // Testing: book activity, car, pay, confirm
        new Expectations() {
            {
                ActivityInterface.reserveActivity(arrival, arrival, AGE, this.anyString, this.anyString);
                this.result = ACTIVITY_CONFIRMATION;

                ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

                CarInterface.rentCar((Class<? extends Vehicle>)any, anyString, anyString, (LocalDate)any, (LocalDate)any);

                BankInterface.processPayment(IBAN, anyDouble);
                this.result = PAYMENT_CONFIRMATION;
            }
        };

        Adventure adventure = new Adventure(this.broker, arrival, arrival, this.client, MARGIN, true);

        adventure.process();
        adventure.process();
        adventure.process();

        Assert.assertEquals(State.CONFIRMED, adventure.getState());
    }

    @Test
    public void successSequenceTwoWithNoCar(@Mocked final TaxInterface taxInterface,
                                            @Mocked final BankInterface bankInterface,
                                            @Mocked final ActivityInterface activityInterface,
                                            @Mocked final HotelInterface roomInterface) {
        // Testing: book activity, pay, confirm
        new Expectations() {
            {
                ActivityInterface.reserveActivity(arrival, arrival, AGE, this.anyString, this.anyString);
                this.result = ACTIVITY_CONFIRMATION;

                ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
            }
        };

        Adventure adventure = new Adventure(this.broker, arrival, arrival, this.client, MARGIN);

        adventure.process();
        adventure.process();
        adventure.process();

        Assert.assertEquals(State.CONFIRMED, adventure.getState());
    }




    @Test
    public void unsuccessSequenceOne(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface,
                                     @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {

        //Testing: fail activity, undo
        new Expectations() {
            {

                ActivityInterface.reserveActivity(arrival, departure, AGE, this.anyString, this.anyString);
                this.result = new ActivityException();

            }
        };

        Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN);

        adventure.process();

        Assert.assertEquals(State.UNDO, adventure.getState());
    }

    @Test
    public void unsuccessSequenceTwo(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface,
                                     @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
        //Testing: fail activity, undo, cancelled
        new Expectations() {
            {

                ActivityInterface.reserveActivity(arrival, departure, AGE, this.anyString, this.anyString);
                this.result = new ActivityException();

            }
        };

        Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN);

        adventure.process();
        adventure.process();

        Assert.assertEquals(State.CANCELLED, adventure.getState());
    }

    @Test
    public void unsuccessSequenceThree(@Mocked TaxInterface taxInterface,
                                       @Mocked final BankInterface bankInterface,
                                       @Mocked final ActivityInterface activityInterface,
                                       @Mocked final HotelInterface roomInterface) {
        //Testing: activity, fail hotel, undo, cancelled
        new Expectations() {
            {

                ActivityInterface.reserveActivity(arrival, departure, this.anyInt, this.anyString, this.anyString);
                this.result = ACTIVITY_CONFIRMATION;

                HotelInterface.reserveRoom(Type.SINGLE, arrival, departure);
                this.result = new HotelException();


                ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
                this.result = ACTIVITY_CANCELLATION;
            }
        };

        Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN);

        adventure.process();
        adventure.process();
        adventure.process();
        adventure.process();

        Assert.assertEquals(State.CANCELLED, adventure.getState());
    }

    @Test
    public void unsuccessSequenceFour(@Mocked final TaxInterface taxInterface,
                                      @Mocked final BankInterface bankInterface,
                                      @Mocked final ActivityInterface activityInterface,
                                      @Mocked final HotelInterface roomInterface,
                                      @Mocked final CarInterface carInterface) {
        //Testing: activity, fail car, undo, cancelled
        new Expectations() {
            {

                ActivityInterface.reserveActivity(arrival, arrival, AGE, this.anyString, this.anyString);
                this.result = ACTIVITY_CONFIRMATION;

                CarInterface.rentCar((Class<? extends Vehicle>)any, anyString, anyString, (LocalDate)any, (LocalDate)any);
                this.result = new CarException();

                ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
                this.result = ACTIVITY_CANCELLATION;
            }
        };

        Adventure adventure = new Adventure(this.broker, arrival, arrival, this.client, MARGIN, true);

        adventure.process();
        adventure.process();
        adventure.process();
        for (int i = 0; i < ConfirmedState.MAX_BANK_EXCEPTIONS; i++) {
            adventure.process();
        }
        adventure.process();

        Assert.assertEquals(State.CANCELLED, adventure.getState());
    }


    @Test
    public void unsuccessSequenceFive(@Mocked TaxInterface taxInterface,
                                      @Mocked final BankInterface bankInterface,
                                      @Mocked final ActivityInterface activityInterface,
                                      @Mocked final HotelInterface roomInterface,
                                      @Mocked final CarInterface carInterface) {
        //Testing: activity, room, fail car, undo, cancelled
        new Expectations() {
            {

                ActivityInterface.reserveActivity(arrival, departure, AGE, this.anyString, this.anyString);
                this.result = ACTIVITY_CONFIRMATION;

                HotelInterface.reserveRoom(Type.SINGLE, arrival, departure);
                this.result = ROOM_CONFIRMATION;

                CarInterface.rentCar((Class<? extends Vehicle>)any, anyString, anyString, (LocalDate)any, (LocalDate)any);
                this.result = new CarException();

                ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
                this.result = ACTIVITY_CANCELLATION;

                HotelInterface.cancelBooking(ROOM_CONFIRMATION);
                this.result = ROOM_CANCELLATION;
            }
        };

        Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN, true);

        adventure.process();
        adventure.process();
        adventure.process();
        adventure.process();

        Assert.assertEquals(State.CANCELLED, adventure.getState());
    }

    @Test
    public void unsuccessSequenceSix(@Mocked TaxInterface taxInterface,
                                      @Mocked final BankInterface bankInterface,
                                      @Mocked final ActivityInterface activityInterface,
                                      @Mocked final HotelInterface roomInterface,
                                      @Mocked final CarInterface carInterface) {
        //Testing: activity, room, fail car, fail payment, undo, cancelled
        new Expectations() {
            {

                ActivityInterface.reserveActivity(arrival, departure, AGE, this.anyString, this.anyString);
                this.result = ACTIVITY_CONFIRMATION;

                HotelInterface.reserveRoom(Type.SINGLE, arrival, departure);
                this.result = ROOM_CONFIRMATION;

                CarInterface.rentCar((Class<? extends Vehicle>)any, anyString, anyString, (LocalDate)any, (LocalDate)any);
                this.result = RENTING_CONFIRMATION;

                BankInterface.processPayment(IBAN, anyDouble);
                this.result = new BankException();

                ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
                this.result = ACTIVITY_CANCELLATION;

                HotelInterface.cancelBooking(ROOM_CONFIRMATION);
                this.result = ROOM_CANCELLATION;

                CarInterface.cancelRenting(RENTING_CANCELLATION);
                this.result = RENTING_CANCELLATION;
            }
        };

        Adventure adventure = new Adventure(this.broker, arrival, departure, this.client, MARGIN, true);

        adventure.process();
        adventure.process();
        adventure.process();
        adventure.process();
        adventure.process();

        Assert.assertEquals(State.CANCELLED, adventure.getState());
    }

    @After
    public void tearDown() {
        Broker.brokers.clear();
    }
}