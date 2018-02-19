package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;

public class VehicleTest {
    private static final String PLATE_CAR = "22-33-HZ";
    private static final String PLATE_MOTORCYCLE = "44-33-HZ";
    private static final String RENT_A_CAR_NAME = "Eartz";
    private static final String DRIVING_LICENSE = "123XYZ";

    @Test
    public void constructorSuccess() {
        RentACar rentACar = new RentACar(RENT_A_CAR_NAME);
        Vehicle car = new Car(PLATE_CAR, 10, rentACar);
        Vehicle motorcycle = new Motorcycle(PLATE_MOTORCYCLE, 10, rentACar);

        assertEquals(PLATE_CAR, car.getPlate());
        assertEquals(PLATE_MOTORCYCLE, motorcycle.getPlate());
    }
    
    @Test
    public void rent(){
    	RentACar rentACar = new RentACar(RENT_A_CAR_NAME);
        Vehicle car = new Car(PLATE_CAR, 10, rentACar);
        
        car.rent(DRIVING_LICENSE, LocalDate.parse("2018-01-06"), LocalDate.parse("2018-01-16"));
        assertEquals(
        	false,
        	car.isFree(LocalDate.parse("2018-01-06"), LocalDate.parse("2018-01-07"))
        );
    }

    @After
    public void tearDown() {
        RentACar.rentACars.clear();
        Vehicle.vehicles.clear();
    }

}
