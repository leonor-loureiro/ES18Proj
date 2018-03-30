package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class BrokerPersistenceTest extends BaseTest {
	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		Broker broker = new Broker(BROKER_CODE, BROKER_NAME, BROKER_NIF_AS_SELLER, NIF_AS_BUYER, BROKER_IBAN);
		Client client = new Client(broker, CLIENT_IBAN, CLIENT_NIF, DRIVING_LICENSE, AGE);
		new Adventure(broker, this.begin, this.end, client, MARGIN);

		BulkRoomBooking bulk = new BulkRoomBooking(broker, NUMBER_OF_BULK, this.begin, this.end, NIF_AS_BUYER, CLIENT_IBAN);

		new Reference(bulk, REF_ONE);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		assertEquals(1, FenixFramework.getDomainRoot().getBrokerSet().size());

		List<Broker> brokers = new ArrayList<>(FenixFramework.getDomainRoot().getBrokerSet());
		Broker broker = brokers.get(0);

		assertEquals(BROKER_CODE, broker.getCode());
		assertEquals(BROKER_NAME, broker.getName());
		assertEquals(1, broker.getAdventureSet().size());
		assertEquals(1, broker.getRoomBulkBookingSet().size());

		List<Adventure> adventures = new ArrayList<>(broker.getAdventureSet());
		Adventure adventure = adventures.get(0);

		assertNotNull(adventure.getID());
		assertEquals(broker, adventure.getBroker());
		assertEquals(this.begin, adventure.getBegin());
		assertEquals(this.end, adventure.getEnd());
		assertEquals(AGE, adventure.getAge());
		assertEquals(CLIENT_IBAN, adventure.getIBAN());

		assertEquals(Adventure.State.RESERVE_ACTIVITY, adventure.getState().getValue());
		assertEquals(0, adventure.getState().getNumOfRemoteErrors());

		List<BulkRoomBooking> bulks = new ArrayList<>(broker.getRoomBulkBookingSet());
		BulkRoomBooking bulk = bulks.get(0);

		assertNotNull(bulk);
		assertEquals(this.begin, bulk.getArrival());
		assertEquals(this.end, bulk.getDeparture());
		assertEquals(NUMBER_OF_BULK, bulk.getNumber());
		assertFalse(bulk.getCancelled());
		assertEquals(0, bulk.getNumberOfHotelExceptions());
		assertEquals(0, bulk.getNumberOfRemoteErrors());
		assertEquals(1, bulk.getReferenceSet().size());

		List<Reference> references = new ArrayList<>(bulk.getReferenceSet());
		Reference reference = references.get(0);

		assertEquals(REF_ONE, reference.getValue());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Broker broker : FenixFramework.getDomainRoot().getBrokerSet()) {
			broker.delete();
		}
	}

}
