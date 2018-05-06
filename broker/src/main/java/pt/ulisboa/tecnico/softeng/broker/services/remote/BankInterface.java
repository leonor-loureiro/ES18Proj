package pt.ulisboa.tecnico.softeng.broker.services.remote;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.RemoteAccessException;

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
		}catch(ResourceAccessException e) {
			throw new RemoteAccessException(e.getMessage());
		}
	}

	public static BankOperationData getOperationData(String reference) {
		try {
			String url = "http://localhost:8082/banksControl/getOperationData/{reference}";
			RestTemplate rest = new RestTemplate();
		
			Map<String, String> map = new TreeMap<>();
				map.put("reference", reference);
		
			return rest.getForObject(url, BankOperationData.class, map);
		}catch(ResourceAccessException e) {
			throw new RemoteAccessException(e.getMessage());
		}
	}
}
