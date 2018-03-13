package pt.ulisboa.tecnico.softeng.broker.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class BrokerConstructorMethodTest {
	private static final String CODE = "BR01";
	private static final String NAME = "WeExplore";
	private static final String BROKER_IBAN = "BROKER_IBAN";
	private static final String BUYER_NIF = "buyerNIF";
	private static final String SELLER_NIF = "sellerNIF";

	@Test
	public void success() {
		Broker broker = new Broker(CODE, NAME, SELLER_NIF, BUYER_NIF, BROKER_IBAN);

		Assert.assertEquals(CODE, broker.getCode());
		Assert.assertEquals(NAME, broker.getName());
		Assert.assertEquals(0, broker.getNumberOfAdventures());
		Assert.assertTrue(Broker.brokers.contains(broker));
	}

	@Test
	public void nullCode() {
		try {
			new Broker(null, NAME, SELLER_NIF, BUYER_NIF, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyCode() {
		try {
			new Broker("", NAME, SELLER_NIF, BUYER_NIF, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void blankCode() {
		try {
			new Broker("  ", NAME, SELLER_NIF, BUYER_NIF, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void uniqueCode() {
		Broker broker = new Broker(CODE, NAME, SELLER_NIF, BUYER_NIF, BROKER_IBAN);

		try {
			new Broker(CODE, "WeExploreX", SELLER_NIF, BUYER_NIF, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(1, Broker.brokers.size());
			Assert.assertTrue(Broker.brokers.contains(broker));
		}
	}

	@Test
	public void nullName() {
		try {
			new Broker(CODE, null, SELLER_NIF, BUYER_NIF, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyName() {
		try {
			new Broker(CODE, "", SELLER_NIF, BUYER_NIF, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void blankName() {
		try {
			new Broker(CODE, "    ", SELLER_NIF, BUYER_NIF, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void nullSellerNIF() {
		try {
			new Broker(CODE, NAME, null, BUYER_NIF, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptySellerNIF() {
		try {
			new Broker(CODE, NAME, "    ", BUYER_NIF, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void uniqueSellerNIF() {
		new Broker(CODE, NAME, SELLER_NIF, "123456789", BROKER_IBAN);
		try {
			new Broker(CODE, NAME, SELLER_NIF, BUYER_NIF, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(1, Broker.brokers.size());
		}
	}

	@Test
	public void nullBuyerNIF() {
		try {
			new Broker(CODE, NAME, SELLER_NIF, null, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyBuyerNIF() {
		try {
			new Broker(CODE, NAME, SELLER_NIF, "   ", BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void uniqueBuyerNIFOne() {
		new Broker(CODE, NAME, SELLER_NIF, BUYER_NIF, BROKER_IBAN);
		try {
			new Broker(CODE, NAME, "123456789", BUYER_NIF, BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(1, Broker.brokers.size());
		}
	}

	@Test
	public void uniqueBuyerSellerNIFTwo() {
		new Broker(CODE, NAME, SELLER_NIF, BUYER_NIF, BROKER_IBAN);
		try {
			new Broker(CODE, NAME, BUYER_NIF, "123456789", BROKER_IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(1, Broker.brokers.size());
		}
	}

	@Test
	public void nullIBAN() {
		try {
			new Broker(CODE, NAME, SELLER_NIF, BUYER_NIF, null);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyIBAN() {
		try {
			new Broker(CODE, NAME, SELLER_NIF, BUYER_NIF, "    ");
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void sellerNIFDifferentBuyerNIF() {
		try {
			new Broker(CODE, NAME, SELLER_NIF, SELLER_NIF, BROKER_IBAN);
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
