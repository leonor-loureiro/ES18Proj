package pt.ulisboa.tecnico.softeng.broker.domain;

import java.util.HashSet;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

public class BulkGettersTest {
	private static final int numberOfRooms = 2;
	private static final LocalDate arrival = new LocalDate(2018, 3, 5);
	private static final LocalDate departure = new LocalDate(2018, 3, 15);
	private static final BulkRoomBooking bulk = new BulkRoomBooking(numberOfRooms, arrival, departure);
	
	@Test
	public void success() {
		Assert.assertTrue(bulk.getReferences().equals(new HashSet<>()));
		Assert.assertTrue(bulk.getDeparture().equals(departure));
		Assert.assertTrue(bulk.getArrival().equals(arrival));
		Assert.assertTrue(bulk.getNumber() == 2);
	}
}
