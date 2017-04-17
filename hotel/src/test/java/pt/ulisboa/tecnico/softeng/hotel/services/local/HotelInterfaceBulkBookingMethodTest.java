package pt.ulisboa.tecnico.softeng.hotel.services.local;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.RollbackTestAbstractClass;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.services.local.HotelInterface;

public class HotelInterfaceBulkBookingMethodTest extends RollbackTestAbstractClass {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private Hotel hotel;

	@Override
	public void populate4Test() {
		this.hotel = new Hotel("XPTO123", "Paris");
		new Room(this.hotel, "01", Type.DOUBLE);
		new Room(this.hotel, "02", Type.SINGLE);
		new Room(this.hotel, "03", Type.DOUBLE);
		new Room(this.hotel, "04", Type.SINGLE);

		this.hotel = new Hotel("XPTO124", "Paris");
		new Room(this.hotel, "01", Type.DOUBLE);
		new Room(this.hotel, "02", Type.SINGLE);
		new Room(this.hotel, "03", Type.DOUBLE);
		new Room(this.hotel, "04", Type.SINGLE);
	}

	@Test
	public void success() {
		Set<String> references = HotelInterface.bulkBooking(2, this.arrival, this.departure);

		assertEquals(2, references.size());
	}

	@Test(expected = HotelException.class)
	public void zeroNumber() {
		HotelInterface.bulkBooking(0, this.arrival, this.departure);
	}

	@Test(expected = HotelException.class)
	public void noRooms() {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			hotel.delete();
		}
		this.hotel = new Hotel("XPTO124", "Paris");

		HotelInterface.bulkBooking(3, this.arrival, this.departure);
	}

	@Test
	public void OneNumber() {
		Set<String> references = HotelInterface.bulkBooking(1, this.arrival, this.departure);

		assertEquals(1, references.size());
	}

	@Test(expected = HotelException.class)
	public void nullArrival() {
		HotelInterface.bulkBooking(2, null, this.departure);
	}

	@Test(expected = HotelException.class)
	public void nullDeparture() {
		HotelInterface.bulkBooking(2, this.arrival, null);
	}

	@Test
	public void reserveAll() {
		Set<String> references = HotelInterface.bulkBooking(8, this.arrival, this.departure);

		assertEquals(8, references.size());
	}

	@Test
	public void reserveAllPlusOne() {
		try {
			HotelInterface.bulkBooking(9, this.arrival, this.departure);
			fail();
		} catch (HotelException he) {
			assertEquals(8, HotelInterface.getAvailableRooms(8, this.arrival, this.departure).size());
		}
	}

}
