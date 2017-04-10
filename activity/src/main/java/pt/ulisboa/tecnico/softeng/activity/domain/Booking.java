package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class Booking extends Booking_Base {

	public Booking(ActivityOffer offer) {
		checkArguments(offer);

		setReference(offer.getActivity().getActivityProvider().getCode()
				+ Integer.toString(offer.getActivity().getActivityProvider().getCounter()));

		setActivityOffer(offer);
	}

	public void delete() {
		setActivityOffer(null);

		deleteDomainObject();
	}

	private void checkArguments(ActivityOffer offer) {
		if (offer == null) {
			throw new ActivityException();
		}

		if (offer.getCapacity() == offer.getNumberActiveOfBookings()) {
			throw new ActivityException();
		}
	}

	public String cancel() {
		setCancel("CANCEL" + getReference());
		setCancellationDate(new LocalDate());
		return getCancel();
	}

	public boolean isCancelled() {
		return getCancel() != null;
	}

}
