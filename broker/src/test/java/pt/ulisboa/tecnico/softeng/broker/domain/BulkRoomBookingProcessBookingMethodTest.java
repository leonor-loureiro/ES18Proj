package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.broker.services.remote.HotelInterface;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.HotelException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.RemoteAccessException;

@RunWith(JMockit.class)
public class BulkRoomBookingProcessBookingMethodTest extends RollbackTestAbstractClass {
	private static final int NUMBER = 20;
	private static final LocalDate arrival = new LocalDate(2016, 12, 19);
	private static final LocalDate departure = new LocalDate(2016, 12, 21);
	private BulkRoomBooking bulk;

	@Override
	public void populate4Test() {
		this.bulk = new BulkRoomBooking(new Broker("BK1111", "Traveling"), NUMBER, arrival, departure);
	}

	@Test
	public void success(@Mocked final HotelInterface roomInterface) {
		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new HashSet<>(Arrays.asList("ref1", "ref2"));
			}
		};

		this.bulk.processBooking();

		assertEquals(2, this.bulk.getReferences().size());
	}

	@Test
	public void successFirstFailSecond(@Mocked final HotelInterface roomInterface) {
		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new HashSet<>(Arrays.asList("ref1", "ref2"));
			}
		};

		this.bulk.processBooking();
		this.bulk.processBooking();

		assertEquals(2, this.bulk.getReferences().size());
	}

	@Test
	public void cancelled(@Mocked final HotelInterface roomInterface) {
		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new HashSet<>(Arrays.asList("ref1", "ref2"));
			}
		};

		this.bulk.processBooking();
		this.bulk.processBooking();
		this.bulk.processBooking();
		this.bulk.processBooking();
		this.bulk.processBooking();

		assertEquals(2, this.bulk.getReferences().size());
	}

	@Test
	public void oneHotelException(@Mocked final HotelInterface roomInterface) {
		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new HotelException();
				this.times = 1;

				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new HashSet<>(Arrays.asList("ref1", "ref2"));
			}
		};

		this.bulk.processBooking();
		this.bulk.processBooking();

		assertEquals(2, this.bulk.getReferences().size());
	}

	@Test
	public void maxHotelException(@Mocked final HotelInterface roomInterface) {
		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new HotelException();
				this.times = BulkRoomBooking.MAX_HOTEL_EXCEPTIONS;
			}
		};

		this.bulk.processBooking();
		this.bulk.processBooking();
		this.bulk.processBooking();
		this.bulk.processBooking();

		assertEquals(0, this.bulk.getReferences().size());
	}

	@Test
	public void maxMinusOneHotelException(@Mocked final HotelInterface roomInterface) {
		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new HotelException();
				this.times = BulkRoomBooking.MAX_HOTEL_EXCEPTIONS - 1;

				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new HashSet<>(Arrays.asList("ref1", "ref2"));
			}
		};

		this.bulk.processBooking();
		this.bulk.processBooking();
		this.bulk.processBooking();

		assertEquals(2, this.bulk.getReferences().size());
	}

	@Test
	public void hotelExceptionValueIsResetBySuccess(@Mocked final HotelInterface roomInterface) {
		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new HotelException();
				this.times = BulkRoomBooking.MAX_HOTEL_EXCEPTIONS - 1;

				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new HashSet<>(Arrays.asList("ref1", "ref2"));
			}
		};

		this.bulk.processBooking();
		this.bulk.processBooking();
		this.bulk.processBooking();
		this.bulk.processBooking();
		this.bulk.processBooking();
		this.bulk.processBooking();

		assertEquals(2, this.bulk.getReferences().size());
	}

	@Test
	public void hotelExceptionValueIsResetByRemoteException(@Mocked final HotelInterface roomInterface) {
		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new HotelException();
				this.times = BulkRoomBooking.MAX_HOTEL_EXCEPTIONS - 1;

				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new RemoteAccessException();

				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new HotelException();
				this.times = BulkRoomBooking.MAX_HOTEL_EXCEPTIONS - 1;

				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new HashSet<>(Arrays.asList("ref1", "ref2"));

			}
		};

		this.bulk.processBooking();
		this.bulk.processBooking();
		this.bulk.processBooking();
		this.bulk.processBooking();
		this.bulk.processBooking();
		this.bulk.processBooking();

		assertEquals(2, this.bulk.getReferences().size());
	}

	@Test
	public void oneRemoteException(@Mocked final HotelInterface roomInterface) {
		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new RemoteAccessException();
				this.times = 1;

				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new HashSet<>(Arrays.asList("ref1", "ref2"));
			}
		};

		this.bulk.processBooking();
		this.bulk.processBooking();

		assertEquals(2, this.bulk.getReferences().size());
	}

	@Test
	public void maxRemoteException(@Mocked final HotelInterface roomInterface) {
		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new RemoteAccessException();
				this.times = BulkRoomBooking.MAX_REMOTE_ERRORS;
			}
		};

		for (int i = 0; i < BulkRoomBooking.MAX_REMOTE_ERRORS; i++) {
			this.bulk.processBooking();
		}
		this.bulk.processBooking();

		assertEquals(0, this.bulk.getReferences().size());
	}

	@Test
	public void maxMinusOneRemoteException(@Mocked final HotelInterface roomInterface) {
		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new RemoteAccessException();
				this.times = BulkRoomBooking.MAX_REMOTE_ERRORS - 1;

				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new HashSet<>(Arrays.asList("ref1", "ref2"));
			}
		};

		for (int i = 0; i < BulkRoomBooking.MAX_REMOTE_ERRORS - 1; i++) {
			this.bulk.processBooking();
		}
		this.bulk.processBooking();

		assertEquals(2, this.bulk.getReferences().size());
	}

	@Test
	public void remoteExceptionValueIsResetBySuccess(@Mocked final HotelInterface roomInterface) {
		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new RemoteAccessException();
				this.times = BulkRoomBooking.MAX_REMOTE_ERRORS - 1;

				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new HashSet<>(Arrays.asList("ref1", "ref2"));
			}
		};

		for (int i = 0; i < BulkRoomBooking.MAX_REMOTE_ERRORS - 1; i++) {
			this.bulk.processBooking();
		}
		this.bulk.processBooking();
		for (int i = 0; i < BulkRoomBooking.MAX_REMOTE_ERRORS - 1; i++) {
			this.bulk.processBooking();
		}
		this.bulk.processBooking();

		assertEquals(2, this.bulk.getReferences().size());
	}

	@Test
	public void remoteExceptionValueIsResetByHotelException(@Mocked final HotelInterface roomInterface) {
		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new RemoteAccessException();
				this.times = BulkRoomBooking.MAX_REMOTE_ERRORS - 1;

				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new HotelException();

				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new RemoteAccessException();
				this.times = BulkRoomBooking.MAX_REMOTE_ERRORS - 1;

				HotelInterface.bulkBooking(NUMBER, arrival, departure);
				this.result = new HashSet<>(Arrays.asList("ref1", "ref2"));

			}
		};

		for (int i = 0; i < BulkRoomBooking.MAX_REMOTE_ERRORS - 1; i++) {
			this.bulk.processBooking();
		}
		this.bulk.processBooking();
		for (int i = 0; i < BulkRoomBooking.MAX_REMOTE_ERRORS - 1; i++) {
			this.bulk.processBooking();
		}
		this.bulk.processBooking();

		assertEquals(2, this.bulk.getReferences().size());
	}

}
