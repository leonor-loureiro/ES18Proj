package pt.ulisboa.tecnico.softeng.broker.services.remote;

import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects.RoomBookingData;

public class HotelInterface {
	public static enum RoomType {
		SINGLE, DOUBLE
	}

	public static String reserveRoom(RoomType type, LocalDate arrival, LocalDate departure) {
		// TODO: implement in the final version as a rest invocation
		return null;
	}

	public static String cancelBooking(String roomConfirmation) {
		// TODO: implement in the final version as a rest invocation
		return null;
	}

	public static RoomBookingData getRoomBookingData(String reference) {
		// TODO: implement in the final version as a rest invocation
		return null;
	}

	public static Set<String> bulkBooking(int number, LocalDate arrival, LocalDate departure) {
		// TODO: implement in the final version as a rest invocation
		return null;
	}
}
