package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;

@RunWith(JMockit.class)
public class HotelConstructorTest {
	private static final String HOTEL_NAME = "Londres";
	private static final String HOTEL_CODE = "XPTO123";
	private static final String NIF = "123456789";
	private static final String IBAN = "ES061";
	private static final int singlePRICE = 10;
	private static final int doublePRICE = 10;
	
	@Test
	public void success(@Mocked final Account hotelAccount, @Mocked final Seller hotelAsSeller) {
		new Expectations() {{
			hotelAsSeller.getNIF();
			result = NIF;
			
			hotelAccount.getIBAN();
			result = IBAN;
		}};
		
		Hotel hotel = new Hotel(HOTEL_CODE, HOTEL_NAME, hotelAsSeller.getNIF(), hotelAccount.getIBAN(), singlePRICE, doublePRICE);

		Assert.assertEquals(HOTEL_NAME, hotel.getName());
		Assert.assertTrue(hotel.getCode().length() == Hotel.CODE_SIZE);
		Assert.assertEquals(0, hotel.getNumberOfRooms());
		Assert.assertEquals(1, Hotel.hotels.size());
		Assert.assertEquals(NIF, hotel.getNIF());
		Assert.assertEquals(IBAN, hotel.getIBAN());
		Assert.assertEquals(singlePRICE, hotel.getSinglePrice());
		Assert.assertEquals(doublePRICE, hotel.getDoublePrice());
	}

	@Test(expected = HotelException.class)
	public void nullCode(@Mocked final Account hotelAccount, @Mocked final Seller hotelAsSeller) {
		new Expectations() {{
			hotelAsSeller.getNIF();
			result = NIF;
			
			hotelAccount.getIBAN();
			result = IBAN;
		}};
		
		new Hotel(null, HOTEL_NAME, hotelAsSeller.getNIF(), hotelAccount.getIBAN(), singlePRICE, doublePRICE);
	}

	@Test(expected = HotelException.class)
	public void blankCode(@Mocked final Account hotelAccount, @Mocked final Seller hotelAsSeller) {
		new Expectations() {{
			hotelAsSeller.getNIF();
			result = NIF;
			
			hotelAccount.getIBAN();
			result = IBAN;
		}};
		
		new Hotel("      ", HOTEL_NAME, hotelAsSeller.getNIF(), hotelAccount.getIBAN(), singlePRICE, doublePRICE);
	}

	@Test(expected = HotelException.class)
	public void largerCode(@Mocked final Account hotelAccount, @Mocked final Seller hotelAsSeller) {
		new Expectations() {{
			hotelAsSeller.getNIF();
			result = NIF;
			
			hotelAccount.getIBAN();
			result = IBAN;
		}};
		
		new Hotel(HOTEL_CODE + "4", HOTEL_NAME, hotelAsSeller.getNIF(), hotelAccount.getIBAN(), singlePRICE, doublePRICE);
	}
	
	@Test(expected = HotelException.class)
	public void smallerCode(@Mocked final Account hotelAccount, @Mocked final Seller hotelAsSeller) {
		new Expectations() {{
			hotelAsSeller.getNIF();
			result = NIF;
			
			hotelAccount.getIBAN();
			result = IBAN;
		}};
		
		new Hotel("XPTO12", HOTEL_NAME, hotelAsSeller.getNIF(), hotelAccount.getIBAN(), singlePRICE, doublePRICE);
	}

	@Test(expected = HotelException.class)
	public void nullName(@Mocked final Account hotelAccount, @Mocked final Seller hotelAsSeller) {
		new Expectations() {{
			hotelAsSeller.getNIF();
			result = NIF;
			
			hotelAccount.getIBAN();
			result = IBAN;
		}};
		
		new Hotel(HOTEL_CODE, null, hotelAsSeller.getNIF(), hotelAccount.getIBAN(), singlePRICE, doublePRICE);
	}

	@Test(expected = HotelException.class)
	public void blankName(@Mocked final Account hotelAccount, @Mocked final Seller hotelAsSeller) {
		new Expectations() {{
			hotelAsSeller.getNIF();
			result = NIF;
			
			hotelAccount.getIBAN();
			result = IBAN;
		}};
		
		new Hotel(HOTEL_CODE, "  ", hotelAsSeller.getNIF(), hotelAccount.getIBAN(), singlePRICE, doublePRICE);
	}

	@Test(expected = HotelException.class)
	public void emptyName(@Mocked final Account hotelAccount, @Mocked final Seller hotelAsSeller) {
		new Expectations() {{
			hotelAsSeller.getNIF();
			result = NIF;
			
			hotelAccount.getIBAN();
			result = IBAN;
		}};
		
		new Hotel(HOTEL_CODE, "", hotelAsSeller.getNIF(), hotelAccount.getIBAN(), singlePRICE, doublePRICE);
	}

	@Test(expected = HotelException.class)
	public void codeNotUnique(@Mocked final Account hotelAccount, @Mocked final Seller hotelAsSeller) {
		new Expectations() {{
			hotelAsSeller.getNIF();
			result = NIF;
			
			hotelAccount.getIBAN();
			result = IBAN;
		}};
		
		new Hotel(HOTEL_CODE, HOTEL_NAME, hotelAsSeller.getNIF(), hotelAccount.getIBAN(), singlePRICE, doublePRICE);
		new Hotel(HOTEL_CODE, HOTEL_NAME + " City", hotelAsSeller.getNIF(), hotelAccount.getIBAN(), singlePRICE, doublePRICE);
	}
	
	@Test(expected = HotelException.class)
	public void nullNIF(@Mocked final Account hotelAccount, @Mocked final Seller hotelAsSeller) {
		new Expectations() {{
			hotelAsSeller.getNIF();
			result = null;
			
			hotelAccount.getIBAN();
			result = IBAN;
		}};
		
		new Hotel(HOTEL_CODE, HOTEL_NAME, hotelAsSeller.getNIF(), hotelAccount.getIBAN(), singlePRICE, doublePRICE);
	}
	
	@Test(expected = HotelException.class)
	public void smallerNIF(@Mocked final Account hotelAccount, @Mocked final Seller hotelAsSeller) {
		new Expectations() {{
			hotelAsSeller.getNIF();
			result = "12345678";
			
			hotelAccount.getIBAN();
			result = IBAN;
		}};
		
		new Hotel(HOTEL_CODE, HOTEL_NAME, hotelAsSeller.getNIF(), hotelAccount.getIBAN(), singlePRICE, doublePRICE);
	}

	@Test(expected = HotelException.class)
	public void biggerNIF(@Mocked final Account hotelAccount, @Mocked final Seller hotelAsSeller) {
		new Expectations() {{
			hotelAsSeller.getNIF();
			result = "1234567890";
			
			hotelAccount.getIBAN();
			result = IBAN;
		}};
		
		new Hotel(HOTEL_CODE, HOTEL_NAME, hotelAsSeller.getNIF(), hotelAccount.getIBAN(), singlePRICE, doublePRICE);
	}
	
	@Test(expected = HotelException.class)
	public void nullIBAN(@Mocked final Account hotelAccount, @Mocked final Seller hotelAsSeller) {
		new Expectations() {{
			hotelAsSeller.getNIF();
			result = NIF;
			
			hotelAccount.getIBAN();
			result = null;
		}};
		
		new Hotel(HOTEL_CODE, HOTEL_NAME, hotelAsSeller.getNIF(), hotelAccount.getIBAN(), singlePRICE, doublePRICE);
	}

	@Test(expected = HotelException.class)
	public void smallerIBAN(@Mocked final Account hotelAccount, @Mocked final Seller hotelAsSeller) {
		new Expectations() {{
			hotelAsSeller.getNIF();
			result = NIF;
			
			hotelAccount.getIBAN();
			result = "ES06";
		}};
		
		new Hotel(HOTEL_CODE, HOTEL_NAME, hotelAsSeller.getNIF(), hotelAccount.getIBAN(), singlePRICE, doublePRICE);
	}
	
	@Test(expected = HotelException.class)
	public void freeSinglePrice(@Mocked final Account hotelAccount, @Mocked final Seller hotelAsSeller) {
		new Expectations() {{
			hotelAsSeller.getNIF();
			result = NIF;
			
			hotelAccount.getIBAN();
			result = IBAN;
		}};
		
		new Hotel(HOTEL_CODE, HOTEL_NAME, hotelAsSeller.getNIF(), hotelAccount.getIBAN(), 0, doublePRICE);
	}
	
	@Test(expected = HotelException.class)
	public void freeDoublePrice(@Mocked final Account hotelAccount, @Mocked final Seller hotelAsSeller) {
		new Expectations() {{
			hotelAsSeller.getNIF();
			result = NIF;
			
			hotelAccount.getIBAN();
			result = IBAN;
		}};
		
		new Hotel(HOTEL_CODE, HOTEL_NAME, hotelAsSeller.getNIF(), hotelAccount.getIBAN(), singlePRICE, 0);
	}
	
	@Test(expected = HotelException.class)
	public void negativeSinglePrice(@Mocked final Account hotelAccount, @Mocked final Seller hotelAsSeller) {
		new Expectations() {{
			hotelAsSeller.getNIF();
			result = NIF;
			
			hotelAccount.getIBAN();
			result = IBAN;
		}};
		
		new Hotel(HOTEL_CODE, HOTEL_NAME, hotelAsSeller.getNIF(), hotelAccount.getIBAN(), -singlePRICE, doublePRICE);
	}
	
	@Test(expected = HotelException.class)
	public void negativeDoublePrice(@Mocked final Account hotelAccount, @Mocked final Seller hotelAsSeller) {
		new Expectations() {{
			hotelAsSeller.getNIF();
			result = NIF;
			
			hotelAccount.getIBAN();
			result = IBAN;
		}};
		
		new Hotel(HOTEL_CODE, HOTEL_NAME, hotelAsSeller.getNIF(), hotelAccount.getIBAN(), singlePRICE, -doublePRICE);
	}
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
