package pt.ulisboa.tecnico.softeng.tax.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;
import pt.ulisboa.tecnico.softeng.tax.services.local.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.TaxPayerData;

@Controller
@RequestMapping(value = "/taxpayers/{nif}/invoices")
public class InvoiceController {
	private static Logger logger = LoggerFactory.getLogger(InvoiceController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public String invoiceForm(Model model, @PathVariable String nif) {
		logger.info("invoiceForm taxpayerNif:{}", nif);

		TaxPayerData taxPayerData = TaxInterface.getTaxPayerDataByNif(nif);

		if (taxPayerData == null) {
			model.addAttribute("error", "Error: it does not exist a taxpayer with the nif " + nif);
			model.addAttribute("taxpayer", new TaxPayerData());
			model.addAttribute("taxpayers", TaxInterface.getTaxPayers());
			return "taxpayers";
		}

		model.addAttribute("invoice", new InvoiceData());
		model.addAttribute("taxpayer", taxPayerData);
		return "invoices";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String invoiceSubmit(Model model, @PathVariable String nif, @ModelAttribute InvoiceData invoice) {
		logger.info("invoiceSubmit sellerNif:{}, buyerNif:{}, itemTypeName:{}, value:{}, date:{}",
		invoice.getSellerNIF(), invoice.getBuyerNIF(), invoice.getItemType(), invoice.getValue(), invoice.getDate()); 

		try {
			TaxInterface.createInvoice(invoice);
		} catch (TaxException te) {
			model.addAttribute("error", "Error: it was not possible to create the invoice");
			model.addAttribute("invoice", invoice);
			model.addAttribute("taxpayer", TaxInterface.getTaxPayerDataByNif(nif));
			return "invoices";
		}
		
		return "redirect:/taxpayers/" + nif + "/invoices";
	}
}