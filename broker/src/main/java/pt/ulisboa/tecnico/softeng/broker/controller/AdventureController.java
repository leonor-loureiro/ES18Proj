package pt.ulisboa.tecnico.softeng.broker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.broker.services.local.BrokerInterface;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.AdventureData;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.BrokerData;

@Controller
@RequestMapping(value = "/brokers/broker/{brokerCode}/adventures")
public class AdventureController {
	private static Logger logger = LoggerFactory.getLogger(AdventureController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String showBroker(Model model, @PathVariable String brokerCode) {
		logger.info("showBroker code:{}", brokerCode);

		BrokerData brokerData = BrokerInterface.getBrokerByCode(brokerCode);

		if (brokerData == null) {
			model.addAttribute("error", "Error: it does not exist a broker with the code " + brokerCode);
			model.addAttribute("broker", new BrokerData());
			model.addAttribute("brokers", BrokerInterface.getBrokers());
			return "brokers";
		} else {
			model.addAttribute("adventure", new AdventureData());
			model.addAttribute("broker", brokerData);
			return "broker";
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public String adventureSubmit(Model model, @PathVariable String brokerCode,
			@ModelAttribute AdventureData adventureData) {
		logger.info("adventureSubmit brokerCode:{}, begin:{}, end:{}, age:{}, iban:{}, ", brokerCode,
				adventureData.getBegin(), adventureData.getEnd(), adventureData.getAge(), adventureData.getIban(),
				adventureData.getAmount());

		try {
			BrokerInterface.createAdventure(brokerCode, adventureData);
		} catch (BrokerException be) {
			model.addAttribute("error", "Error: it was not possible to create the adventure");
			model.addAttribute("adventure", adventureData);
			model.addAttribute("broker", BrokerInterface.getBrokerByCode(brokerCode));
			return "broker";
		}

		return "redirect:/brokers/broker/" + brokerCode + "/adventures";
	}
}
