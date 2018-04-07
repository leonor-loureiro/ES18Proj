package pt.ulisboa.tecnico.softeng.broker.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class ClientConstructorMethodTest {
	
	private static final int OLD_AGE      = 101;
	private static final int GOOD_AGE     = 20;
	private static final int YOUNG_AGE    = 100;
	private static final int GOOD_AGE_BOT = 18;
	private static final int GOOD_AGE_TOP = 100;
	
	private static final String DR_L      = "A1";
	private static final String IBAN      = "BK011234567";
	private static final String NIF       = "444444444";
	
	private Broker broker;
	
	@Before
	public void setUp() {
		this.broker = new Broker("BR01", "eXtremeADVENTURE");
	}
	
	@Test
	public void success() {
		Client client = new Client(this.broker, IBAN, NIF, DR_L, GOOD_AGE);
		
		Assert.assertEquals(client.getNif(), NIF);		
		Assert.assertEquals(client.getIban(), IBAN);		
		Assert.assertEquals(client.getAge(), GOOD_AGE);
		Assert.assertEquals(client.getBroker(), this.broker);
		Assert.assertEquals(client.getDrivingLicense(), DR_L);
	}
	
	@Test(expected = BrokerException.class)
	public void nullBroker() {
		new Client(null, IBAN, NIF, DR_L, GOOD_AGE);
	}

	@Test(expected = BrokerException.class)
	public void nullIBAN() {
		new Client(this.broker, null, NIF, DR_L, GOOD_AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void nullNIF() {
		new Client(this.broker, IBAN, null, DR_L, GOOD_AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void nullDR_L() {
		new Client(this.broker, IBAN, NIF, null, GOOD_AGE);
	}
	
	public void successEqual18() {
		Client client = new Client(this.broker, IBAN, NIF, DR_L, GOOD_AGE_BOT);
		
		Assert.assertEquals(client.getNif(), NIF);		
		Assert.assertEquals(client.getIban(), IBAN);		
		Assert.assertEquals(client.getAge(), GOOD_AGE);
		Assert.assertEquals(client.getBroker(), this.broker);
		Assert.assertEquals(client.getDrivingLicense(), DR_L);
	}
	
	public void successEqual100() {
		Client client = new Client(this.broker, IBAN, NIF, DR_L, GOOD_AGE_TOP);
		
		Assert.assertEquals(client.getNif(), NIF);		
		Assert.assertEquals(client.getIban(), IBAN);		
		Assert.assertEquals(client.getAge(), GOOD_AGE);
		Assert.assertEquals(client.getBroker(), this.broker);
		Assert.assertEquals(client.getDrivingLicense(), DR_L);
	}
	
	@Test(expected = BrokerException.class)
	public void successLess18() {
		new Client(this.broker, IBAN, NIF, DR_L, YOUNG_AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void successOver100() {
		new Client(this.broker, IBAN, NIF, DR_L, OLD_AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void emptyIBAN() {
		new Client(this.broker, "       ", NIF, DR_L, GOOD_AGE_BOT);
	}
	
	@Test(expected = BrokerException.class)
	public void emptyNIF() {
		new Client(this.broker, IBAN, "       ", DR_L, GOOD_AGE_BOT);
	}
	
	@Test(expected = BrokerException.class)
	public void emptyDR_L() {
		new Client(this.broker, IBAN, NIF, "      ", GOOD_AGE_BOT);
	}
}
