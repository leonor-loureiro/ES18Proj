package pt.ulisboa.tecnico.softeng.broker.services.remote;

import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects.RoomBookingData;

public class HotelInterface {
	public static enum Type {
		SINGLE, DOUBLE
	}

	public static String reserveRoom(Type type, LocalDate arrival, LocalDate departure, String buyerNif,
			String buyerIban) {
		// return Hotel.reserveRoom(type, arrival, departure, buyerNif, buyerIban);
		// TODO: implement in the final version as a rest invocation
		return null;
	}

	public static String cancelBooking(String roomConfirmation) {
		// return Hotel.cancelBooking(roomConfirmation);
		// TODO: implement in the final version as a rest invocation
		return null;
	}

	public static RoomBookingData getRoomBookingData(String reference) {
		// return Hotel.getRoomBookingData(reference);
		// TODO: implement in the final version as a rest invocation
		return null;
	}

	public static Set<String> bulkBooking(int number, LocalDate arrival, LocalDate departure, String buyerNif,
			String buyerIban) {
		// return Hotel.bulkBooking(number, arrival, departure, buyerNif, buyerIban);
		// TODO: implement in the final version as a rest invocation
		return null;
	}

}
