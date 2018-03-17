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
	private double priceSingle;
	private double priceDouble;

	private final Set<Room> rooms = new HashSet<>();
	
	private final Processor processor = new Processor();

	public Hotel(String code, String name, String nif, String iban, double priceSingle, double priceDouble) {
		checkArguments(code, name, nif, iban, priceSingle, priceDouble);

		this.code = code;
		this.name = name;
		this.nif = nif;
		this.iban = iban;
		this.priceSingle = priceSingle;
		this.priceDouble = priceDouble;

		Hotel.hotels.add(this);
	}

	private void checkArguments(String code, String name, String nif, String iban, double priceSingle,
			double priceDouble) {
		if (code == null || name == null || isEmpty(code) || isEmpty((name)) || nif == null || isEmpty(nif)
				|| iban == null || isEmpty(iban) || priceSingle < 0 || priceDouble < 0) {

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

	public Processor getProcessor() {
		return this.processor;
	}
	
	public double getPriceSingle() {
		return this.priceSingle;
	}

	public double getPriceDouble() {
		return this.priceDouble;
	}

	public double getPrice(Room.Type type) {
		if (type == null) {
			throw new HotelException();
		} else {
			return type.equals(Room.Type.SINGLE) ? this.priceSingle : this.priceDouble;
		}
	}

	public void setPrice(Room.Type type, double price) {
		if (price < 0 || type == null) {
			throw new HotelException();
		} else if (type.equals(Room.Type.SINGLE)) {
			this.priceSingle = price;
		} else {
			this.priceDouble = price;
		}
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

	public static String reserveRoom(Room.Type type, LocalDate arrival, LocalDate departure, String buyerNIF) {
		for (Hotel hotel : Hotel.hotels) {
			Room room = hotel.hasVacancy(type, arrival, departure);
			if (room != null) {
				Booking booking = room.reserve(type, arrival, departure, buyerNIF);
				hotel.getProcessor().submitBooking(booking);
				return booking.getReference();
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

	public static Set<String> bulkBooking(int number, LocalDate arrival, LocalDate departure, String buyerNIF) {
		if (number < 1) {
			throw new HotelException();
		}

		Set<Room> rooms = getAvailableRooms(number, arrival, departure);
		if (rooms.size() < number) {
			throw new HotelException();
		}

		Set<String> references = new HashSet<>();
		for (Room room : rooms) {
			Booking booking = room.reserve(room.getType(), arrival, departure, buyerNIF);
			room.getHotel().getProcessor().submitBooking(booking);
			references.add(booking.getReference());
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
		this.rooms.clear();
	}
}
