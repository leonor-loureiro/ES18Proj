package pt.ulisboa.tecnico.softeng.broker.interfaces;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.broker.dataobjects.ActivityReservationData;

public class ActivityInterface {
	public static String reserveActivity(LocalDate begin, LocalDate end, int age) {
		// TODO: implement in the final version as a rest invocation
		return null;
	}

	public static String cancelReservation(String activityConfirmation) {
		// TODO: implement in the final version as a rest invocation
		return null;
	}

	public static ActivityReservationData getActivityReservationData(String reference) {
		// TODO: implement in the final version as a rest invocation
		return null;
	}
}
