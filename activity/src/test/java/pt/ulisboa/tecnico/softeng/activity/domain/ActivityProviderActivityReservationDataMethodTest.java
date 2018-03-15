package pt.ulisboa.tecnico.softeng.activity.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.activity.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;

@RunWith(JMockit.class)
public class ActivityProviderActivityReservationDataMethodTest {
	private static final String NAME = "ExtremeAdventure";
	private static final String CODE = "XtremX";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private ActivityProvider provider;
	private ActivityOffer offer;
	private Booking booking;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider(CODE, NAME, "NIF", "IBAN");
		Activity activity = new Activity(this.provider, "Bush Walking", 18, 80, 3);

		this.offer = new ActivityOffer(activity, this.begin, this.end, 30);
	}

	@Test
	public void success(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);

				TaxInterface.submitInvoice((InvoiceData) this.any);
			}
		};
		this.booking = new Booking(this.provider, this.offer, "123456789", "IBAN");

		ActivityReservationData data = ActivityProvider.getActivityReservationData(this.booking.getReference());

		assertEquals(this.booking.getReference(), data.getReference());
		assertNull(data.getCancellation());
		assertEquals(NAME, data.getName());
		assertEquals(CODE, data.getCode());
		assertEquals(this.begin, data.getBegin());
		assertEquals(this.end, data.getEnd());
		assertNull(data.getCancellationDate());
	}

	@Test
	public void successCancelled(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);

				TaxInterface.submitInvoice((InvoiceData) this.any);
			}
		};

		this.booking = new Booking(this.provider, this.offer, "123456789", "IBAN");
		this.booking.cancel();
		ActivityReservationData data = ActivityProvider.getActivityReservationData(this.booking.getCancellation());

		assertEquals(this.booking.getReference(), data.getReference());
		assertEquals(this.booking.getCancellation(), data.getCancellation());
		assertEquals(NAME, data.getName());
		assertEquals(CODE, data.getCode());
		assertEquals(this.begin, data.getBegin());
		assertEquals(this.end, data.getEnd());
		assertNotNull(data.getCancellationDate());
	}

	@Test(expected = ActivityException.class)
	public void nullReference() {
		ActivityProvider.getActivityReservationData(null);
	}

	@Test(expected = ActivityException.class)
	public void emptyReference() {
		ActivityProvider.getActivityReservationData("");
	}

	@Test(expected = ActivityException.class)
	public void notExistsReference() {
		ActivityProvider.getActivityReservationData("XPTO");
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
