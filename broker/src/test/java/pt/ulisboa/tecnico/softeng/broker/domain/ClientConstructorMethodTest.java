package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class ClientConstructorMethodTest {
	private static final String BROKER_IBAN = "BROKER_IBAN";
	private static final String BUYER_NIF = "buyerNIF";
	private static final String SELLER_NIF = "sellerNIF";
	private static final String NIF = "NIF";
	private static final String IBAN = "IBAN";
	private static final int age = 35;
	private Broker broker;

	@Before
	public void setUp() {
		this.broker = new Broker("BR01", "eXtremeADVENTURE", SELLER_NIF, BUYER_NIF, BROKER_IBAN);
	}

	@Test
	public void success() {
		Client client = new Client(this.broker, IBAN, NIF, age);

		Assert.assertEquals(IBAN, client.getIBAN());
		Assert.assertEquals(NIF, client.getNIF());
		Assert.assertEquals(age, client.getAge());
	}

	@Test(expected = BrokerException.class)
	public void nullBroker() {
		new Client(null, IBAN, NIF, age);
	}

	@Test(expected = BrokerException.class)
	public void nullIBAN() {
		new Client(this.broker, null, NIF, age);
	}

	@Test(expected = BrokerException.class)
	public void emptyOrBlanIBAN() {
		new Client(this.broker, "   ", NIF, age);
	}

	@Test(expected = BrokerException.class)
	public void nullNIF() {
		new Client(this.broker, IBAN, null, age);
	}

	@Test(expected = BrokerException.class)
	public void emptyOrBlankNIF() {
		new Client(this.broker, IBAN, "    ", age);
	}

	@Test(expected = BrokerException.class)
	public void negativeAge() {
		new Client(this.broker, IBAN, NIF, -1);
	}

	public void clientExistsWithSameIBAN() {
		new Client(this.broker, IBAN, NIF, age);
		new Client(this.broker, IBAN, "OTHER_NIF", age);
	}

	public void clientExistsWithSameNIF() {
		Client client = new Client(this.broker, IBAN, NIF, age);
		try {
			new Client(this.broker, "OTHER_IBAN", NIF, age);
			fail();
		} catch (BrokerException br) {
			assertEquals(client, this.broker.getClientByNIF(NIF));
		}
	}

	@After
	public void tearDown() {
		Broker.brokers.clear();
	}

}
