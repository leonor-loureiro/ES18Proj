package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.domain.Processor;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Hotel {
	public static Set<Hotel> hotels = new HashSet<>();

	static final int CODE_SIZE = 7;

	private final String code;
	private final String name;
	private final Set<Room> rooms = new HashSet<>();
	
	private final String NIF;
	private final String IBAN;

	
	private final Processor processor = new Processor();

	private final int singlePrice;
	private final int doublePrice;

	public Hotel(String code, String name, String NIF, String IBAN, int singlePrice, int doublePrice) {
		checkArguments(code, name, NIF, IBAN, singlePrice, doublePrice);


		this.code = code;
		this.name = name;
		this.NIF = NIF;
		this.IBAN = IBAN;
		this.singlePrice = singlePrice;
		this.doublePrice = doublePrice;
		
		Hotel.hotels.add(this);
	}

	private void checkArguments(String code, String name, String NIF, String IBAN, int singlePrice, int doublePrice) {
		if (code == null || name == null || code.trim().length() == 0 || name.trim().length() == 0) {
			throw new HotelException();
		}

		if (code.length() != Hotel.CODE_SIZE) {
			throw new HotelException();	
		}

		if (NIF == null || NIF.length() != 9) {
			throw new HotelException();
		}

		if (IBAN == null || IBAN.length() < 5) {
			throw new HotelException();
		}
		
		if (singlePrice <= 0 || doublePrice <= 0) {
			throw new HotelException();
		}
		
		for (Hotel hotel : hotels) {
			if (hotel.getCode().equals(code)) {
				throw new HotelException();
			}
		}
	}

	
	public Processor getProcessor() {
		return this.processor;
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
		return this.NIF;
	}
	
	public String getIBAN() {
		return this.IBAN;
	}
	
	public int getSinglePrice() {
		return this.singlePrice;
	}

	public int getDoublePrice() {
		return this.doublePrice;
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

	public static String reserveRoom(Room.Type type, LocalDate arrival, LocalDate departure, String clientNIF, String clientIBAN) {
		for (Hotel hotel : Hotel.hotels) {
			Room room = hotel.hasVacancy(type, arrival, departure);
			if (room != null) {
				return room.reserve(type, arrival, departure, clientNIF, clientIBAN).getReference();
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

	public static Set<String> bulkBooking(int number, LocalDate arrival, LocalDate departure, String clientNIF, String clientIBAN) {
		if (number < 1) {
			throw new HotelException();
		}

		Set<Room> rooms = getAvailableRooms(number, arrival, departure);
		if (rooms.size() < number) {
			throw new HotelException();
		}

		Set<String> references = new HashSet<>();
		for (Room room : rooms) {
			references.add(room.reserve(room.getType(), arrival, departure, clientNIF, clientIBAN).getReference());
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