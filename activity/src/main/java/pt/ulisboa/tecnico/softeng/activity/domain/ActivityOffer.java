package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class ActivityOffer {
	private final LocalDate begin;
	private final LocalDate end;
	private final int capacity;
	private final int amount;
	private final Set<Booking> bookings = new HashSet<>();

	public ActivityOffer(Activity activity, LocalDate begin, LocalDate end, int amount) {
		checkArguments(activity, begin, end, amount);

		this.begin = begin;
		this.end = end;
		this.capacity = activity.getCapacity();
		this.amount = amount;

		activity.addOffer(this);
	}

	private void checkArguments(Activity activity, LocalDate begin, LocalDate end, int amount) {
		if (activity == null || begin == null || end == null) {
			throw new ActivityException();
		}

		if (end.isBefore(begin)) {
			throw new ActivityException();
		}

		if (amount < 1) {
			throw new ActivityException();
		}
	}

	public LocalDate getBegin() {
		return this.begin;
	}

	public LocalDate getEnd() {
		return this.end;
	}

	public int getPrice() {
		return this.amount;
	}

	int getNumberOfBookings() {
		int count = 0;
		for (Booking booking : this.bookings) {
			if (!booking.isCancelled()) {
				count++;
			}
		}
		return count;
	}

	void addBooking(Booking booking) {
		if (this.capacity == getNumberOfBookings()) {
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
		return this.capacity > getNumberOfBookings();
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
