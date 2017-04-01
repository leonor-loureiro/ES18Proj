package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class ActivityOffer extends ActivityOffer_Base {
	private final Set<Booking> bookings = new HashSet<>();

	public ActivityOffer(Activity activity, LocalDate begin, LocalDate end) {
		checkArguments(activity, begin, end);

		setBegin(begin);
		setEnd(end);
		setCapacity(activity.getCapacity());

		setActivity(activity);
	}

	public void delete() {
		setActivity(null);

		deleteDomainObject();
	}

	private void checkArguments(Activity activity, LocalDate begin, LocalDate end) {
		if (activity == null || begin == null || end == null) {
			throw new ActivityException();
		}

		if (end.isBefore(begin)) {
			throw new ActivityException();
		}
	}

	int getNumberActiveOfBookings() {
		int count = 0;
		for (Booking booking : this.bookings) {
			if (!booking.isCancelled()) {
				count++;
			}
		}
		return count;
	}

	void addBooking(Booking booking) {
		if (getCapacity() == getNumberActiveOfBookings()) {
			throw new ActivityException();
		}

		this.bookings.add(booking);

	}

	boolean available(LocalDate begin, LocalDate end) {
		return hasVacancy() && matchDate(begin, end);
	}

	boolean matchDate(LocalDate begin, LocalDate end) {
		if (begin == null || end == null) {
			throw new ActivityException();
		}

		return begin.equals(getBegin()) && end.equals(getEnd());
	}

	boolean hasVacancy() {
		return getCapacity() > getNumberActiveOfBookings();
	}

	public Booking getBooking(String reference) {
		for (Booking booking : this.bookings) {
			if (booking.getReference().equals(reference)
					|| (booking.isCancelled() && booking.getCancellation().equals(reference))) {
				return booking;
			}
		}
		return null;
	}

}
