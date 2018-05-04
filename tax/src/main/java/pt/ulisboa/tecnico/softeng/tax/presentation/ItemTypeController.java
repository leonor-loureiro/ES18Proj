package pt.ulisboa.tecnico.softeng.tax.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;
import pt.ulisboa.tecnico.softeng.tax.services.local.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.ItemTypeData;

@Controller
@RequestMapping(value = "/itemtypes")
public class ItemTypeController {
	private static Logger logger = LoggerFactory.getLogger(ItemTypeController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public String itemtypeForm(Model model) {
		logger.info("itemtypeForm");
		model.addAttribute("itemtype", new ItemTypeData());
		model.addAttribute("itemtypes", TaxInterface.getItemTypes());
		return "itemtypes";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String itemtypeSubmit(Model model, @ModelAttribute ItemTypeData itemType) {
		logger.info("itemtypeSubmit name:{}, tax:{}", itemType.getName(), itemType.getTax());
		
		try {
			TaxInterface.createItemType(itemType);
		} catch(TaxException te) {
			model.addAttribute("error", "Error: it was not possible to create the itemtype");
			model.addAttribute("itemtype", itemType);
			model.addAttribute("itemtypes", TaxInterface.getItemTypes());
			return "itemtypes";
		}
		return "redirect:/itemtypes";
	}
}
