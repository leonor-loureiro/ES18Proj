package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Hotel extends Hotel_Base {
	static final int CODE_SIZE = 7;

	private final String nif;
	private final String iban;
	private double priceSingle;
	private double priceDouble;

	private final Processor processor = new Processor();

	@Override
	public int getCounter() {
		int counter = super.getCounter() + 1;
		setCounter(counter);
		return counter;
	}

	public Hotel(String code, String name, String nif, String iban, double priceSingle, double priceDouble) {
		checkArguments(code, name, nif, iban, priceSingle, priceDouble);

		setCode(code);
		setName(name);

		this.nif = nif;
		this.iban = iban;
		this.priceSingle = priceSingle;
		this.priceDouble = priceDouble;

		FenixFramework.getDomainRoot().addHotel(this);
	}

	public void delete() {
		setRoot(null);

		for (Room room : getRoomSet()) {
			room.delete();
		}

		deleteDomainObject();
	}

	private void checkArguments(String code, String name, String nif, String iban, double priceSingle,
			double priceDouble) {
		if (code == null || name == null || isEmpty(code) || isEmpty(name) || nif == null || isEmpty(nif)
				|| iban == null || isEmpty(iban) || priceSingle < 0 || priceDouble < 0) {

			throw new HotelException();
		}

		if (code.length() != Hotel.CODE_SIZE) {
			throw new HotelException();
		}

		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			if (hotel.getCode().equals(code)) {
				throw new HotelException();
			}
		}

		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			if (hotel.getNIF().equals(nif)) {
				throw new HotelException();
			}
		}
	}

	public Room hasVacancy(Room.Type type, LocalDate arrival, LocalDate departure) {
		if (type == null || arrival == null || departure == null) {
			throw new HotelException();
		}

		for (Room room : getRoomSet()) {
			if (room.isFree(type, arrival, departure)) {
				return room;
			}
		}
		return null;
	}

	public Set<Room> getAvailableRooms(LocalDate arrival, LocalDate departure) {
		Set<Room> availableRooms = new HashSet<>();
		for (Room room : getRoomSet()) {
			if (room.isFree(room.getType(), arrival, departure)) {
				availableRooms.add(room);
			}
		}
		return availableRooms;
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

	@Override
	public void addRoom(Room room) {
		if (hasRoom(room.getNumber())) {
			throw new HotelException();
		}

		super.addRoom(room);
	}

	public boolean hasRoom(String number) {
		for (Room room : getRoomSet()) {
			if (room.getNumber().equals(number)) {
				return true;
			}
		}
		return false;
	}

	private Booking getBooking(String reference) {
		for (Room room : getRoomSet()) {
			Booking booking = room.getBooking(reference);
			if (booking != null) {
				return booking;
			}
		}
		return null;
	}

	public static String reserveRoom(Room.Type type, LocalDate arrival, LocalDate departure, String buyerNIF,
			String buyerIban) {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			Room room = hotel.hasVacancy(type, arrival, departure);
			if (room != null) {
				Booking booking = room.reserve(type, arrival, departure, buyerNIF, buyerIban);
				hotel.getProcessor().submitBooking(booking);
				return booking.getReference();
			}
		}
		throw new HotelException();
	}

	public static String cancelBooking(String reference) {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			Booking booking = hotel.getBooking(reference);
			if (booking != null) {
				return booking.cancel();
			}
		}
		throw new HotelException();
	}

	public static RoomBookingData getRoomBookingData(String reference) {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			for (Room room : hotel.getRoomSet()) {
				Booking booking = room.getBooking(reference);
				if (booking != null) {
					return new RoomBookingData(room, booking);
				}
			}
		}
		throw new HotelException();
	}

	public static Set<String> bulkBooking(int number, LocalDate arrival, LocalDate departure, String buyerNIF,
			String buyerIban) {
		if (number < 1) {
			throw new HotelException();
		}

		List<Room> rooms = getAvailableRooms(number, arrival, departure);
		if (rooms.size() < number) {
			throw new HotelException();
		}

		Set<String> references = new HashSet<>();

		for (int i = 0; i < number; i++) {
			Booking booking = rooms.get(i).reserve(rooms.get(i).getType(), arrival, departure, buyerNIF, buyerIban);
			rooms.get(i).getHotel().getProcessor().submitBooking(booking);
			references.add(booking.getReference());
		}

		return references;
	}

	public static List<Room> getAvailableRooms(int number, LocalDate arrival, LocalDate departure) {
		List<Room> availableRooms = new ArrayList<>();
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			availableRooms.addAll(hotel.getAvailableRooms(arrival, departure));
			if (availableRooms.size() >= number) {
				return availableRooms;
			}
		}
		return availableRooms;
	}

	public static Hotel getHotelByCode(String code) {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			if (hotel.getCode().equals(code)) {
				return hotel;
			}
		}
		return null;
	}

}
