package pt.ulisboa.tecnico.softeng.broker.services.remote;

import java.util.Map;
import java.util.TreeMap;

import org.joda.time.LocalDate;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.RemoteAccessException;

public class ActivityInterface {
	public static String reserveActivity(LocalDate begin, LocalDate end, int age, String nif, String iban) {
		try{
			String url = "http://localhost:8081/activitiesControl/reserveActivity/begin/{begin}/end/{end}/age/{age}/nif/{nif}/iban/{iban}";
		
			RestTemplate rest = new RestTemplate();
		
			Map<String, Object> map = new TreeMap<>();
				map.put("begin", begin);
				map.put("end", end);
				map.put("age", age);
				map.put("nif", nif);
				map.put("iban", iban);
				
		
			return rest.getForObject(url, String.class, map);
			
		}catch(ResourceAccessException e) {
			throw new RemoteAccessException(e.getMessage());
		}
	}

	public static String cancelReservation(String activityConfirmation) {
		try{
			String url = "http://localhost:8081/activitiesControl/cancelReservation/{reference}";
		
			RestTemplate rest = new RestTemplate();
		
			Map<String, Object> map = new TreeMap<>();
				map.put("reference", activityConfirmation);
		
			return rest.getForObject(url, String.class, map);
		
		}catch(ResourceAccessException e) {
			throw new RemoteAccessException(e.getMessage());
		}
	
	}

	public static ActivityReservationData getActivityReservationData(String reference) {
		try{
			String url = "http://localhost:8081/activitiesControl/getReservationData/{reference}";
		
			RestTemplate rest = new RestTemplate();
		
			Map<String, Object> map = new TreeMap<>();
				map.put("reference", reference);
		
			return rest.getForObject(url, ActivityReservationData.class, map);
		
		}catch(ResourceAccessException e) {
			throw new RemoteAccessException(e.getMessage());
		}
	}

}
