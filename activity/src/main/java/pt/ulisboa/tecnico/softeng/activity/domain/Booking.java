package pt.ulisboa.tecnico.softeng.activity.domain;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class Booking {
	private static int counter = 0;

	private final String reference;
	private String cancel;

	public Booking(ActivityProvider provider, ActivityOffer offer) {
		checkArguments(provider, offer);

		this.reference = provider.getCode() + Integer.toString(++Booking.counter);

		offer.addBooking(this);
	}

	private void checkArguments(ActivityProvider provider, ActivityOffer offer) {
		if (provider == null || offer == null) {
			throw new ActivityException();
		}
	}

	public String getReference() {
		return this.reference;
	}

	public String getCancel() {
		return this.cancel;
	}

	public String cancel() {
		this.cancel = "CANCEL" + this.reference;
		return this.cancel;
	}

	public boolean isCancelled() {
		return this.cancel != null;
	}
}
