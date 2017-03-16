package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;

@RunWith(JMockit.class)
public class CancelledStateProcessMethodTest {
	private static final String PAYMENT_CANCELLATION = "paymentConfirmation";
	private static final String IBAN = "BK01987654321";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private CancelledState cancelledState;

	@Before
	public void setUp() {
		this.cancelledState = new CancelledState();
	}

	@Test
	public void doesNotChangeState(@Mocked final Adventure adventure, @Mocked final BankInterface bankInterface,
			@Mocked final HotelInterface hotelInterface, @Mocked final ActivityInterface activityInterface) {
		new Expectations() {
			{
				adventure.getPaymentCancellation();
				this.result = PAYMENT_CANCELLATION;

				adventure.getActivityCancellation();
				this.result = null;

				adventure.getRoomCancellation();
				this.result = null;

				BankInterface.getOperationData(this.anyString);

				BankInterface.getOperationData(PAYMENT_CANCELLATION);
			}
		};

		this.cancelledState.process(adventure);

		Assert.assertEquals(Adventure.State.CANCELLED, this.cancelledState.getState());
	}

}
