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

	public static String processPayment(String iban, int amount, String adventureId) {
		logger.info("processPayment iban:{}, amount:{}, adventureId:{}", iban, amount, adventureId);

		RestTemplate restTemplate = new RestTemplate();
		try {
			String result = restTemplate.postForObject(ENDPOINT + "/rest/banks/accounts/" + iban
					+ "/processPayment?amount=" + amount + "&adventureId=" + adventureId, null, String.class);
			return result;
		} catch (HttpClientErrorException e) {
			throw new BankException();
		} catch (Exception e) {
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
			throw new BankException();
		} catch (Exception e) {
			throw new RemoteAccessException();
		}
	}

	public static BankOperationData getOperationData(String reference) {
		logger.info("getOperationData reference:{}", reference);

		RestTemplate restTemplate = new RestTemplate();
		try {
			BankOperationData result = restTemplate
					.getForObject(ENDPOINT + "/rest/banks/operation?reference=" + reference, BankOperationData.class);
			logger.info("getOperationData iban:{}", result.getIban());
			return result;
		} catch (HttpClientErrorException e) {
			throw new BankException();
		} catch (Exception e) {
			logger.info("getOperationData REMOTE");

			throw new RemoteAccessException();
		}
	}

}
