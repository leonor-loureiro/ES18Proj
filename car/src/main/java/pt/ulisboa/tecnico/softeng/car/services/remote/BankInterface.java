package pt.ulisboa.tecnico.softeng.car.services.remote;

import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.TreeMap;

public class BankInterface {
	public static String processPayment(String IBAN, double amount) {

		String url = "http://localhost:8082/banksControl/processPayment/{IBAN}/amount/{amount}";

		RestTemplate rest = new RestTemplate();

		Map<String, Object> map = new TreeMap<>();
		map.put("IBAN", IBAN);
		map.put("amount", amount);

		return rest.getForObject(url, String.class, map);
	}

	public static String cancelPayment(String reference) {
		String url = "http://localhost:8082/banksControl/cancelPayment/{reference}";

		RestTemplate rest = new RestTemplate();

		Map<String, Object> map = new TreeMap<>();
		map.put("reference", reference);

		return rest.getForObject(url, String.class, map);
	}

}
