package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentingTest {
    private static final String PLATE_CAR = "22-33-HZ";
    private static final String DRIVING_LICENSE = "112233";
    private static final LocalDate date1 = LocalDate.parse("2018-01-06");
    private static final LocalDate date2 = LocalDate.parse("2018-01-07");
    private static final LocalDate date3 = LocalDate.parse("2018-01-08");
    private static final LocalDate date4 = LocalDate.parse("2018-01-09");

    @Test
    public void success() {
        RentACar rentACar = new RentACar("Eartz");
        Vehicle car = new Car(PLATE_CAR, 10, rentACar);
        Renting renting = new Renting(DRIVING_LICENSE, date1, date2, car); 
        
        assertEquals(DRIVING_LICENSE, renting.getDrivingLicense());
    }
    
    @Test(expected = CarException.class)
    public void endBeforeBegin() {
        RentACar rentACar = new RentACar("Eartz");
        Vehicle car = new Car(PLATE_CAR, 10, rentACar);
        new Renting(DRIVING_LICENSE, date2, date1, car); 
    }

    @Test(expected = CarException.class)
    public void noDrivingLicense() {
        RentACar rentACar = new RentACar("Eartz");
        Vehicle car = new Car(PLATE_CAR, 10, rentACar);
        new Renting(null, date1, date2, car); 
    }

    @Test(expected = CarException.class)
    public void noCar() {
        new Renting(DRIVING_LICENSE, date1, date2, null); 
    }

    @Test
    public void checkout() {
        RentACar rentACar = new RentACar("Eartz");
        Vehicle car = new Car(PLATE_CAR, 10, rentACar);
        Renting renting = new Renting(DRIVING_LICENSE, date1, date2, car); 
        renting.checkout(100);
        assertEquals(110, car.getKilometers());
    }
    
    @Test()
    public void checkAvailability() {
        RentACar rentACar = new RentACar("Eartz");
        Vehicle car = new Car(PLATE_CAR, 10, rentACar);
        Renting renting = new Renting(DRIVING_LICENSE, date1, date2, car);
        assertFalse(renting.conflict(date3, date4));
        assertFalse(renting.conflict(date3, date3));
        assertTrue(renting.conflict(date2, date3));
        assertTrue(renting.conflict(date1, date1));
    }
    
    @Test(expected = CarException.class)
    public void endBeforeBeginCheckAvailability() {
        RentACar rentACar = new RentACar("Eartz");
        Vehicle car = new Car(PLATE_CAR, 10, rentACar);
        Renting renting = new Renting(DRIVING_LICENSE, date1, date2, car);
        renting.conflict(date2, date1);
    }

    @After
    public void tearDown() {
        RentACar.rentACars.clear();
        Vehicle.vehicles.clear();
    }

}
