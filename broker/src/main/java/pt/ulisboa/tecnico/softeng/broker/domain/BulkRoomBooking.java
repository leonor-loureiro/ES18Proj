package pt.ulisboa.tecnico.softeng.broker.domain;

import java.util.Set;
import java.util.stream.Collectors;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class BulkRoomBooking extends BulkRoomBooking_Base {
	public static final int MAX_HOTEL_EXCEPTIONS = 3;
	public static final int MAX_REMOTE_ERRORS = 10;

	public BulkRoomBooking(Broker broker, int number, LocalDate arrival, LocalDate departure, String buyerNif, String buyerIban) {

		setNumber(number);
		setArrival(arrival);
		setDeparture(departure);
		setBuyerNif(buyerNif);
		setBuyerIban(buyerIban);
		setBroker(broker);
	}

	public void delete() {
		setBroker(null);

		for (Reference reference : getReferenceSet()) {
			reference.delete();
		}

		deleteDomainObject();
	}

	public Set<String> getReferences() {
		return getReferenceSet().stream().map(r -> r.getValue()).collect(Collectors.toSet());
	}

	public void processBooking() {
		if (getCancelled()) {
			return;
		}

		try {
			for (String reference : HotelInterface.bulkBooking(getNumber(), getArrival(), getDeparture(), getBuyerNif(),
                    getBuyerIban())) {
				addReference(new Reference(this, reference));
			}
			setNumberOfHotelExceptions(0);
			setNumberOfRemoteErrors(0);
			return;
		} catch (HotelException he) {
			setNumberOfHotelExceptions(getNumberOfHotelExceptions() + 1);
			if (getNumberOfHotelExceptions() == MAX_HOTEL_EXCEPTIONS) {
				setCancelled(true);
			}
			setNumberOfRemoteErrors(0);
			return;
		} catch (RemoteAccessException rae) {
			setNumberOfRemoteErrors(getNumberOfRemoteErrors() + 1);
			if (getNumberOfRemoteErrors() == MAX_REMOTE_ERRORS) {
				setCancelled(true);
			}
			setNumberOfHotelExceptions(0);
			return;
		}
	}

	public String getReference(String type) {
		if (getCancelled()) {
			return null;
		}

		for (Reference reference : getReferenceSet()) {
			RoomBookingData data = null;
			try {
				data = HotelInterface.getRoomBookingData(reference.getValue());
				setNumberOfRemoteErrors(0);
			} catch (HotelException he) {
				setNumberOfRemoteErrors(0);
			} catch (RemoteAccessException rae) {
				setNumberOfRemoteErrors(getNumberOfRemoteErrors() + 1);
				if (getNumberOfRemoteErrors() == MAX_REMOTE_ERRORS) {
					setCancelled(true);
				}
			}

			if (data != null && data.getRoomType().equals(type)) {
				removeReference(reference);
				return reference.getValue();
			}
		}
		return null;
	}

}
