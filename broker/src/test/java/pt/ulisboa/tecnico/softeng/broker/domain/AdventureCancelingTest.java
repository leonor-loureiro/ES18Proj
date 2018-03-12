package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class AdventureCancelingTest {
	
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
	public void cancelRoomTest() {
		adv.setRoomConfirmation("NotNull");
		adv.setRoomCancellation(null);
		Assert.assertTrue(adv.cancelRoom());
		
		adv.setRoomConfirmation(null);
		adv.setRoomCancellation(null);
		Assert.assertFalse(adv.cancelRoom());
		
		adv.setRoomConfirmation("NotNull");
		adv.setRoomCancellation("NotNull");
		Assert.assertFalse(adv.cancelRoom());
		
		adv.setRoomConfirmation(null);
		adv.setRoomCancellation("NotNull");
		Assert.assertFalse(adv.cancelRoom());
	}
	
	@Test
	public void cancelActivityTest() {
		adv.setActivityConfirmation("NotNull");
		adv.setActivityCancellation(null);
		Assert.assertTrue(adv.cancelActivity());
		
		adv.setActivityConfirmation(null);
		adv.setActivityCancellation(null);
		Assert.assertFalse(adv.cancelActivity());
		
		adv.setActivityConfirmation("NotNull");
		adv.setActivityCancellation("NotNull");
		Assert.assertFalse(adv.cancelActivity());
		
		adv.setActivityConfirmation(null);
		adv.setActivityCancellation("NotNull");
		Assert.assertFalse(adv.cancelActivity());
	}
	
	@Test
	public void cancelPaymentTest() {
		adv.setPaymentConfirmation("NotNull");
		adv.setPaymentCancellation(null);
		Assert.assertTrue(adv.cancelPayment());
		
		adv.setPaymentConfirmation(null);
		adv.setPaymentCancellation(null);
		Assert.assertFalse(adv.cancelPayment());
		
		adv.setPaymentConfirmation("NotNull");
		adv.setPaymentCancellation("NotNull");
		Assert.assertFalse(adv.cancelPayment());
		
		adv.setPaymentConfirmation(null);
		adv.setPaymentCancellation("NotNull");
		Assert.assertFalse(adv.cancelPayment());
	}
	
	@After
	public void tearDown() {
		Broker.brokers.clear();
	}
}
