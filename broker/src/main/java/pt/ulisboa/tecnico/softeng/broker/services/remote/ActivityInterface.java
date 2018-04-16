package pt.ulisboa.tecnico.softeng.broker.services.remote;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects.ActivityReservationData;

public class ActivityInterface {
	public static String reserveActivity(LocalDate begin, LocalDate end, int age, String nif, String iban) {
		// return ActivityProvider.reserveActivity(begin, end, age, nif, iban);
		// TODO: implement in the final version as a rest invocation
		return null;
	}

	public static String cancelReservation(String activityConfirmation) {
		// return ActivityProvider.cancelReservation(activityConfirmation);
		// TODO: implement in the final version as a rest invocation
		return null;
	}

	public static ActivityReservationData getActivityReservationData(String reference) {
		// return ActivityProvider.getActivityReservationData(reference);
		// TODO: implement in the final version as a rest invocation
		return null;
	}

}
