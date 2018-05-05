package pt.ulisboa.tecnico.softeng.bank.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.bank.services.local.BankInterface;

import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankOperationData;


@RestController
@RequestMapping(value = "/banksControl")
public class BankRestController {
	private static Logger logger = LoggerFactory.getLogger(BankRestController.class);

	@RequestMapping(value = "/getOperationData/{reference}" , method = RequestMethod.GET)
	public BankOperationData getOperationData(@PathVariable String reference) {
		logger.info("getOperationData reference:{}", reference);
		try {
			return BankInterface.getOperationData(reference);
		} catch (BankException be) {
			logger.error("Bank Exception caught - Error trying to get Operation Data " + reference);
			return null;
		}
	}
	// usage example:
	//	String url = "http://localhost:8082/banksControl/getOperationData/{reference}";
	//	RestTemplate rest = new RestTemplate();
	//
	//	Map<String, String> map = new TreeMap<>();
	//		map.put("reference", this.reference);
	//
	//	BankOperationData data = rest.getForObject(url, BankOperationData.class, map);



	@RequestMapping(value="/cancelPayment/{reference}" ,method = RequestMethod.GET)
	public String cancelPayment(@PathVariable String reference) {
		logger.info("cancelPayment reference:{}", reference);
		try {
			return BankInterface.cancelPayment(reference);
		} catch (BankException be) {
			logger.error("Bank Exception caught - Error trying to cancel payment " + reference);
			return null;
		}
	}

	@RequestMapping(value="/processPayment/{IBAN}/amount/{amount}", method = RequestMethod.GET)
	public String processPayment(@PathVariable String IBAN, @PathVariable double amount) {
		logger.info("processPayment IBAN:{} , AMOUNT:{}", IBAN, amount);
		try {
			return BankInterface.processPayment(IBAN, amount);
		} catch (BankException be) {
			logger.error("Bank Exception caught - Error trying to process Payment IBAN: " + IBAN + "   amount:" +amount);
			return null;
		}
	}


}
