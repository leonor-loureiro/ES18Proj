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
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.TaxPayerData;

@Controller
@RequestMapping(value = "/taxpayers")
public class TaxPayerController {
	private static Logger logger = LoggerFactory.getLogger(TaxPayerController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public String taxpayerForm(Model model) {
		logger.info("taxpayerForm");
		model.addAttribute("taxpayer", new TaxPayerData());
		model.addAttribute("taxpayers", TaxInterface.getTaxPayers());
		model.addAttribute("buyers", TaxInterface.getBuyers());
		model.addAttribute("sellers", TaxInterface.getSellers());
		return "taxpayers";
	}
	
	@RequestMapping(value = "/buyer", method = RequestMethod.POST)
	public String buyerSubmit(Model model, @ModelAttribute TaxPayerData taxPayer) {
		logger.info("buyerSubmit nif:{}, name:{}, address:{}", taxPayer.getNif(), taxPayer.getName(), taxPayer.getAddress());
		
		try {
			TaxInterface.createBuyer(taxPayer);
		} catch(TaxException te) {
			model.addAttribute("error", "Error: it was not possible to create the buyer");
			model.addAttribute("taxpayer", taxPayer);
			model.addAttribute("taxpayers", TaxInterface.getTaxPayers());
			model.addAttribute("buyers", TaxInterface.getBuyers());
			model.addAttribute("sellers", TaxInterface.getSellers());
			return "taxpayers";
		}
		return "redirect:/taxpayers";
	}
	
	@RequestMapping(value = "/seller", method = RequestMethod.POST)
	public String sellerSubmit(Model model, @ModelAttribute TaxPayerData taxPayer) {
		logger.info("sellerSubmit nif:{}, name:{}, address:{}", taxPayer.getNif(), taxPayer.getName(), taxPayer.getAddress());
		
		try {
			TaxInterface.createSeller(taxPayer);
		} catch(TaxException te) {
			model.addAttribute("error", "Error: it was not possible to create the seller");
			model.addAttribute("taxpayer", taxPayer);
			model.addAttribute("taxpayers", TaxInterface.getTaxPayers());
			model.addAttribute("buyers", TaxInterface.getBuyers());
			model.addAttribute("sellers", TaxInterface.getSellers());
			return "taxpayers";
		}
		return "redirect:/taxpayers";
	}
}