package pt.ulisboa.tecnico.softeng.broker.interfaces;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;

public class HotelInterface {
	public static String reserveHotel(Room.Type type, LocalDate arrival, LocalDate departure) {
		return Hotel.reserveHotel(type, arrival, departure);
	}
}
