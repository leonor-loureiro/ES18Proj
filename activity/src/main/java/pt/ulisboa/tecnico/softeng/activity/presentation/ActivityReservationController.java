package pt.ulisboa.tecnico.softeng.activity.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityOfferData;

@Controller
@RequestMapping(value = "/providers/{codeProvider}/activities/{codeActivity}/offers/reservations")
public class ActivityReservationController {
	private static Logger logger = LoggerFactory.getLogger(ActivityOfferController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String reservationForm(Model model, @PathVariable String codeProvider, @PathVariable String codeActivity) {
		logger.info("reservationForm");

		return null;
	}
	
//	@RequestMapping(method = RequestMethod.POST)
//	public String offerSubmit(Model model, @PathVariable String codeProvider, @PathVariable String codeActivity, @ModelAttribute ActivityOfferData offer) {
//		logger.info("reservationSubmit");
//
//		return "redirect:/providers/" + codeProvider + "/activities/" + codeActivity + "/offers/reservations";
//	}
}
