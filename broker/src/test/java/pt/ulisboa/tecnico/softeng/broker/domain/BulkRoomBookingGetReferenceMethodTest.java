package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Delegate;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class BulkRoomBookingGetReferenceMethodTest {
	private static final String SINGLE = "SINGLE";
	private static final String DOUBLE = "DOUBLE";
	private static final String REF_1 = "ref1";
	private static final String REF_2 = "ref2";
	private static final int NUMBER = 20;
	private static final LocalDate arrival = new LocalDate(2016, 12, 19);
	private static final LocalDate departure = new LocalDate(2016, 12, 21);
	private BulkRoomBooking bulk;

	@Before
	public void setUp() {
		this.bulk = new BulkRoomBooking(NUMBER, arrival, departure);
		this.bulk.getReferences().addAll(Arrays.asList(REF_1, REF_2));
	}

	@Test
	public void successSINGLE(@Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{
				HotelInterface.getRoomBookingData(this.anyString);
				this.result = new Delegate() {
					RoomBookingData delegate() {
						return new RoomBookingData(SINGLE);
					}
				};
			}
		};

		this.bulk.getReference(SINGLE);

		assertEquals(1, this.bulk.getReferences().size());
	}

	@Test
	public void successDOUBLE(@Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{
				HotelInterface.getRoomBookingData(this.anyString);
				this.result = new Delegate() {
					RoomBookingData delegate() {
						return new RoomBookingData(DOUBLE);
					}
				};
			}
		};

		this.bulk.getReference(DOUBLE);

		assertEquals(1, this.bulk.getReferences().size());
	}

	@Test
	public void hotelException(@Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{
				HotelInterface.getRoomBookingData(this.anyString);
				this.result = new HotelException();
				this.times = 2;
			}
		};

		assertNull(this.bulk.getReference(DOUBLE));

		assertEquals(2, this.bulk.getReferences().size());
	}

	@Test
	public void remoteException(@Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{
				HotelInterface.getRoomBookingData(this.anyString);
				this.result = new RemoteAccessException();
				this.times = 2;
			}
		};

		assertNull(this.bulk.getReference(DOUBLE));

		assertEquals(2, this.bulk.getReferences().size());
	}

	@Test
	public void maxRemoteException(@Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{
				HotelInterface.getRoomBookingData(this.anyString);
				this.result = new RemoteAccessException();
				this.times = BulkRoomBooking.MAX_REMOTE_ERRORS;
			}
		};

		for (int i = 0; i < BulkRoomBooking.MAX_REMOTE_ERRORS / 2; i++) {
			assertNull(this.bulk.getReference(DOUBLE));
		}
		assertNull(this.bulk.getReference(DOUBLE));

		assertEquals(2, this.bulk.getReferences().size());
	}

	@Test
	public void maxMinusOneRemoteException(@Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{
				HotelInterface.getRoomBookingData(this.anyString);
				this.result = new Delegate() {
					int i = 0;

					RoomBookingData delegate() {
						this.i++;
						if (this.i < BulkRoomBooking.MAX_REMOTE_ERRORS - 1) {
							throw new RemoteAccessException();
						} else {
							return new RoomBookingData(DOUBLE);
						}
					}
				};
			}
		};

		for (int i = 0; i < BulkRoomBooking.MAX_REMOTE_ERRORS / 2 - 1; i++) {
			assertNull(this.bulk.getReference(DOUBLE));
		}
		this.bulk.getReference(DOUBLE);

		assertEquals(1, this.bulk.getReferences().size());
	}

	@Test
	public void remoteExceptionValueIsResetBySuccess(@Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{
				HotelInterface.getRoomBookingData(this.anyString);
				this.result = new Delegate() {
					int i = 0;

					RoomBookingData delegate() {
						this.i++;
						if (this.i < BulkRoomBooking.MAX_REMOTE_ERRORS - 1) {
							throw new RemoteAccessException();
						} else if (this.i == BulkRoomBooking.MAX_REMOTE_ERRORS - 1) {
							return new RoomBookingData(DOUBLE);
						} else {
							throw new RemoteAccessException();
						}
					}
				};
			}
		};

		for (int i = 0; i < BulkRoomBooking.MAX_REMOTE_ERRORS / 2 - 1; i++) {
			assertNull(this.bulk.getReference(DOUBLE));
		}

		this.bulk.getReference(DOUBLE);

		for (int i = 0; i < BulkRoomBooking.MAX_REMOTE_ERRORS; i++) {
			assertNull(this.bulk.getReference(DOUBLE));
		}

		assertEquals(1, this.bulk.getReferences().size());
	}

	@Test
	public void remoteExceptionValueIsResetByHotelException(@Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{
				HotelInterface.getRoomBookingData(this.anyString);
				this.result = new Delegate() {
					int i = 0;

					RoomBookingData delegate() {
						this.i++;
						if (this.i < BulkRoomBooking.MAX_REMOTE_ERRORS - 1) {
							throw new RemoteAccessException();
						} else if (this.i == BulkRoomBooking.MAX_REMOTE_ERRORS - 1) {
							throw new HotelException();
						} else {
							throw new RemoteAccessException();
						}
					}
				};
			}
		};

		for (int i = 0; i < BulkRoomBooking.MAX_REMOTE_ERRORS / 2 - 1; i++) {
			assertNull(this.bulk.getReference(DOUBLE));
		}

		this.bulk.getReference(DOUBLE);

		for (int i = 0; i < BulkRoomBooking.MAX_REMOTE_ERRORS; i++) {
			assertNull(this.bulk.getReference(DOUBLE));
		}

		assertEquals(2, this.bulk.getReferences().size());
	}

}