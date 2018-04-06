package pt.ulisboa.tecnico.softeng.hotel.domain;

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
import pt.ulisboa.tecnico.softeng.hotel.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.TaxInterface;


@RunWith(JMockit.class)
public class InvoiceProcessorSubmitBookingMethodTest {
	private static final LocalDate arrival = new LocalDate(2018, 6, 1);
	private static final LocalDate departure = new LocalDate(2018, 7, 1);
	private Hotel hotel;
	private Booking booking;
	
	private static final int singlePRICE = 10;
	private static final int doublePRICE = 10;
	
	@Before
	public void setUp() {
		this.hotel = new Hotel("1234567", "Caravela", "123456789", "123456", singlePRICE, doublePRICE);
		this.booking = new Booking(this.hotel, arrival, departure, "987654321", "654321");		
	}

	@Test
	public void success(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				TaxInterface.submitInvoice((InvoiceData) this.any);
			}
		};

		this.hotel.getProcessor().submitBooking(this.booking);

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
				BankInterface.processPayment(this.anyString, this.anyDouble);
				
				TaxInterface.cancelInvoice(this.anyString);
				BankInterface.cancelPayment(this.anyString);
			}
		};

		this.hotel.getProcessor().submitBooking(this.booking);
		this.booking.cancel();

		new FullVerifications() {
			{
			}
		};
	}
	
	
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
