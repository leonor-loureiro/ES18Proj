package pt.ulisboa.tecnico.softeng.activity.domain;

import static org.junit.Assert.fail;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.FullVerifications;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.interfaces.TaxInterface;

@RunWith(JMockit.class)
public class BookingContructorMethodTest {
	private static final String NIF = "123456789";
	private ActivityProvider provider;
	private ActivityOffer offer;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure", "NIF", "IBAN");
		Activity activity = new Activity(this.provider, "Bush Walking", 18, 80, 3);

		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		this.offer = new ActivityOffer(activity, begin, end, 30);
	}

	@Test
	public void success(@Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.result = this.anyString;
			}
		};

		Booking booking = new Booking(this.provider, this.offer, NIF);

		Assert.assertTrue(booking.getReference().startsWith(this.provider.getCode()));
		Assert.assertTrue(booking.getReference().length() > ActivityProvider.CODE_SIZE);
		Assert.assertEquals(1, this.offer.getNumberOfBookings());
	}

	@Test(expected = ActivityException.class)
	public void nullProvider(@Mocked final TaxInterface taxInterface) {
		new Booking(null, this.offer, NIF);

		new FullVerifications(taxInterface) {
		};
	}

	@Test(expected = ActivityException.class)
	public void nullOffer(@Mocked final TaxInterface taxInterface) {
		new Booking(this.provider, null, NIF);

		new FullVerifications(taxInterface) {
		};
	}

	@Test(expected = ActivityException.class)
	public void nullNIF(@Mocked final TaxInterface taxInterface) {
		new Booking(null, this.offer, null);

		new FullVerifications(taxInterface) {
		};
	}

	@Test(expected = ActivityException.class)
	public void emptyNIF(@Mocked final TaxInterface taxInterface) {
		new Booking(this.provider, null, "     ");
	}

	@Test
	public void bookingEqualCapacity(@Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.result = this.anyString;
				this.times = 3;
			}
		};

		new Booking(this.provider, this.offer, NIF);
		new Booking(this.provider, this.offer, NIF);
		new Booking(this.provider, this.offer, NIF);
		try {
			new Booking(this.provider, this.offer, NIF);
			fail();
		} catch (ActivityException ae) {
			Assert.assertEquals(3, this.offer.getNumberOfBookings());
		}
	}

	@Test
	public void bookingEqualCapacityButHasCancelled(@Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.result = this.anyString;
				this.times = 4;
			}
		};

		new Booking(this.provider, this.offer, NIF);
		new Booking(this.provider, this.offer, NIF);
		Booking booking = new Booking(this.provider, this.offer, NIF);
		booking.cancel();
		new Booking(this.provider, this.offer, NIF);

		Assert.assertEquals(3, this.offer.getNumberOfBookings());
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
