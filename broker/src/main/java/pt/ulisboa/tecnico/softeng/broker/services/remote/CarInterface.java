package pt.ulisboa.tecnico.softeng.broker.services.remote;

import java.util.Map;
import java.util.TreeMap;

import org.joda.time.LocalDate;
import org.springframework.web.client.RestTemplate;

import pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects.RentingData;

public class CarInterface {
	public static enum Type {
		CAR, MOTORCYCLE
	}

	public static String rentCar(Type vehicleType, String drivingLicense, String nif, String iban, LocalDate begin,
			LocalDate end) {
		String url = "http://localhost:8084/rentACarsControl/rent/type/{type}/license/{license}/begin/{begin}/end/{end}/nif/{nif}/iban/{iban}";
		
		RestTemplate rest = new RestTemplate();
	
		Map<String, Object> map = new TreeMap<>();
			map.put("type", vehicleType);
			map.put("license", drivingLicense);
			map.put("begin", begin);
			map.put("end", end);
			map.put("nif", nif);
			map.put("iban", iban);
	
		return rest.getForObject(url, String.class, map);
	}

	public static String cancelRenting(String rentingReference) {
		String url = "http://localhost:8084/rentACarsControl/cancelRenting/{reference}";
		
		RestTemplate rest = new RestTemplate();
	
		Map<String, Object> map = new TreeMap<>();
			map.put("reference", rentingReference);
	
		return rest.getForObject(url, String.class, map);
	
	}

	public static RentingData getRentingData(String reference) {
		String url = "http://localhost:8084/rentACarsControl/getRentingData/{reference}";
		
		RestTemplate rest = new RestTemplate();
	
		Map<String, Object> map = new TreeMap<>();
			map.put("reference", reference);
	
		return rest.getForObject(url, RentingData.class, map);
	}

}
