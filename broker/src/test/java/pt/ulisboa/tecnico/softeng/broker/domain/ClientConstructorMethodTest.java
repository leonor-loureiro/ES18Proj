package pt.ulisboa.tecnico.softeng.broker.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class ClientConstructorMethodTest {
	
	private static final String CODE = "BR01";
	private static final String NAME = "eXtremeADVENTURE";
	private static final String NIF_AS_BUYER  = "111111111";
	private static final String NIF_OF_CLIENT = "333333333";
	private static final String IBAN_BROKER = "BK011234567";
	
	private static final int GOOD_AGE     = 20;
	private static final int YOUNG_AGE    = 17;
	private static final int GOOD_AGE_BOT = 18;
	private static final int GOOD_AGE_TOP = 100;
	private static final int OLD_AGE      = 101;
	
	private static final String NULL_STR = null;
	private static final String EMPTY_STR = "";
	private static final String BLANK_STR = "     ";
	
	private static final String DR_L        = "A1";
	private static final String IBAN_CLIENT = "BK011234567";
	private static final String NIF_AS_SELLER = "222222222";	
	private Broker broker;
	
	@Before
	public void setUp() {
		this.broker = new Broker(CODE, NAME, NIF_AS_SELLER, NIF_AS_BUYER, IBAN_BROKER);
	}
	
	@Test
	public void success() {
		Client client = new Client(this.broker, IBAN_CLIENT, NIF_OF_CLIENT, DR_L, GOOD_AGE);
		
		Assert.assertEquals(client.getNif(), NIF_OF_CLIENT);		
		Assert.assertEquals(client.getIban(), IBAN_CLIENT);		
		Assert.assertEquals(client.getAge(), GOOD_AGE);
		Assert.assertEquals(client.getBroker(), this.broker);
		Assert.assertEquals(client.getDrivingLicense(), DR_L);
	}
	
	@Test(expected = BrokerException.class)
	public void nullBroker() {
		new Client(null, IBAN_CLIENT, NIF_OF_CLIENT, DR_L, GOOD_AGE);
	}

	@Test(expected = BrokerException.class)
	public void nullIBAN() {
		new Client(this.broker, NULL_STR, NIF_OF_CLIENT, DR_L, GOOD_AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void emptyIBAN() {
		new Client(this.broker, EMPTY_STR, NIF_OF_CLIENT, DR_L, GOOD_AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void blankIBAN() {
		new Client(this.broker, BLANK_STR, NIF_OF_CLIENT, DR_L, GOOD_AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void nullNIF() {
		new Client(this.broker, IBAN_CLIENT, NULL_STR, DR_L, GOOD_AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void emptyNIF() {
		new Client(this.broker, IBAN_CLIENT, EMPTY_STR, DR_L, GOOD_AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void blankNIF() {
		new Client(this.broker, IBAN_CLIENT, BLANK_STR, DR_L, GOOD_AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void nullDR_L() {
		new Client(this.broker, IBAN_CLIENT, NIF_OF_CLIENT, NULL_STR, GOOD_AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void emptyDR_L() {
		new Client(this.broker, IBAN_CLIENT, NIF_OF_CLIENT, EMPTY_STR, GOOD_AGE);
	}
	@Test(expected = BrokerException.class)
	public void blankDR_L() {
		new Client(this.broker, IBAN_CLIENT, NIF_OF_CLIENT, BLANK_STR, GOOD_AGE);
	}
	
	public void successEqual18() {
		Client client = new Client(this.broker, IBAN_CLIENT, NIF_OF_CLIENT, DR_L, GOOD_AGE_BOT);
		
		Assert.assertEquals(client.getNif(), NIF_OF_CLIENT);		
		Assert.assertEquals(client.getIban(), IBAN_CLIENT);		
		Assert.assertEquals(client.getAge(), GOOD_AGE);
		Assert.assertEquals(client.getBroker(), this.broker);
		Assert.assertEquals(client.getDrivingLicense(), DR_L);
	}
	
	public void successEqual100() {
		Client client = new Client(this.broker, IBAN_CLIENT, NIF_OF_CLIENT, DR_L, GOOD_AGE_TOP);
		
		Assert.assertEquals(client.getNif(), NIF_OF_CLIENT);		
		Assert.assertEquals(client.getIban(), IBAN_CLIENT);		
		Assert.assertEquals(client.getAge(), GOOD_AGE);
		Assert.assertEquals(client.getBroker(), this.broker);
		Assert.assertEquals(client.getDrivingLicense(), DR_L);
	}
	
	@Test(expected = BrokerException.class)
	public void failLess18() {
		new Client(this.broker, IBAN_CLIENT, NIF_OF_CLIENT, DR_L, YOUNG_AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void failOver100() {
		new Client(this.broker, IBAN_CLIENT, NIF_OF_CLIENT, DR_L, OLD_AGE);
	}
	
	@After
	public void tearDown() {
		Broker.brokers.clear();
	}
}
