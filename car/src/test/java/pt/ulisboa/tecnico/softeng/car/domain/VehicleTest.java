package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class VehicleTest {
    private static final String PLATE_CAR = "22-33-HZ";
    private static final String PLATE_MOTORCYCLE = "44-33-HZ";
    private static final String RENT_A_CAR_NAME = "Eartz";
    private static final String DRIVING_LICENSE = "123XYZ";
    private static final LocalDate date1 = LocalDate.parse("2018-01-06");
    private static final LocalDate date2 = LocalDate.parse("2018-01-07");
    private static final LocalDate date3 = LocalDate.parse("2018-01-08");
    private static final LocalDate date4 = LocalDate.parse("2018-01-09");

    @Test
    public void constructorSuccess() {
        RentACar rentACar = new RentACar(RENT_A_CAR_NAME);
        Vehicle car = new Car(PLATE_CAR, 10, rentACar);
        Vehicle motorcycle = new Motorcycle(PLATE_MOTORCYCLE, 10, rentACar);

        assertEquals(PLATE_CAR, car.getPlate());
        assertTrue(Vehicle.vehicles.contains(car));
        assertTrue(Car.cars.contains(car));
        assertTrue(rentACar.hasVehicle(PLATE_CAR));
        assertEquals(PLATE_MOTORCYCLE, motorcycle.getPlate());
        assertTrue(Vehicle.vehicles.contains(motorcycle));
        assertTrue(Motorcycle.motorcycles.contains(motorcycle));
        assertTrue(rentACar.hasVehicle(PLATE_MOTORCYCLE));
    }

    @Test(expected = CarException.class)
    public void constructorNoLicensePlate() {
        RentACar rentACar = new RentACar(RENT_A_CAR_NAME);
        new Car("", 10, rentACar);
    }

    @Test(expected = CarException.class)
    public void constructorNegativeKilometers() {
        RentACar rentACar = new RentACar(RENT_A_CAR_NAME);
        new Car(PLATE_CAR, -1, rentACar);
    }

    @Test(expected = CarException.class)
    public void constructorNoRenACar() {
        new Car(PLATE_CAR, 0, null);
    }

    @Test(expected = CarException.class)
    public void constructorDuplicatedCar() {
        RentACar rentACar = new RentACar(RENT_A_CAR_NAME);
        new Car(PLATE_CAR, 0, rentACar);
        new Car(PLATE_CAR, 0, rentACar);
    }

    @Test
    public void rent() {
        RentACar rentACar = new RentACar(RENT_A_CAR_NAME);
        Vehicle car = new Car(PLATE_CAR, 10, rentACar);

        assertTrue(car.isFree(date1, date2));
        assertTrue(car.isFree(date1, date3));
        assertTrue(car.isFree(date3, date4));
        assertTrue(car.isFree(date4, date4));
        
        car.rent(DRIVING_LICENSE, date2, date4);
        
        assertEquals(1, car.getNumberOfRentings());
        assertFalse(car.isFree(date1, date2));
        assertFalse(car.isFree(date1, date3));
        assertFalse(car.isFree(date3, date4));
        assertFalse(car.isFree(date4, date4));
        assertTrue(car.isFree(date1, date1));
    }

    @Test
    public void getVehicles() {
        RentACar rentACar = new RentACar(RENT_A_CAR_NAME);
        Vehicle car = new Car(PLATE_CAR, 10, rentACar);
        Vehicle motorcycle = new Motorcycle(PLATE_MOTORCYCLE, 10, rentACar);

        assertTrue(Car.cars.contains(car));
        assertTrue(Motorcycle.motorcycles.contains(motorcycle));
        assertTrue(Vehicle.vehicles.contains(car));
        assertTrue(Vehicle.vehicles.contains(motorcycle));

        assertEquals(1, rentACar.getAvailableCars(date1, date2).size());
        assertTrue(rentACar.getAvailableCars(date1, date2).contains(car));
        assertEquals(1, rentACar.getAvailableMotorcycles(date1, date2).size());
        assertTrue(rentACar.getAvailableMotorcycles(date1, date2).contains(motorcycle));

        assertEquals(1, RentACar.getAllAvailableCars(date1, date2).size());
        assertTrue(RentACar.getAllAvailableCars(date1, date2).contains(car));
        assertEquals(1, RentACar.getAllAvailableMotorcycles(date1, date2).size());
        assertTrue(RentACar.getAllAvailableMotorcycles(date1, date2).contains(motorcycle));
    }

    @After
    public void tearDown() {
        RentACar.rentACars.clear();
        Vehicle.vehicles.clear();
        Car.cars.clear();
        Motorcycle.motorcycles.clear();
    }

}
