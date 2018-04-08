package pt.ulisboa.tecnico.softeng.broker.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class BrokerConstructorMethodTest {
	private static final String CODE1 = "BR01";
	private static final String CODE2 = "BR02";
	private static final String NAME = "WeExplore";
	private static final String NIF1 = "111111111";
	private static final String NIF2 = "222222222";
	private static final String NIF3 = "333333333";
	private static final String NIF4 = "444444444";
	private static final String IBAN1 = "BK011234567";
	private static final String IBAN2 = "BK022345678";
	private static final String NULL_STR = null;
	private static final String EMPTY_STR = "";
	private static final String BLANK_STR = "     ";
	
	
	
	@Test
	public void success() {
		Broker broker = new Broker(CODE1, NAME, NIF1, NIF2, IBAN1);

		Assert.assertEquals(CODE1, broker.getCode());
		Assert.assertEquals(NAME, broker.getName());
		Assert.assertEquals(NIF1, broker.getNIFSeller());
		Assert.assertEquals(NIF2, broker.getNIFBuyer());
		Assert.assertEquals(IBAN1, broker.getIBAN());
		
		
		Assert.assertEquals(0, broker.getNumberOfAdventures());
		Assert.assertTrue(Broker.brokers.contains(broker));
	}

	@Test
	public void nullCode() {
		try {
			new Broker(NULL_STR, NAME, NIF1, NIF2, IBAN1);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyCode() {
		try {
			new Broker(EMPTY_STR, NAME, NIF1, NIF2, IBAN1);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void blankCode() {
		try {
			new Broker(BLANK_STR, NAME, NIF1, NIF2, IBAN1);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void uniqueCode() {
		Broker broker = new Broker(CODE1, NAME, NIF1, NIF2, IBAN1);

		try {
			new Broker(CODE1, NAME, NIF3, NIF4, IBAN2);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(1, Broker.brokers.size());
			Assert.assertTrue(Broker.brokers.contains(broker));
		}
	}
	
	@Test
	public void uniqueNIFSeller() {
		Broker broker = new Broker(CODE1, NAME, NIF1, NIF2, IBAN1);

		try {
			new Broker(CODE2, NAME, NIF1, NIF3, IBAN2);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(1, Broker.brokers.size());
			Assert.assertTrue(Broker.brokers.contains(broker));
		}
	}
	
	@Test
	public void uniqueNIFBuyer() {
		Broker broker = new Broker(CODE1, NAME, NIF1, NIF2, IBAN1);

		try {
			new Broker(CODE2, NAME, NIF3, NIF2, IBAN2);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(1, Broker.brokers.size());
			Assert.assertTrue(Broker.brokers.contains(broker));
		}
	}
	
	@Test
	public void uniqueIBAN() {
		Broker broker = new Broker(CODE1, NAME, NIF1, NIF2, IBAN1);

		try {
			new Broker(CODE2, NAME, NIF3, NIF4, IBAN1);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(1, Broker.brokers.size());
			Assert.assertTrue(Broker.brokers.contains(broker));
		}
	}

	@Test
	public void nullName() {
		try {
			new Broker(CODE1, NULL_STR, NIF1, NIF2, IBAN1);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyName() {
		try {
			new Broker(CODE1, EMPTY_STR, NIF1, NIF2, IBAN1);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void blankName() {
		try {
			new Broker(CODE1, BLANK_STR, NIF1, NIF2, IBAN1);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}
	
	@Test
	public void nullNIFSeller() {
		try {
			new Broker(CODE1, NAME, NULL_STR, NIF2, IBAN1);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyNIFSeller() {
		try {
			new Broker(CODE1, NAME, EMPTY_STR, NIF2, IBAN1);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void blankNIFSeller() {
		try {
			new Broker(CODE1, NAME, BLANK_STR, NIF2, IBAN1);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void nullNIFBuyer() {
		try {
			new Broker(CODE1, NAME, NIF1, NULL_STR, IBAN1);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyNIFBuyer() {
		try {
			new Broker(CODE1, NAME, NIF1, EMPTY_STR, IBAN1);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void blankNIFBuyer() {
		try {
			new Broker(CODE1, NAME, NIF1, BLANK_STR, IBAN1);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}
	
	@Test
	public void nullIBAN() {
		try {
			new Broker(CODE1, NAME, NIF1, NIF2, NULL_STR);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyIBAN() {
		try {
			new Broker(CODE1, NAME, NIF1, NIF2, EMPTY_STR);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void blankIBAN() {
		try {
			new Broker(CODE1, NAME, NIF1, NIF2, BLANK_STR);
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
