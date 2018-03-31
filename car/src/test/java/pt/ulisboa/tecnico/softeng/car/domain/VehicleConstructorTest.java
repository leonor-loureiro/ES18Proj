package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class VehicleConstructorTest {
	private static final String PLATE_CAR = "22-33-HZ";
	private static final String PLATE_MOTORCYCLE = "44-33-HZ";
	private static final String RENT_A_CAR_NAME = "Eartz";
	private RentACar rentACar;

	private static final String NIF1 = "123456789"; // novo
	private static final String IBAN1 = "ES061"; // novo
	private static final String NIF2 = "987654321"; // novo
	private static final String IBAN2 = "ES062"; // novo
	private static final int PRICE = 50; 
	
	@Before
	public void setUp() {
		this.rentACar = new RentACar(RENT_A_CAR_NAME, NIF1, IBAN1); // novo
	}
	
	

	@Test
	public void success() {
		Vehicle car = new Car(PLATE_CAR, 10, PRICE, this.rentACar); 
		Vehicle motorcycle = new Motorcycle(PLATE_MOTORCYCLE, 10, PRICE, this.rentACar); 

		assertEquals(PLATE_CAR, car.getPlate());
		assertTrue(this.rentACar.hasVehicle(PLATE_CAR));
		assertEquals(PLATE_MOTORCYCLE, motorcycle.getPlate());
		assertTrue(this.rentACar.hasVehicle(PLATE_MOTORCYCLE));
		
		assertEquals(PRICE, car.getPrice()); 
		assertEquals(PRICE, motorcycle.getPrice()); 
		assertEquals(10, car.getKilometers()); 
		assertEquals(10, motorcycle.getKilometers());
	}

	@Test(expected = CarException.class)
	public void emptyLicensePlate() {
		new Car("", 10, PRICE, this.rentACar); 
	}

	@Test(expected = CarException.class)
	public void nullLicensePlate() {
		new Car(null, 10, PRICE, this.rentACar);
	}

	@Test(expected = CarException.class)
	public void invalidLicensePlate() {
		new Car("AA-XX-a", 10, PRICE, this.rentACar); 
	}

	@Test(expected = CarException.class)
	public void invalidLicensePlate2() {
		new Car("AA-XX-aaa", 10, PRICE, this.rentACar); 
	}

	@Test(expected = CarException.class)
	public void duplicatedPlate() {
		new Car(PLATE_CAR, 0, PRICE, this.rentACar); 
		new Car(PLATE_CAR, 0, PRICE, this.rentACar); 
	}

	@Test(expected = CarException.class)
	public void duplicatedPlateDifferentRentACar() {
		new Car(PLATE_CAR, 0, PRICE, rentACar); 
		RentACar rentACar2 = new RentACar(RENT_A_CAR_NAME + "2", NIF2, IBAN2); // novo
		new Car(PLATE_CAR, 2, PRICE, rentACar2);
	}

	@Test(expected = CarException.class)
	public void negativeKilometers() {
		new Car(PLATE_CAR, -1, PRICE, this.rentACar); 
	}

	@Test(expected = CarException.class)
	public void zeroPrice() {
		new Car(PLATE_CAR, 10, 0, this.rentACar);
	}
	
	@Test(expected = CarException.class)
	public void negativePrice() {
		new Car(PLATE_CAR, 0, -PRICE, this.rentACar);
	}
	
	@Test(expected = CarException.class)
	public void noRentACar() {
		new Car(PLATE_CAR, 0, PRICE, null);
	}

	
	
	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
	}

}
