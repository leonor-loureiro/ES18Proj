package pt.ulisboa.tecnico.softeng.broker.services.remote;

import java.util.Arrays;
import java.util.HashSet;
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
	public static enum Type {
		SINGLE, DOUBLE
	}

	private static Logger logger = LoggerFactory.getLogger(HotelInterface.class);

	private static String ENDPOINT = "http://localhost:8085";

	public static String reserveRoom(Type type, LocalDate arrival, LocalDate departure, String nif, String iban,
			String adventureId) {
		logger.info("reserveRoom arrival:{}, departure:{}, nif:{}, iban:{}, adventureId:{}", arrival, departure, nif,
				iban, adventureId);
		RestTemplate restTemplate = new RestTemplate();
		try {
			String result = restTemplate
					.postForObject(
							ENDPOINT + "/rest/hotels/reserve?type=" + type + "&arrival=" + arrival + "&departure="
									+ departure + "&nif=" + nif + "&iban=" + iban + "&adventureId=" + adventureId,
							null, String.class);
			return result;
		} catch (HttpClientErrorException e) {
			logger.info("reserveRoom HttpClientErrorException arrival:{}, departure:{}, adventureId:{}", arrival,
					departure, adventureId);
			throw new HotelException();
		} catch (Exception e) {
			logger.info("reserveRoom Exception arrival:{}, departure:{}, adventureId:{}", arrival, departure,
					adventureId);
			throw new RemoteAccessException();
		}
	}

	public static String cancelBooking(String roomConfirmation) {
		logger.info("cancelBooking roomConfirmation:{}", roomConfirmation);
		RestTemplate restTemplate = new RestTemplate();
		try {
			String result = restTemplate.postForObject(ENDPOINT + "/rest/hotels/cancel?reference=" + roomConfirmation,
					null, String.class);
			return result;
		} catch (HttpClientErrorException e) {
			logger.info("cancelBooking HttpClientErrorException roomConfirmation:{}", roomConfirmation);
			throw new HotelException();
		} catch (Exception e) {
			logger.info("cancelBooking RemoteAccessException roomConfirmation:{}", roomConfirmation);
			throw new RemoteAccessException();
		}
	}

	public static RoomBookingData getRoomBookingData(String reference) {
		logger.info("getRoomBookingData reference:{}", reference);
		RestTemplate restTemplate = new RestTemplate();
		try {
			RoomBookingData result = restTemplate.getForObject(ENDPOINT + "/rest/hotels/booking?reference=" + reference,
					RoomBookingData.class);
			return result;
		} catch (HttpClientErrorException e) {
			logger.info("getRoomBookingData HttpClientErrorException reference:{}", reference);
			throw new HotelException();
		} catch (Exception e) {
			logger.info("getRoomBookingData Exception reference:{}", reference);
			throw new RemoteAccessException();
		}
	}

	public static Set<String> bulkBooking(int number, LocalDate arrival, LocalDate departure, String nif, String iban) {
		logger.info("bulkBooking number:{}, arrival:{}, departure:{}, nif:{}, iban:{}", number, arrival, departure);
		RestTemplate restTemplate = new RestTemplate();
		try {
			String[] result = restTemplate.postForObject(ENDPOINT + "/rest/hotels/bulk?number=" + number + "&arrival="
					+ arrival + "&departure=" + departure + "&nif=" + nif + "&iban=" + iban, null, String[].class);
			return new HashSet<>(Arrays.asList(result));
		} catch (HttpClientErrorException e) {
			throw new HotelException();
		} catch (Exception e) {
			logger.info("bulkBooking Exception");
			throw new RemoteAccessException();
		}

	}
}
