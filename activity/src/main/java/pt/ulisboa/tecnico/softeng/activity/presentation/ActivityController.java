package pt.ulisboa.tecnico.softeng.activity.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.activity.services.local.ActivityInterface;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData;

@Controller
@RequestMapping(value = "/providers/{code}/activities")
public class ActivityController {
	private static Logger logger = LoggerFactory.getLogger(ActivityController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String activityForm(Model model, @PathVariable String code) {
		logger.info("activityForm providerCode:{}", code);

		ActivityProviderData providerData = ActivityInterface.getProviderDataByCode(code);

		if (providerData == null) {
			model.addAttribute("error", "Error: it does not exist an activity provider with the code " + code);
			model.addAttribute("provider", new ActivityProviderData());
			model.addAttribute("providers", ActivityInterface.getProviders());
			return "providers";
		}

		model.addAttribute("activity", new ActivityData());
		model.addAttribute("provider", providerData);
		return "activities";
	}

}
