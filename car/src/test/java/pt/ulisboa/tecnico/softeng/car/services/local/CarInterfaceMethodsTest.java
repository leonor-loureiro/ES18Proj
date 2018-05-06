package pt.ulisboa.tecnico.softeng.car.services.local;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.car.domain.Car;
import pt.ulisboa.tecnico.softeng.car.domain.RentACar;
import pt.ulisboa.tecnico.softeng.car.domain.RollbackTestAbstractClass;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.services.remote.BankInterface;
import pt.ulisboa.tecnico.softeng.car.services.remote.TaxInterface;
import pt.ulisboa.tecnico.softeng.car.services.remote.dataobjects.InvoiceData;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(JMockit.class)
public class CarInterfaceMethodsTest extends RollbackTestAbstractClass {
	private static final String PLATE_CAR = "22-33-HZ";
	private static final String RENT_A_CAR_NAME = "Eartz";
	private static final String DRIVING_LICENSE = "lx1423";
	private static final LocalDate BEGIN = LocalDate.parse("2018-01-06");
	private static final LocalDate END = LocalDate.parse("2018-01-09");
	private static final String NIF = "NIF";
	private static final String IBAN = "IBAN";
	private static final String IBAN_BUYER = "IBAN";
	private RentACar rentACar;
	private Car car;


	@Mocked
	private TaxInterface taxInterface;
	@Mocked
	private BankInterface bankInterface;

	@Override
	public void populate4Test() {
		rentACar = new RentACar(RENT_A_CAR_NAME, NIF, IBAN);
		car = new Car(PLATE_CAR, 10, 10, rentACar);
	}

//	@Test
//	public void success() {
//		new Expectations() {
//			{
//				BankInterface.processPayment(this.anyString, this.anyDouble);
//
//				TaxInterface.submitInvoice((InvoiceData) this.any);
//			}
//		};
//	}

	@Test
	public void getRentingsByPlate() {
		String reference = RentACar.rent(Car.class, DRIVING_LICENSE, NIF, IBAN_BUYER, BEGIN, END);

		Set<RentingData> data = RentACarInterface.getRentingsByPlate(rentACar.getCode(), PLATE_CAR);

		assertTrue(reference.equals(data.stream().findFirst().get().getReference()));
	}
	@Test
	public void rentACar() {
		assertNotNull(RentACarInterface.rentVehicle(rentACar.getCode(), PLATE_CAR, DRIVING_LICENSE, BEGIN, END, NIF, IBAN));
	}

}
