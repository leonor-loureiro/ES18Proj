package pt.ulisboa.tecnico.softeng.broker.services.remote;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.joda.time.LocalDate;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.RemoteAccessException;

public class HotelInterface {
	public static enum Type {
		SINGLE, DOUBLE
	}

	public static String reserveRoom(Type type, LocalDate arrival, LocalDate departure, String buyerNif,
			String buyerIban) {
		try {
			String url = "http://localhost:8085/hotelsControl/reserveRoom/{type}/arrival/{arrival}/departure/{departure}/buyerNif/{buyerNif}/buyerIban/{buyerIban}";
			
			RestTemplate rest = new RestTemplate();
		
			Map<String, Object> map = new TreeMap<>();
				map.put("type", type);
				map.put("arrival", arrival);
				map.put("departure", departure);
				map.put("buyerNif", buyerNif);
				map.put("buyerIban", buyerIban);
		
			return rest.getForObject(url, String.class, map);
			
		}catch(ResourceAccessException e) {
			throw new RemoteAccessException(e.getMessage());
		}
		
	}

	public static String cancelBooking(String roomConfirmation) {
		try {
			String url = "http://localhost:8085/hotelsControl/cancelBooking/{reference}";
			
			RestTemplate rest = new RestTemplate();
		
			Map<String, Object> map = new TreeMap<>();
				map.put("reference", roomConfirmation);
		
			return rest.getForObject(url, String.class, map);
			
		}catch(ResourceAccessException e) {
			throw new RemoteAccessException(e.getMessage());
		}
	}

	public static RoomBookingData getRoomBookingData(String reference) {
		try {
			String url = "http://localhost:8085/hotelsControl/getRoomBookingData/{reference}";
			
			RestTemplate rest = new RestTemplate();
		
			Map<String, Object> map = new TreeMap<>();
				map.put("reference", reference);
		
			return rest.getForObject(url, RoomBookingData.class, map);
		}catch(ResourceAccessException e) {
			throw new RemoteAccessException(e.getMessage());
		}
		
	}

	public static Set<String> bulkBooking(int number, LocalDate arrival, LocalDate departure, String buyerNif,
			String buyerIban) {
		try {
			String url = "http://localhost:8085/hotelsControl//bulkBooking/{number}/arrival/{arrival}/departure/{departure}/buyerNif/{buyerNif}/buyerIban/{buyerIban}";
			
			RestTemplate rest = new RestTemplate();
		
			Map<String, Object> map = new TreeMap<>();
				map.put("number", number);
				map.put("arrival", arrival);
				map.put("departure", departure);
				map.put("buyerNif", buyerNif);
				map.put("buyerIban", buyerIban);
		
			String[] bookings = rest.getForObject(url, String[].class, map);
			return new HashSet<String>(Arrays.asList(bookings));
			
		}catch(ResourceAccessException e) {
			throw new RemoteAccessException(e.getMessage());
		}
	}

}
