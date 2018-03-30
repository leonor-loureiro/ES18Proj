package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;

@RunWith(JMockit.class)
public class RentACarConstructorTest {
	private static final String NAME = "eartz";
	private static final String NIF = "123456789";
	private static final String IBAN = "ES061";
	
	@Test
	public void success(@Mocked final Account rentACarAccount, @Mocked final Seller rentACarAsSeller) {
		new Expectations() {{
				rentACarAsSeller.getNIF();
				result = NIF;
				
				rentACarAccount.getIBAN();
				result = IBAN;
		}};
		
		RentACar rentACar = new RentACar(NAME, rentACarAsSeller.getNIF(), rentACarAccount.getIBAN());
		assertEquals(NAME, rentACar.getName());
		assertEquals(NIF, rentACar.getNIF());
		assertEquals(IBAN, rentACar.getIBAN());
	}

	@Test(expected = CarException.class)
	public void nullName(@Mocked final Account rentACarAccount, @Mocked final Seller rentACarAsSeller) {
		new Expectations() {{
			rentACarAsSeller.getNIF();
			result = NIF;
			
			rentACarAccount.getIBAN();
			result = IBAN;
		}};
		
		new RentACar(null, rentACarAsSeller.getNIF(), rentACarAccount.getIBAN());
	}

	@Test(expected = CarException.class)
	public void nullNIF(@Mocked final Account rentACarAccount, @Mocked final Seller rentACarAsSeller) {
		new Expectations() {{
			rentACarAsSeller.getNIF();
			result = null;
			
			rentACarAccount.getIBAN();
			result = IBAN;
		}};
		
		new RentACar(NAME, rentACarAsSeller.getNIF(), rentACarAccount.getIBAN());
	}
	
	@Test(expected = CarException.class)
	public void nullIBAN(@Mocked final Account rentACarAccount, @Mocked final Seller rentACarAsSeller) {
		new Expectations() {{
			rentACarAsSeller.getNIF();
			result = NIF;
			
			rentACarAccount.getIBAN();
			result = null;
		}};
		
		new RentACar(NAME, rentACarAsSeller.getNIF(), rentACarAccount.getIBAN());
	}
	
	@Test(expected = CarException.class)
	public void emptyName(@Mocked final Account rentACarAccount, @Mocked final Seller rentACarAsSeller) {
		new Expectations() {{
			rentACarAsSeller.getNIF();
			result = NIF;
			
			rentACarAccount.getIBAN();
			result = IBAN;
		}};
		
		new RentACar("", rentACarAsSeller.getNIF(), rentACarAccount.getIBAN());
	}

	@Test(expected = CarException.class)
	public void smallerNIF(@Mocked final Account rentACarAccount, @Mocked final Seller rentACarAsSeller) {
		new Expectations() {{
			rentACarAsSeller.getNIF();
			result = "12345678";
			
			rentACarAccount.getIBAN();
			result = IBAN;
		}};
		
		new RentACar(NAME, rentACarAsSeller.getNIF(), rentACarAccount.getIBAN());
	}

	@Test(expected = CarException.class)
	public void biggerNIF(@Mocked final Account rentACarAccount, @Mocked final Seller rentACarAsSeller) {
		new Expectations() {{
			rentACarAsSeller.getNIF();
			result = "1234567890";
			
			rentACarAccount.getIBAN();
			result = IBAN;
		}};
		
		new RentACar(NAME, rentACarAsSeller.getNIF(), rentACarAccount.getIBAN());
	}
	
	@Test(expected = CarException.class)
	public void smallerIBAN(@Mocked final Account rentACarAccount, @Mocked final Seller rentACarAsSeller) {
		new Expectations() {{
			rentACarAsSeller.getNIF();
			result = NIF;
			
			rentACarAccount.getIBAN();
			result = "ES06";
		}};
		
		new RentACar(NAME, rentACarAsSeller.getNIF(), rentACarAccount.getIBAN());
	}
	
	@After
	public void tearDown() {
		RentACar.rentACars.clear();
	}
}
