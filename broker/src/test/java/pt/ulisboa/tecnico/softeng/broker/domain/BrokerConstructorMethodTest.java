package pt.ulisboa.tecnico.softeng.broker.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class BrokerConstructorMethodTest {
	private static final String CODE = "BR01";
	private static final String NAME = "WeExplore";
	private static final String BROKER_IBAN = "BROKER_IBAN";
	private static final String NIF_AS_BUYER = "buyerNIF";
	private static final String NIF_AS_SELLER = "sellerNIF";

	@Test
	public void success() {
		Broker broker = new Broker(CODE, NAME, NIF_AS_SELLER, NIF_AS_BUYER, BROKER_IBAN);

		Assert.assertEquals(CODE, broker.getCode());
		Assert.assertEquals(NAME, broker.getName());
		Assert.assertEquals(0, broker.getNumberOfAdventures());
		Assert.assertTrue(Broker.brokers.contains(broker));
	}

	@Test
	public void nullCode() {
		try {
			new Broker(null, NAME, NIF_AS_SELLER, NIF_AS_BUYER, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyCode() {
		try {
			new Broker("", NAME, NIF_AS_SELLER, NIF_AS_BUYER, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void blankCode() {
		try {
			new Broker("  ", NAME, NIF_AS_SELLER, NIF_AS_BUYER, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void uniqueCode() {
		Broker broker = new Broker(CODE, NAME, NIF_AS_SELLER, NIF_AS_BUYER, BROKER_IBAN);

		try {
			new Broker(CODE, "WeExploreX", NIF_AS_SELLER, NIF_AS_BUYER, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(1, Broker.brokers.size());
			Assert.assertTrue(Broker.brokers.contains(broker));
		}
	}

	@Test
	public void nullName() {
		try {
			new Broker(CODE, null, NIF_AS_SELLER, NIF_AS_BUYER, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyName() {
		try {
			new Broker(CODE, "", NIF_AS_SELLER, NIF_AS_BUYER, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void blankName() {
		try {
			new Broker(CODE, "    ", NIF_AS_SELLER, NIF_AS_BUYER, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void nullSellerNIF() {
		try {
			new Broker(CODE, NAME, null, NIF_AS_BUYER, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test

	public void emptySellerNIF() {
		try {
			new Broker(CODE, NAME, "    ", NIF_AS_BUYER, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test

	public void uniqueSellerNIF() {
		new Broker(CODE, NAME, NIF_AS_SELLER, "123456789", BROKER_IBAN);
		try {
			new Broker(CODE, NAME, NIF_AS_SELLER, NIF_AS_BUYER, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(1, Broker.brokers.size());
		}
	}

	@Test
	public void nullBuyerNIF() {
		try {
			new Broker(CODE, NAME, NIF_AS_SELLER, null, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyBuyerNIF() {
		try {
			new Broker(CODE, NAME, NIF_AS_SELLER, "   ", BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void uniqueBuyerNIFOne() {
		new Broker(CODE, NAME, NIF_AS_SELLER, NIF_AS_BUYER, BROKER_IBAN);
		try {
			new Broker(CODE, NAME, "123456789", NIF_AS_BUYER, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(1, Broker.brokers.size());
		}
	}

	@Test
	public void uniqueBuyerSellerNIFTwo() {
		new Broker(CODE, NAME, NIF_AS_SELLER, NIF_AS_BUYER, BROKER_IBAN);
		try {
			new Broker(CODE, NAME, NIF_AS_BUYER, "123456789", BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(1, Broker.brokers.size());
		}
	}

	@Test
	public void nullIBAN() {
		try {
			new Broker(CODE, NAME, NIF_AS_SELLER, NIF_AS_BUYER, null);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyIBAN() {
		try {
			new Broker(CODE, NAME, NIF_AS_SELLER, NIF_AS_BUYER, "    ");
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void sellerNIFDifferentBuyerNIF() {
		try {
			new Broker(CODE, NAME, NIF_AS_SELLER, NIF_AS_SELLER, BROKER_IBAN);
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
