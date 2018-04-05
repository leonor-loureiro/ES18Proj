package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RentACarRentingTest {
	private static final String NAME1 = "eartz";
	private static final String PLATE_CAR1 = "aa-00-11";
	private static final String PLATE_MOTORCYCLE1 = "aa-11-00";
	
	private static final LocalDate date1 = LocalDate.parse("2018-01-06");
	private static final LocalDate date2 = LocalDate.parse("2018-01-07");
	private static final LocalDate date3 = LocalDate.parse("2018-01-08");
	private static final LocalDate date4 = LocalDate.parse("2018-01-09");
	private Renting renting;

	private static final String NIF1 = "123456789"; 
	private static final String IBAN1 = "ES061"; 
	
	private static final String DRIVING_LICENSE1 = "br123";
	private static final String clientNIF1 = "135792468";
	private static final String clientIBAN1 = "ES063"; 
	
	private static final String DRIVING_LICENSE2 = "br124";
	private static final String clientNIF2 = "135792467";
	private static final String clientIBAN2 = "ES064"; 
	
	private static final int PRICE = 50; 
	
	private RentACar rentACar1;
	private Vehicle car1;
	private Vehicle motorcycle1;
	
	@Before
	public void setUp() {
		rentACar1 = new RentACar(NAME1, NIF1, IBAN1); 
		Vehicle car1 = new Car(PLATE_CAR1, 10, PRICE, rentACar1);
		car1.rent(DRIVING_LICENSE1, date1, date2, clientNIF1, clientIBAN1);
		Vehicle motorcycle1 = new Motorcycle(PLATE_MOTORCYCLE1, 20, PRICE, rentACar1);
		motorcycle1.rent(DRIVING_LICENSE1, date1, date2, clientNIF1, clientIBAN1);
	}

	@Test
	public void sucessRentCar() {
		renting = rentACar1.rentVehicle(Car.class,DRIVING_LICENSE2, date3, date4, clientNIF2, clientIBAN2);
		assertNotNull(renting);
	}

	@Test
	public void sucessRentMotorcycle() {
		renting = rentACar1.rentVehicle(Motorcycle.class,DRIVING_LICENSE2, date3, date4, clientNIF2, clientIBAN2);
		assertNotNull(renting);
	}
	
	@Test
	public void failureRentCar() {
		renting = rentACar1.rentVehicle(Car.class,DRIVING_LICENSE2, date1, date1, clientNIF2, clientIBAN2);
		assertNotNull(renting);
	}

	@Test
	public void failureRentMotorcycle() {
		renting = rentACar1.rentVehicle(Motorcycle.class,DRIVING_LICENSE2, date1, date2, clientNIF2, clientIBAN2);
		assertNotNull(renting);
	}

	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
		renting = null;
	}
}
