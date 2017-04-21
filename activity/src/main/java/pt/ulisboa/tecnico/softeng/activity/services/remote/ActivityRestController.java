package pt.ulisboa.tecnico.softeng.activity.services.remote;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.services.local.ActivityInterface;

@RestController
@RequestMapping(value = "/rest/providers")
public class ActivityRestController {
	private static Logger logger = LoggerFactory.getLogger(ActivityRestController.class);

	@RequestMapping(value = "/reserve", method = RequestMethod.POST)
	public ResponseEntity<String> reserve(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
			@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate end, @RequestParam int age) {
		logger.info("reserve begin:{}, end:{}, age:{}", begin, end, age);
		try {
			return new ResponseEntity<>(ActivityInterface.reserveActivity(begin, end, age), HttpStatus.OK);
		} catch (ActivityException be) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
