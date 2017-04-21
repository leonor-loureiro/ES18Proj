package pt.ulisboa.tecnico.softeng.broker.services.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.RemoteAccessException;

public class BankInterface {
	private static Logger logger = LoggerFactory.getLogger(BankInterface.class);

	private static String ENDPOINT = "http://localhost:8082";

	public static String processPayment(String iban, int amount) {
		logger.info("processPayment iban:{}, amount:{}", iban, amount);

		RestTemplate restTemplate = new RestTemplate();
		try {
			String result = restTemplate.postForObject(
					ENDPOINT + "/rest/banks/accounts/" + iban + "/processPayment?amount=" + amount, null, String.class);
			return result;
		} catch (HttpClientErrorException e) {
			throw new BankException();
		} catch (Exception e) {
			throw new RemoteAccessException();
		}
	}

	public static String cancelPayment(String reference) {
		// TODO: implement in the final version as a rest invocation
		return null;
	}

	public static BankOperationData getOperationData(String reference) {
		// TODO: implement in the final version as a rest invocation
		return null;
	}

}
