package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class BulkRoomBookingProcessBookingMethodTest {
	private static final int NUMBER = 20;
	private static final LocalDate arrival = new LocalDate(2016, 12, 19);
	private static final LocalDate departure = new LocalDate(2016, 12, 21);
	private static final String NIF_BUYER = "123456789";
	private static final String IBAN_BUYER = "IBAN";
	private BulkRoomBooking bulk;

	@Before
	public void setUp() {
		this.bulk = new BulkRoomBooking(NUMBER, arrival, departure, NIF_BUYER, IBAN_BUYER);
	}

	@Test
	public void success(@Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure, NIF_BUYER, IBAN_BUYER);
				this.result = new HashSet<>(Arrays.asList("ref1", "ref2"));
			}
		};

		this.bulk.processBooking();

		assertEquals(2, this.bulk.getReferences().size());
	}

	@Test
	public void successTwice(@Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure, NIF_BUYER, IBAN_BUYER);
				this.result = new HashSet<>(Arrays.asList("ref1", "ref2"));
				this.result = new HashSet<>(Arrays.asList("ref3", "ref4"));
			}
		};

		this.bulk.processBooking();
		this.bulk.processBooking();

		assertEquals(4, this.bulk.getReferences().size());
	}

	@Test
	public void cancelled(@Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure, NIF_BUYER, IBAN_BUYER);
				this.result = new HashSet<>(Arrays.asList("ref1", "ref2"));
				this.result = new HotelException();
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
		new Expectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure, NIF_BUYER, IBAN_BUYER);
				this.result = new HotelException();
				this.result = new HashSet<>(Arrays.asList("ref1", "ref2"));
			}
		};

		this.bulk.processBooking();
		this.bulk.processBooking();

		assertEquals(2, this.bulk.getReferences().size());
	}

	@Test
	public void maxHotelException(@Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure, NIF_BUYER, IBAN_BUYER);
				this.result = new HotelException();
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
		new Expectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure, NIF_BUYER, IBAN_BUYER);
				this.result = new Delegate() {
					int i = 0;

					Set<String> delegate() {
						this.i++;
						if (this.i < BulkRoomBooking.MAX_HOTEL_EXCEPTIONS - 1) {
							throw new HotelException();
						} else {
							return new HashSet<>(Arrays.asList("ref1", "ref2"));
						}
					}
				};
			}
		};

		this.bulk.processBooking();
		this.bulk.processBooking();
		this.bulk.processBooking();

		assertEquals(2, this.bulk.getReferences().size());
	}

	@Test
	public void hotelExceptionValueIsResetBySuccess(@Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure, NIF_BUYER, IBAN_BUYER);
				this.result = new Delegate() {
					int i = 0;

					Set<String> delegate() {
						this.i++;
						if (this.i < BulkRoomBooking.MAX_HOTEL_EXCEPTIONS - 1) {
							throw new HotelException();
						} else if (this.i == BulkRoomBooking.MAX_HOTEL_EXCEPTIONS - 1) {
							return new HashSet<>(Arrays.asList("ref1", "ref2"));
						} else if (this.i < 2 * BulkRoomBooking.MAX_HOTEL_EXCEPTIONS - 1) {
							throw new HotelException();
						} else {
							return new HashSet<>(Arrays.asList("ref3", "ref4"));
						}
					}
				};

			}
		};

		this.bulk.processBooking();
		this.bulk.processBooking();
		this.bulk.processBooking();
		this.bulk.processBooking();
		this.bulk.processBooking();
		this.bulk.processBooking();

		assertEquals(4, this.bulk.getReferences().size());
	}

	@Test
	public void hotelExceptionValueIsResetByRemoteException(@Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure, NIF_BUYER, IBAN_BUYER);
				this.result = new Delegate() {
					int i = 0;

					Set<String> delegate() {
						this.i++;
						if (this.i < BulkRoomBooking.MAX_HOTEL_EXCEPTIONS - 1) {
							throw new HotelException();
						} else if (this.i == BulkRoomBooking.MAX_HOTEL_EXCEPTIONS - 1) {
							throw new RemoteAccessException();
						} else if (this.i < 2 * BulkRoomBooking.MAX_HOTEL_EXCEPTIONS - 1) {
							throw new HotelException();
						} else {
							return new HashSet<>(Arrays.asList("ref1", "ref2"));
						}
					}
				};
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
		new Expectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure, NIF_BUYER, IBAN_BUYER);
				this.result = new RemoteAccessException();
				this.result = new HashSet<>(Arrays.asList("ref1", "ref2"));
			}
		};

		this.bulk.processBooking();
		this.bulk.processBooking();

		assertEquals(2, this.bulk.getReferences().size());
	}

	@Test
	public void maxRemoteException(@Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure, NIF_BUYER, IBAN_BUYER);
				this.result = new RemoteAccessException();
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
		new Expectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure, NIF_BUYER, IBAN_BUYER);
				this.result = new Delegate() {
					int i = 0;

					Set<String> delegate() {
						this.i++;
						if (this.i < BulkRoomBooking.MAX_REMOTE_ERRORS - 1) {
							throw new RemoteAccessException();
						} else {
							return new HashSet<>(Arrays.asList("ref1", "ref2"));
						}
					}
				};
				this.result = new RemoteAccessException();
				this.times = BulkRoomBooking.MAX_REMOTE_ERRORS - 1;

				HotelInterface.bulkBooking(NUMBER, arrival, departure, NIF_BUYER, IBAN_BUYER);
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
		new Expectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure, NIF_BUYER, IBAN_BUYER);
				this.result = new Delegate() {
					int i = 0;

					Set<String> delegate() {
						this.i++;
						if (this.i < BulkRoomBooking.MAX_REMOTE_ERRORS - 1) {
							throw new RemoteAccessException();
						} else if (this.i == BulkRoomBooking.MAX_REMOTE_ERRORS - 1) {
							return new HashSet<>(Arrays.asList("ref1", "ref2"));
						} else if (this.i < 2 * BulkRoomBooking.MAX_REMOTE_ERRORS - 1) {
							throw new RemoteAccessException();
						} else {
							return new HashSet<>(Arrays.asList("ref3", "ref4"));
						}
					}
				};
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

		assertEquals(4, this.bulk.getReferences().size());
	}

	@Test
	public void remoteExceptionValueIsResetByHotelException(@Mocked final HotelInterface roomInterface) {
		new Expectations() {
			{
				HotelInterface.bulkBooking(NUMBER, arrival, departure, NIF_BUYER, IBAN_BUYER);
				this.result = new Delegate() {
					int i = 0;

					Set<String> delegate() {
						this.i++;
						if (this.i < BulkRoomBooking.MAX_REMOTE_ERRORS - 1) {
							throw new RemoteAccessException();
						} else if (this.i == BulkRoomBooking.MAX_REMOTE_ERRORS - 1) {
							throw new HotelException();
						} else if (this.i < 2 * BulkRoomBooking.MAX_REMOTE_ERRORS - 1) {
							throw new RemoteAccessException();
						} else {
							return new HashSet<>(Arrays.asList("ref1", "ref2"));
						}
					}
				};
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