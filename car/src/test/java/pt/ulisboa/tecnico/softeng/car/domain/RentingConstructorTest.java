package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;

public class RentingConstructorTest {
    private static final String PLATE_CAR = "22-33-HZ";
    private static final String DRIVING_LICENSE = "112233";

    @Test
    public void success() {
        RentACar rentACar = new RentACar("Eartz");
        Vehicle car = new Car(PLATE_CAR, 10, rentACar);
        Renting renting = new Renting(DRIVING_LICENSE, new LocalDate(), new LocalDate(), car); 
        
        assertEquals(DRIVING_LICENSE, renting.getDrivingLicense());
    }

    @After
    public void tearDown() {
        RentACar.rentACars.clear();
        Vehicle.vehicles.clear();
    }

}
