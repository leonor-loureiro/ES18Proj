package pt.ulisboa.tecnico.softeng.activity.domain;

public class Booking {
	private static int counter = 0;

	private final String reference;

	public Booking(ActivityProvider provider, ActivityOffer offer) {
		this.reference = provider.getCode() + Integer.toString(++Booking.counter);

		offer.addBooking(this);
	}

	public String getReference() {
		return this.reference;
	}
}
