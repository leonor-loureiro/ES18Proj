package pt.ulisboa.tecnico.softeng.hotel.services.remote;

import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import pt.ulisboa.tecnico.softeng.hotel.services.remote.exceptions.RemoteAccessException;

import java.util.Map;
import java.util.TreeMap;

public class BankInterface {
	public static String processPayment(String IBAN, double amount) {
		try {
			String url = "http://localhost:8082/banksControl/processPayment/{IBAN}/amount/{amount}";

			RestTemplate rest = new RestTemplate();

			Map<String, Object> map = new TreeMap<>();
			map.put("IBAN", IBAN);
			map.put("amount", amount);

			return rest.getForObject(url, String.class, map);
		}catch(ResourceAccessException e) {
			throw new RemoteAccessException(e.getMessage());
		}
	}

	public static String cancelPayment(String reference) {
		try {
			String url = "http://localhost:8082/banksControl/cancelPayment/{reference}";

			RestTemplate rest = new RestTemplate();

			Map<String, Object> map = new TreeMap<>();
			map.put("reference", reference);

			return rest.getForObject(url, String.class, map);

		}catch(ResourceAccessException e){
			throw new RemoteAccessException(e.getMessage());
		}
	}
}
