package pt.ulisboa.tecnico.softeng.activity.services.local;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.activity.domain.Booking;
import pt.ulisboa.tecnico.softeng.activity.domain.RollbackTestAbstractClass;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.services.local.ActivityInterface;

public class ActivityInterfaceCancelReservationMethodTest extends RollbackTestAbstractClass {
	private ActivityProvider provider;
	private ActivityOffer offer;

	@Override
	public void populate4Test() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		Activity activity = new Activity(this.provider, "Bush Walking", 18, 80, 3);

		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		this.offer = new ActivityOffer(activity, begin, end);
	}

	@Test
	public void success() {
		Booking booking = new Booking(this.offer);

		String cancel = ActivityInterface.cancelReservation(booking.getReference());

		assertTrue(booking.isCancelled());
		assertEquals(cancel, booking.getCancel());
	}

	@Test(expected = ActivityException.class)
	public void doesNotExist() {
		new Booking(this.offer);

		ActivityInterface.cancelReservation("XPTO");
	}

}
