package pt.ulisboa.tecnico.softeng.broker.services.remote;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.RemoteAccessException;

public class ActivityInterface {
	private static Logger logger = LoggerFactory.getLogger(ActivityInterface.class);

	private static String ENDPOINT = "http://localhost:8081";

	public static String reserveActivity(LocalDate begin, LocalDate end, int age, String nif, String iban,
			String adventureId) {
		logger.info("reserveActivity begin:{}, end:{}, age:{}, adventureId:{}", begin, end, age, adventureId);
		RestTemplate restTemplate = new RestTemplate();
		try {
			String result = restTemplate
					.postForObject(
							ENDPOINT + "/rest/providers/reserve?begin=" + begin + "&end=" + end + "&age=" + age
									+ "&nif=" + nif + "&iban=" + iban + "&adventureId=" + adventureId,
							null, String.class);
			return result;
		} catch (HttpClientErrorException e) {
			logger.info("reserveActivity HttpClientErrorException begin:{}, end:{}, age:{}", begin, end, age);
			throw new ActivityException();
		} catch (Exception e) {
			logger.info("reserveActivity Exception begin:{}, end:{}, age:{}", begin, end, age);
			throw new RemoteAccessException();
		}
	}

	public static String cancelReservation(String activityConfirmation) {
		logger.info("cancelReservation activityConfirmation:{}", activityConfirmation);
		RestTemplate restTemplate = new RestTemplate();
		try {
			String result = restTemplate.postForObject(
					ENDPOINT + "/rest/providers/cancel?reference=" + activityConfirmation, null, String.class);
			return result;
		} catch (HttpClientErrorException e) {
			logger.info("cancelReservation HttpClientErrorException activityConfirmation:{}", activityConfirmation);
			throw new ActivityException();
		} catch (Exception e) {
			logger.info("cancelReservation Exception activityConfirmation:{}", activityConfirmation);
			throw new RemoteAccessException();
		}
	}

	public static ActivityReservationData getActivityReservationData(String reference) {
		logger.info("getActivityReservationData reference:{}", reference);
		RestTemplate restTemplate = new RestTemplate();
		try {
			ActivityReservationData result = restTemplate.getForObject(
					ENDPOINT + "/rest/providers/reservation?reference=" + reference, ActivityReservationData.class);
			return result;
		} catch (HttpClientErrorException e) {
			logger.info("getActivityReservationData HttpClientErrorException:{}", reference);
			throw new ActivityException();
		} catch (Exception e) {
			logger.info("getActivityReservationData Exception:{}", reference);
			throw new RemoteAccessException();
		}

	}
}
