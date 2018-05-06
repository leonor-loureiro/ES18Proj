package pt.ulisboa.tecnico.softeng.hotel.services.remote;

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
		// return Bank.cancelPayment(reference);
		// TODO: implement in the final version as a rest invocation
		return null;
	}
}
