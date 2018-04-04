package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class ActivityProvider extends ActivityProvider_Base {
	static final int CODE_SIZE = 6;

	private final String nif;
	private final String iban;

	private final Processor processor = new Processor();

	@Override
	public int getCounter() {
		int counter = super.getCounter() + 1;
		setCounter(counter);
		return counter;
	}

	public ActivityProvider(String code, String name, String nif, String iban) {
		checkArguments(code, name, nif, iban);

		setCode(code);
		setName(name);

		this.nif = nif;
		this.iban = iban;

		FenixFramework.getDomainRoot().addActivityProvider(this);
	}

	public void delete() {
		setRoot(null);

		for (Activity activity : getActivitySet()) {
			activity.delete();
		}

		deleteDomainObject();
	}

	private void checkArguments(String code, String name, String nif, String iban) {
		if (code == null || name == null || code.trim().equals("") || name.trim().equals("") || nif == null
				|| nif.trim().length() == 0 || iban == null || iban.trim().length() == 0) {
			throw new ActivityException();
		}

		if (code.length() != ActivityProvider.CODE_SIZE) {
			throw new ActivityException();
		}

		for (ActivityProvider activityProvider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			if (activityProvider.getCode().equals(code) || activityProvider.getName().equals(name)) {
				throw new ActivityException();
			}
		}

		for (ActivityProvider activityProvider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			if (activityProvider.getNif().equals(nif)) {
				throw new ActivityException();
			}
		}
	}

	public String getNif() {
		return this.nif;
	}

	public String getIban() {
		return this.iban;
	}

	public Processor getProcessor() {
		return this.processor;
	}

	public List<ActivityOffer> findOffer(LocalDate begin, LocalDate end, int age) {
		List<ActivityOffer> result = new ArrayList<>();
		for (Activity activity : getActivitySet()) {
			result.addAll(activity.getOffers(begin, end, age));
		}
		return result;
	}

	private Booking getBooking(String reference) {
		for (Activity activity : getActivitySet()) {
			Booking booking = activity.getBooking(reference);
			if (booking != null) {
				return booking;
			}
		}
		return null;
	}

	private static Booking getBookingByReference(String reference) {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			Booking booking = provider.getBooking(reference);
			if (booking != null) {
				return booking;
			}
		}
		return null;
	}

	public static String reserveActivity(LocalDate begin, LocalDate end, int age, String nif, String iban) {
		List<ActivityOffer> offers;
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			offers = provider.findOffer(begin, end, age);
			if (!offers.isEmpty()) {
				Booking booking = new Booking(provider, offers.get(0), nif, iban);
				provider.getProcessor().submitBooking(booking);
				return booking.getReference();
			}
		}
		throw new ActivityException();
	}

	public static String cancelReservation(String reference) {
		Booking booking = getBookingByReference(reference);
		if (booking != null) {
			return booking.cancel();
		}
		throw new ActivityException();
	}

	public static ActivityReservationData getActivityReservationData(String reference) {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			for (Activity activity : provider.getActivitySet()) {
				for (ActivityOffer offer : activity.getActivityOfferSet()) {
					Booking booking = offer.getBooking(reference);
					if (booking != null) {
						return new ActivityReservationData(provider, offer, booking);
					}
				}
			}
		}
		throw new ActivityException();
	}

}
