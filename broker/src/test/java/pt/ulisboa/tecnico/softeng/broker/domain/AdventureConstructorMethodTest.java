package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.domain.exception.BrokerException;

public class AdventureConstructorMethodTest {
	private static final int AGE = 20;
	private static final int AMOUNT = 300;
	private static final String IBAN = "BK011234567";
	private Broker broker;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);

	@Before
	public void setUp() {
		this.broker = new Broker("BR01", "eXtremeADVENTURE");
	}

	@Test
	public void success() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, AGE, IBAN, AMOUNT);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(20, adventure.getAge());
		Assert.assertEquals(IBAN, adventure.getIBAN());
		Assert.assertEquals(300, adventure.getAmount());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getBankPayment());
		Assert.assertNull(adventure.getActivityBooking());
		Assert.assertNull(adventure.getRoomBooking());
	}

	@Test(expected = BrokerException.class)
	public void nullBroker() {
		new Adventure(null, this.begin, this.end, AGE, IBAN, AMOUNT);
	}

	@Test(expected = BrokerException.class)
	public void nullBegin() {
		new Adventure(this.broker, null, this.end, AGE, IBAN, AMOUNT);
	}

	@Test(expected = BrokerException.class)
	public void nullEnd() {
		new Adventure(this.broker, this.begin, null, AGE, IBAN, AMOUNT);
	}

	@Test(expected = BrokerException.class)
	public void lessThan18Age() {
		new Adventure(this.broker, this.begin, this.end, 17, IBAN, AMOUNT);
	}

	@Test(expected = BrokerException.class)
	public void over100() {
		new Adventure(this.broker, this.begin, this.end, 101, IBAN, AMOUNT);
	}

	@Test(expected = BrokerException.class)
	public void nullIBAN() {
		new Adventure(this.broker, this.begin, this.end, AGE, null, AMOUNT);
	}

	@Test(expected = BrokerException.class)
	public void emptyIBAN() {
		new Adventure(this.broker, this.begin, this.end, AGE, "     ", AMOUNT);
	}

	@Test(expected = BrokerException.class)
	public void negativeAmount() {
		new Adventure(this.broker, this.begin, this.end, AGE, IBAN, -100);
	}

	@Test(expected = BrokerException.class)
	public void zeroAmount() {
		new Adventure(this.broker, this.begin, this.end, AGE, IBAN, 0);
	}

	@Test(expected = BrokerException.class)
	public void inconsistentDates() {
		LocalDate endBefore = new LocalDate(2016, 12, 18);
		new Adventure(this.broker, this.begin, endBefore, AGE, IBAN, AMOUNT);
	}

	@After
	public void tearDown() {
		Broker.brokers.clear();
	}

}
