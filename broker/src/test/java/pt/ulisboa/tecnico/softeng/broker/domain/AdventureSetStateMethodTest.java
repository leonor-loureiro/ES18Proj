package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class AdventureSetStateMethodTest {

	private static final int age = 18;
	private static final int amount = 1;
	private static final LocalDate begin = new LocalDate(2018, 3, 10);
	private static final LocalDate end = new LocalDate(2018, 3, 15);
	private static final String IBAN = "PT50";

	private Broker broker;
	private Adventure adv;

	@Before
	public void setUp() {
		broker = new Broker("BRK001", "FaialTurism");
		adv = new Adventure(broker, begin, end, age, IBAN, amount);
	}

	@Test
	public void setAllStates() {
		for (State st : State.values()) {
			try {
				adv.setState(st);
				Assert.assertTrue(adv.getState() == st);
			} catch (BrokerException be) {
				Assert.fail();
			}
		}
	}

	 @After
	 public void tearDown() {
		 Broker.brokers.clear();
	 }
}