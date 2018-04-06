package pt.ulisboa.tecnico.softeng.broker.interfaces;

import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;

public class HotelInterface {
	public static String reserveRoom(Room.Type type, LocalDate arrival, LocalDate departure) {
		return Hotel.reserveRoom(type, arrival, departure, "123456786", "ES06123456785"); //FIXME LAST ARGUMENTS TEMPORARY STUB
	}

	public static String cancelBooking(String roomConfirmation) {
		return Hotel.cancelBooking(roomConfirmation);
	}

	public static RoomBookingData getRoomBookingData(String reference) {
		return Hotel.getRoomBookingData(reference);
	}

	public static Set<String> bulkBooking(int number, LocalDate arrival, LocalDate departure) {
		return Hotel.bulkBooking(number, arrival, departure, "123456788", "ES06123456787"); //FIXME LAST ARGUMENTS TEMPORARY STUB
	}
}
