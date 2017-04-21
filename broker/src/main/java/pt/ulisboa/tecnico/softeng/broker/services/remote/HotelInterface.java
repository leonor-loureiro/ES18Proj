package pt.ulisboa.tecnico.softeng.broker.services.remote;

import java.util.Set;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.HotelException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.RemoteAccessException;

public class HotelInterface {
	private static Logger logger = LoggerFactory.getLogger(HotelInterface.class);

	private static String ENDPOINT = "http://localhost:8084";

	public static enum RoomType {
		SINGLE, DOUBLE
	}

	public static String reserveRoom(RoomType type, LocalDate arrival, LocalDate departure) {
		logger.info("reserveRoom arrival:{}, departure:{}", arrival, departure);
		RestTemplate restTemplate = new RestTemplate();
		try {
			String result = restTemplate.postForObject(
					ENDPOINT + "/rest/hotels/reserve?type=" + type + "&arrival=" + arrival + "&departure=" + departure,
					null, String.class);
			return result;
		} catch (HttpClientErrorException e) {
			throw new HotelException();
		} catch (Exception e) {
			throw new RemoteAccessException();
		}
	}

	public static String cancelBooking(String roomConfirmation) {
		// TODO: implement in the final version as a rest invocation
		return null;
	}

	public static RoomBookingData getRoomBookingData(String reference) {
		// TODO: implement in the final version as a rest invocation
		return null;
	}

	public static Set<String> bulkBooking(int number, LocalDate arrival, LocalDate departure) {
		// TODO: implement in the final version as a rest invocation
		return null;
	}
}
