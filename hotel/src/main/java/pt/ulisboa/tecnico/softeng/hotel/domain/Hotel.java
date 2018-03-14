package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Hotel {
	public static Set<Hotel> hotels = new HashSet<>();

	static final int CODE_SIZE = 7;

	private final String code;
	private final String name;
	private final String nif;
	private final String iban;

	private final Set<Room> rooms = new HashSet<>();

	public Hotel(String code, String name, String nif, String iban) {
		checkArguments(code, name, nif, iban);

		this.code = code;
		this.name = name;
		this.nif = nif;
		this.iban = iban;

		Hotel.hotels.add(this);
	}

	private void checkArguments(String code, String name, String nif, String iban) {
		if (code == null || name == null ||
                isEmpty(code) || isEmpty((name)) ||
                nif == null || isEmpty(nif) ||
                iban == null || isEmpty(iban)) {

			throw new HotelException();

		}

		if (code.length() != Hotel.CODE_SIZE) {
			throw new HotelException();
		}

		for (Hotel hotel : hotels) {
			if (hotel.getCode().equals(code)) {
				throw new HotelException();
			}
		}

		for (Hotel hotel : Hotel.hotels) {
			if (hotel.getNIF().equals(nif)) {
				throw new HotelException();
			}
		}
	}

	public Room hasVacancy(Room.Type type, LocalDate arrival, LocalDate departure) {
		if (type == null || arrival == null || departure == null) {
			throw new HotelException();
		}

		for (Room room : this.rooms) {
			if (room.isFree(type, arrival, departure)) {
				return room;
			}
		}
		return null;
	}

	public String getCode() {
		return this.code;
	}

	public String getName() {
		return this.name;
	}

	public String getNIF() {
	    return this.nif;
	}

	public String getIBAN() {
	    return this.iban;
    }

    private boolean isEmpty(String str) {
	    return str.trim().length() == 0;
    }

	void addRoom(Room room) {
		if (hasRoom(room.getNumber())) {
			throw new HotelException();
		}

		this.rooms.add(room);
	}

	int getNumberOfRooms() {
		return this.rooms.size();
	}

	public boolean hasRoom(String number) {
		for (Room room : this.rooms) {
			if (room.getNumber().equals(number)) {
				return true;
			}
		}
		return false;
	}

	private Booking getBooking(String reference) {
		for (Room room : this.rooms) {
			Booking booking = room.getBooking(reference);
			if (booking != null) {
				return booking;
			}
		}
		return null;
	}

	public static String reserveRoom(Room.Type type, LocalDate arrival, LocalDate departure) {
		for (Hotel hotel : Hotel.hotels) {
			Room room = hotel.hasVacancy(type, arrival, departure);
			if (room != null) {
				return room.reserve(type, arrival, departure).getReference();
			}
		}
		throw new HotelException();
	}

	public static String cancelBooking(String reference) {
		for (Hotel hotel : hotels) {
			Booking booking = hotel.getBooking(reference);
			if (booking != null) {
				return booking.cancel();
			}
		}
		throw new HotelException();
	}

	public static RoomBookingData getRoomBookingData(String reference) {
		for (Hotel hotel : hotels) {
			for (Room room : hotel.rooms) {
				Booking booking = room.getBooking(reference);
				if (booking != null) {
					return new RoomBookingData(room, booking);
				}
			}
		}
		throw new HotelException();
	}

	public static Set<String> bulkBooking(int number, LocalDate arrival, LocalDate departure) {
		if (number < 1) {
			throw new HotelException();
		}

		Set<Room> rooms = getAvailableRooms(number, arrival, departure);
		if (rooms.size() < number) {
			throw new HotelException();
		}

		Set<String> references = new HashSet<>();
		for (Room room : rooms) {
			references.add(room.reserve(room.getType(), arrival, departure).getReference());
		}

		return references;
	}

	static Set<Room> getAvailableRooms(int number, LocalDate arrival, LocalDate departure) {
		Set<Room> rooms = new HashSet<>();
		for (Hotel hotel : hotels) {
			for (Room room : hotel.rooms) {
				if (room.isFree(room.getType(), arrival, departure)) {
					rooms.add(room);
					if (rooms.size() == number) {
						return rooms;
					}
				}
			}
		}
		return rooms;
	}

    public void removeRooms() {
        rooms.clear();
    }
}
