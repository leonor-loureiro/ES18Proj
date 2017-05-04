package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Booking extends Booking_Base {
	public Booking(Room room, LocalDate arrival, LocalDate departure) {
		checkArguments(room, arrival, departure);

		if (!room.isFree(room.getType(), arrival, departure)) {
			throw new HotelException();
		}

		setReference(room.getHotel().getCode() + Integer.toString(room.getHotel().getBookingCounter()));
		setArrival(arrival);
		setDeparture(departure);

		setRoom(room);
	}

	public void delete() {
		setRoom(null);

		deleteDomainObject();
	}

	private void checkArguments(Room room, LocalDate arrival, LocalDate departure) {
		if (room == null || arrival == null || departure == null) {
			throw new HotelException();
		}

		if (departure.isBefore(arrival)) {
			throw new HotelException();
		}
	}

	boolean conflict(LocalDate arrival, LocalDate departure) {
		if (isCancelled()) {
			return false;
		}

		if (arrival.equals(departure)) {
			return true;
		}

		if (departure.isBefore(arrival)) {
			throw new HotelException();
		}

		if ((arrival.equals(getArrival()) || arrival.isAfter(getArrival())) && arrival.isBefore(getDeparture())) {
			return true;
		}

		if ((departure.equals(getDeparture()) || departure.isBefore(getDeparture()))
				&& departure.isAfter(getArrival())) {
			return true;
		}

		if ((arrival.isBefore(getArrival()) && departure.isAfter(getDeparture()))) {
			return true;
		}

		return false;
	}

	public String cancel() {
		setCancellation(getReference() + "CANCEL");
		setCancellationDate(new LocalDate());
		return getCancellation();
	}

	public boolean isCancelled() {
		return getCancellation() != null;
	}

}
