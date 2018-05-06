package pt.ulisboa.tecnico.softeng.activity.presentation;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.activity.services.local.ActivityInterface;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityBookingData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityOfferData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData;

@Controller
@RequestMapping(value = "/providers/{codeProvider}/activities/{codeActivity}/offers/{dateBegin}/{dateEnd}/reservations")
public class ActivityReservationController {
	private static Logger logger = LoggerFactory.getLogger(ActivityOfferController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String reservationForm(Model model, @PathVariable String codeProvider, @PathVariable String codeActivity, @PathVariable String dateBegin, @PathVariable String dateEnd) {
		logger.info("reservationForm codeProvider:{}, codeActivity:{}, dateBegin:{}, dateEnd:{}", codeProvider, codeActivity, dateBegin, dateEnd);
		
		ActivityData ad = ActivityInterface.getActivityDataByCode(codeProvider, codeActivity);
		
		if (ad == null) {
			model.addAttribute("provider", new ActivityProviderData());
			model.addAttribute("providers", ActivityInterface.getProviders());
			return "providers";
		}
		
		for(ActivityOfferData o: ad.getOffers()) {
			if (o.getBegin().equals(new LocalDate(dateBegin)) && o.getEnd().equals(new LocalDate(dateEnd))) {
				model.addAttribute("offer", o);
				model.addAttribute("activity", ad);
				model.addAttribute("booking", new ActivityBookingData());
				return "reservations";
			}
		}
		
		model.addAttribute("provider", new ActivityProviderData());
		model.addAttribute("providers", ActivityInterface.getProviders());
		return "providers";
	}
	
//	@RequestMapping(method = RequestMethod.POST)
//	public String offerSubmit(Model model, @PathVariable String codeProvider, @PathVariable String codeActivity, @PathVariable String dateBegin, @PathVariable String dateEnd) {
//		logger.info("reservationSubmit");
//
//		return "redirect:/providers/" + codeProvider + "/activities/" + codeActivity + "/offers/reservations";
//	}
}
