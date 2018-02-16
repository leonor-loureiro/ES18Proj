package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;

public class VehicleConstructorTest {
    private static final String PLATE_CAR = "22-33-HZ";
    private static final String PLATE_MOTORCYCLE = "44-33-HZ";

    @Test
    public void success() {
        RentACar rentACar = new RentACar("Eartz");
        Vehicle car = new Car(PLATE_CAR, 10, rentACar);
        Vehicle motorcycle = new Motorcycle(PLATE_MOTORCYCLE, 10, rentACar);

        assertEquals(PLATE_CAR, car.getPlate());
        assertEquals(PLATE_MOTORCYCLE, motorcycle.getPlate());
    }

    @After
    public void tearDown() {
        RentACar.rentACars.clear();
        Vehicle.vehicles.clear();
    }

}
