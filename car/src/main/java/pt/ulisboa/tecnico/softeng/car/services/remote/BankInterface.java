package pt.ulisboa.tecnico.softeng.car.services.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import pt.ulisboa.tecnico.softeng.car.services.remote.exceptions.BankException;
import pt.ulisboa.tecnico.softeng.car.services.remote.exceptions.RemoteAccessException;

public class BankInterface {
	private static Logger logger = LoggerFactory.getLogger(BankInterface.class);

	private static String ENDPOINT = "http://localhost:8082";

	public static String processPayment(String iban, double amount, String transactionSource,
			String transactionReference) {
		logger.info("processPayment iban:{}, amount:{}, transactionSource:{}, transactionReference:{}", iban, amount,
				transactionSource, transactionReference);

		RestTemplate restTemplate = new RestTemplate();
		try {
			String result = restTemplate.postForObject(ENDPOINT + "/rest/banks/accounts/" + iban
					+ "/processPayment?amount=" + amount + "&transactionSource=" + transactionSource
					+ "&transactionReference=" + transactionReference, null, String.class);
			return result;
		} catch (HttpClientErrorException e) {
			logger.info("processPayment HttpClientErrorException  iban:{}, amount:{}", iban, amount);
			throw new BankException();
		} catch (Exception e) {
			logger.info("processPayment Exception  iban:{}, amount:{}", iban, amount);
			throw new RemoteAccessException();
		}
	}

	public static String cancelPayment(String reference) {
		logger.info("cancelPayment reference:{}", reference);

		RestTemplate restTemplate = new RestTemplate();
		try {
			String result = restTemplate.postForObject(ENDPOINT + "/rest/banks/cancel?reference=" + reference, null,
					String.class);
			return result;
		} catch (HttpClientErrorException e) {
			logger.info("cancelPayment HttpClientErrorException reference:{}", reference);
			throw new BankException();
		} catch (Exception e) {
			logger.info("cancelPayment Exception reference:{}", reference);
			throw new RemoteAccessException();
		}
	}

}
