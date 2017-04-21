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

	public static String reserveActivity(LocalDate begin, LocalDate end, int age) {
		logger.info("reserveActivity begin:{}, end:{}, age:{}", begin, end, age);
		RestTemplate restTemplate = new RestTemplate();
		try {
			String result = restTemplate.postForObject(
					ENDPOINT + "/rest/providers/reserve?begin=" + begin + "&end=" + end + "&age=" + age, null,
					String.class);
			return result;
		} catch (HttpClientErrorException e) {
			throw new ActivityException();
		} catch (Exception e) {
			throw new RemoteAccessException();
		}
	}

	public static String cancelReservation(String activityConfirmation) {
		// TODO: implement in the final version as a rest invocation
		return null;
	}

	public static ActivityReservationData getActivityReservationData(String reference) {
		// TODO: implement in the final version as a rest invocation
		return null;
	}
}
