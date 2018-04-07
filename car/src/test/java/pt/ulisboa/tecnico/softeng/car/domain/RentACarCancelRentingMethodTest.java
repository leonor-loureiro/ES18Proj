package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.car.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;

@RunWith(JMockit.class)
public class RentACarCancelRentingMethodTest {
	private static final String NAME = "eartz";
	private static final String IBAN = "IBAN1";
	private static final String NIF = "123456789";
	
	private static final String DRIVING_LICENSE1 = "br123";
	private static final String clientNIF1 = "135792468";
	private static final String clientIBAN1 = "ES063";
	
	private static final LocalDate date1 = LocalDate.parse("2018-01-06");
	private static final LocalDate date2 = LocalDate.parse("2018-01-07");
	
	private static final String PLATE_CAR1 = "aa-00-11";
	private static final int PRICE = 10;
	
	private String renting;
	
	
	@Before
	public void setUp() {
		RentACar rentACar1 = new RentACar(NAME, NIF, IBAN); 
		Car car = new Car(PLATE_CAR1, 10, PRICE, rentACar1);	
	}

	@Test
	public void success(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);

				TaxInterface.submitInvoice((InvoiceData) this.any);
			}
		};

		renting = RentACar.rentVehicle(Car.class,DRIVING_LICENSE1, date1, date2, clientNIF1, clientIBAN1);
		String cancel = RentACar.cancelRenting(renting);

		assertTrue(RentACar.getRenting(renting).isCancelled());
		assertEquals(cancel, RentACar.getRenting(renting).getCancellation());
	}

	@Test(expected = CarException.class)
	public void doesNotExist(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);

				TaxInterface.submitInvoice((InvoiceData) this.any);
			}
		};

		
		renting = RentACar.rentVehicle(Car.class,DRIVING_LICENSE1, date1, date2, clientNIF1, clientIBAN1);
		RentACar.cancelRenting("XPTO");
	}

	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
		renting = null;	}

}
