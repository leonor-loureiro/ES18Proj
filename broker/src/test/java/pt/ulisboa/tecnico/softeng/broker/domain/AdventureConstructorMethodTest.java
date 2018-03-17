package pt.ulisboa.tecnico.softeng.broker.domain;

import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mockit.Expectations;

import org.junit.runner.RunWith;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;

@RunWith(JMockit.class)
public class AdventureConstructorMethodTest {
	private static final String BROKER_IBAN = "BROKER_IBAN";
	private static final String NIF_AS_BUYER = "buyerNIF";
	private static final String NIF_AS_SELLER = "sellerNIF";
	private static final String OTHER_NIF = "987654321";
	private static final String NIF = "123456789";
	private static final String DRIVING_LICENSE = "IMT1234";
	private static final int AGE = 20;
	private static final double MARGIN = 0.3;
	private static final String IBAN = "BK011234567";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);

	private Broker broker;
	private Client client;

	@Before
	public void setUp() {
		this.broker = new Broker("BR01", "eXtremeADVENTURE", NIF_AS_SELLER, NIF_AS_BUYER, BROKER_IBAN);

		this.client = new Client(this.broker, IBAN, NIF, DRIVING_LICENSE, AGE);
	}

	@Test
	public void success() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, this.client, MARGIN);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(this.client, adventure.getClient());
		Assert.assertEquals(0.3, adventure.getMargin(), 0);
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void nullBroker() {
		new Adventure(null, this.begin, this.end, this.client, MARGIN);
	}

	@Test(expected = BrokerException.class)
	public void nullBegin() {
		new Adventure(this.broker, null, this.end, this.client, MARGIN);
	}

	@Test(expected = BrokerException.class)
	public void nullEnd() {
		new Adventure(this.broker, this.begin, null, this.client, MARGIN);
	}

	@Test
	public void successEqual18() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end,
				new Client(this.broker, IBAN, OTHER_NIF, DRIVING_LICENSE + "1",18), MARGIN);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(18, adventure.getAge());
		Assert.assertEquals(IBAN, adventure.getIBAN());
		Assert.assertEquals(0.3, adventure.getMargin(), 0);
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void negativeAge() {
		Client c = new Client(this.broker, IBAN, OTHER_NIF, DRIVING_LICENSE, 17);
		new Adventure(this.broker, this.begin, this.end, c, MARGIN);
	}

	@Test
	public void successEqual100() {
        Client c = new Client(this.broker, IBAN, OTHER_NIF, DRIVING_LICENSE + "1", 100);
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, c, MARGIN);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(100, adventure.getAge());
		Assert.assertEquals(IBAN, adventure.getIBAN());
		Assert.assertEquals(0.3, adventure.getMargin(), 0);
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void over100() {
		Client c = new Client(this.broker, IBAN, OTHER_NIF, DRIVING_LICENSE, 101);
		new Adventure(this.broker, this.begin, this.end, c, MARGIN);
	}

	@Test(expected = BrokerException.class)
	public void negativeAmount() {
		new Adventure(this.broker, this.begin, this.end, this.client, -100);
	}

	@Test
	public void success1Amount() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, this.client, 1);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(20, adventure.getAge());
		Assert.assertEquals(IBAN, adventure.getIBAN());
		Assert.assertEquals(1, adventure.getMargin(), 0);
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void zeroAmount() {
		new Adventure(this.broker, this.begin, this.end, this.client, 0);
	}

	@Test
	public void successEqualDates() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.begin, this.client, MARGIN);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.begin, adventure.getEnd());
		Assert.assertEquals(20, adventure.getAge());
		Assert.assertEquals(IBAN, adventure.getIBAN());
		Assert.assertEquals(0.3, adventure.getMargin(), 0);
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void inconsistentDates() {
		new Adventure(this.broker, this.begin, this.begin.minusDays(1), this.client, MARGIN);
	}

	@After
	public void tearDown() {
		Broker.brokers.clear();
	}

}
