package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;

public class RentACarConstructorTest {
    private static final String NAME = "eartz";
    private static final String PLATE_CAR = "aa-00-11";
    private static final String DRIVING_LICENSE = "123xyz";

    @Test
    public void success() {
        RentACar rentACar = new RentACar(NAME);

        assertEquals(NAME, rentACar.getName());
    }
    
    @Test
    public void getRentingData() {
        RentACar rentACar = new RentACar("Eartz");
        Vehicle car = new Car(PLATE_CAR, 10, rentACar);
        Renting renting = new Renting(DRIVING_LICENSE, new LocalDate(), new LocalDate(), car);
        
        RentingData rentingData = RentACar.getRentingData(renting.getReference());
        assertEquals(renting.getReference(), rentingData.getReference());
        assertEquals(DRIVING_LICENSE, rentingData.getDrivingLicense());
        assertEquals(PLATE_CAR, rentingData.getPlate());
        assertEquals(rentACar.getCode(), rentingData.getRentACarCode());
    }
    
    

    @After
    public void tearDown() {
        RentACar.rentACars.clear();
    }

}
