package pt.ulisboa.tecnico.softeng.tax.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;
import pt.ulisboa.tecnico.softeng.tax.services.local.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.InvoiceData; 


@RestController 
@RequestMapping(value = "/taxpayersControl") 
public class TaxRestController { 
	private static Logger logger = LoggerFactory.getLogger(TaxRestController.class); 

	@RequestMapping(value = "/submitInvoice" , method = RequestMethod.POST) 
	public String submitInvoice(@RequestBody InvoiceData invoice) { 
		logger.info("submitInvoice sellerNif:{}, buyerNif:{}, itemTypeName:{}, value:{}, date:{}", invoice.getSellerNIF(), invoice.getBuyerNIF(), invoice.getItemType(), invoice.getValue(), invoice.getDate()); 
		try {
			return TaxInterface.submitInvoice(invoice); 
		} catch (TaxException te) {
			logger.error("Tax Exception caught - Error trying to submit invoice"); 
			return null;
		}
	}

	@RequestMapping(value="/cancelInvoice/{reference}" ,method = RequestMethod.GET) 
	public void cancelInvoice(@PathVariable String reference) { 
		logger.info("cancelInvoice reference:{}", reference); 
		try { 
			TaxInterface.cancelInvoice(reference);
		} catch (TaxException te) { 
			logger.error("Tax Exception caught - Error trying to cancel invoice " + reference);  
		}
	}
}