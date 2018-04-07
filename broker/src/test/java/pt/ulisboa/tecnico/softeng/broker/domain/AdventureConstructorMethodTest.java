package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class AdventureConstructorMethodTest {
	
	private static final String CODE = "BR01";
	private static final String NAME = "eXtremeADVENTURE";
	private static final String NIF_AS_BUYER  = "111111111";
	private static final String NIF_AS_SELLER = "222222222";
	private static final String NIF_OF_CLIENT = "333333333";
	private static final String IBAN_BROKER = "BK011234567";
	private static final String IBAN_CLIENT = "CL011234567";
	
	private static final int GOOD_AGE = 20;
	private static final int MARGIN = 50;
	private static final String DR_L = "A1";
	private static final boolean RENTV_F = false;
		
	private Client client;
	private Broker broker;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);

	@Before
	public void setUp() {
		this.broker = new Broker(CODE, NAME, NIF_AS_SELLER, NIF_AS_BUYER, IBAN_BROKER);
		this.client = new Client(this.broker, IBAN_CLIENT, NIF_OF_CLIENT, DR_L, GOOD_AGE);
	}

	@Test
	public void success() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, this.client, MARGIN, RENTV_F);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(20, adventure.getAge());
		Assert.assertEquals(IBAN_CLIENT, adventure.getIBAN());
		Assert.assertEquals(MARGIN, adventure.getAmount());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void nullBroker() {
		new Adventure(null, this.begin, this.end, this.client, MARGIN, RENTV_F);
	}

	@Test(expected = BrokerException.class)
	public void nullBegin() {
		new Adventure(this.broker, null, this.end, this.client, MARGIN, RENTV_F);
	}

	@Test(expected = BrokerException.class)
	public void nullEnd() {
		new Adventure(this.broker, this.begin, null, this.client, MARGIN, RENTV_F);
	}
	
	@Test(expected = BrokerException.class)
	public void nullClient() {
		new Adventure(this.broker, this.begin, this.end, null, MARGIN, RENTV_F);
	}
	
	@Test(expected = BrokerException.class)
	public void negativeMargin() {
		new Adventure(this.broker, this.begin, this.end, this.client, -100, RENTV_F);
	}

	@Test
	public void successEqualDates() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.begin, this.client, MARGIN, RENTV_F);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.begin, adventure.getEnd());
		Assert.assertEquals(20, adventure.getAge());
		Assert.assertEquals(IBAN_CLIENT, adventure.getIBAN());
		Assert.assertEquals(MARGIN, adventure.getAmount());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void inconsistentDates() {
		new Adventure(this.broker, this.begin, this.begin.minusDays(1), this.client, MARGIN, RENTV_F);
	}

	@After
	public void tearDown() {
		Broker.brokers.clear();
	}

}
