package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.services.remote.BankInterface;
import pt.ulisboa.tecnico.softeng.car.services.remote.TaxInterface;

@RunWith(JMockit.class)
public class RentACarRentTest extends RollbackTestAbstractClass {
	private static final String ADVENTURE_ID = "AdventureId";
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
	private BankInterface bankInterface;
	@Mocked
	private TaxInterface taxInterface;

	@Override
	public void populate4Test() {
		this.rentACar = new RentACar(RENT_A_CAR_NAME, NIF, IBAN);
		this.car = new Car(PLATE_CAR, 10, 10, this.rentACar);
	}

	@Test
	public void rentACarHasCarAvailable() {
		String reference = RentACar.rent(Car.class, DRIVING_LICENSE, NIF, IBAN_BUYER, BEGIN, END, ADVENTURE_ID);
		assertNotNull(reference);
		assertFalse(this.car.isFree(BEGIN, END));
	}

	@Test(expected = CarException.class)
	public void rentACarHasNoCarsAvailable() {
		RentACar.rent(Car.class, DRIVING_LICENSE, NIF, IBAN_BUYER, BEGIN, END, ADVENTURE_ID);
		RentACar.rent(Car.class, DRIVING_LICENSE, NIF, IBAN_BUYER, BEGIN.plusDays(1), END.plusDays(1), ADVENTURE_ID);
	}

	@Test(expected = CarException.class)
	public void noRentACars() {
		this.rentACar.delete();
		RentACar.rent(Car.class, DRIVING_LICENSE, NIF, IBAN_BUYER, BEGIN, END, ADVENTURE_ID);
	}

	@Test(expected = CarException.class)
	public void noMotorcycles() {
		RentACar.rent(Motorcycle.class, DRIVING_LICENSE, NIF, IBAN_BUYER, BEGIN, END, ADVENTURE_ID);
	}
}
