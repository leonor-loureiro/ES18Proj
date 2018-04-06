package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.FullVerifications;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.car.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.car.interfaces.TaxInterface;


@RunWith(JMockit.class)
public class InvoiceProcessorSubmitRentingMethodTest {
	private static final LocalDate begin = new LocalDate(2018, 6, 1);
	private static final LocalDate end = new LocalDate(2018, 7, 1);
	private RentACar rentACar;
	private Vehicle vehicle;
	private Renting renting;

	@Before
	public void setUp() {
		this.rentACar = new RentACar("Oasis", "123456789", "123456");
		this.vehicle = new Car("22-33-HZ", 50, 50, this.rentACar);
		this.renting = new Renting("br112233", begin, end, this.vehicle, "987654321", "654321");		
	}

	@Test
	public void success(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				TaxInterface.submitInvoice((InvoiceData) this.any);
			}
		};

		this.rentACar.getProcessor().submitRenting(this.renting);

		new FullVerifications() {
			{
			}
		};
	}
	
	@Test
	public void successCancel(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				TaxInterface.submitInvoice((InvoiceData) this.any);

				TaxInterface.cancelInvoice(this.anyString);
				BankInterface.cancelPayment(this.anyString);
			}
		};

		this.rentACar.getProcessor().submitRenting(this.renting);
		this.renting.cancel();

		new FullVerifications() {
			{
			}
		};
	}
	
	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
	}

}
