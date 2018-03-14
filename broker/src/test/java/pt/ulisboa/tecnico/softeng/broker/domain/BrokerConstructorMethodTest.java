package pt.ulisboa.tecnico.softeng.broker.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class BrokerConstructorMethodTest {
	private static final String CODE = "BR01";
	private static final String NAME = "WeExplore";
	private static final String BROKER_IBAN = "BROKER_IBAN";
	private static final String NIF = "buyerNIF";
	private static final String SELLER_NIF = "sellerNIF";

	@Test
	public void success() {
		Broker broker = new Broker(CODE, NAME, NIF, BROKER_IBAN);

		Assert.assertEquals(CODE, broker.getCode());
		Assert.assertEquals(NAME, broker.getName());
		Assert.assertEquals(0, broker.getNumberOfAdventures());
		Assert.assertTrue(Broker.brokers.contains(broker));
	}

	@Test
	public void nullCode() {
		try {
			new Broker(null, NAME, NIF, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyCode() {
		try {
			new Broker("", NAME, NIF, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void blankCode() {
		try {
			new Broker("  ", NAME, NIF, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void uniqueCode() {
		Broker broker = new Broker(CODE, NAME, NIF, BROKER_IBAN);

		try {
			new Broker(CODE, "WeExploreX", NIF, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(1, Broker.brokers.size());
			Assert.assertTrue(Broker.brokers.contains(broker));
		}
	}

	@Test
	public void nullName() {
		try {
			new Broker(CODE, null, NIF, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyName() {
		try {
			new Broker(CODE, "", NIF, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void blankName() {
		try {
			new Broker(CODE, "    ", NIF, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void nullNIF() {
		try {
			new Broker(CODE, NAME, null, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyNIF() {
		try {
			new Broker(CODE, NAME, "    ", BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void uniqueNIF() {
		new Broker(CODE, NAME, NIF, BROKER_IBAN);
		try {
			new Broker(CODE, NAME, NIF, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(1, Broker.brokers.size());
		}
	}

	@Test
	public void nullIBAN() {
		try {
			new Broker(CODE, NAME, NIF, null);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyIBAN() {
		try {
			new Broker(CODE, NAME, NIF, "    ");
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@After
	public void tearDown() {
		Broker.brokers.clear();
	}

}
