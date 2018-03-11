package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashSet;
import java.util.Set;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class Seller extends TaxPayer{
	
	public Seller(String nif, String name, String address) {
		super(nif, name, address);
	}
	
	
	public Seller(TaxPayer taxPayer) {
		
		if (taxPayer instanceof Seller || taxPayer == null) {
			throw new TaxException();
		}
		String nifTemp = taxPayer.getNif();
		String nameTemp = taxPayer.getName();
		String addressTemp = taxPayer.getAddress();
		
		TaxPayer.taxPayers.remove(taxPayer);
		
		this.nif = nifTemp;
		this.name = nameTemp;
		this.address = addressTemp;
		
		TaxPayer.taxPayers.add(this);
		
		
	}
	
	public double toPay(int YEAR) {
		checkArguments(YEAR);
		
		Set<Invoice> invoices = new HashSet<>();
		invoices = Invoice.getInvoiceByYear(YEAR);
		
		double returnValue = 0;
		for (Invoice invoice : invoices) {
			returnValue += invoice.getValue() * (invoice.getIVA()/100);
		}
		
		return returnValue;
	}
		
	private void checkArguments(int year) {
		if (year < 1970) {
			throw new TaxException();
		}
	}
	
	public Invoice getInvoiceByReference(String INVOICE_REFERENCE) {
		for (Invoice invoice : Invoice.invoices) {
			if (invoice.getReference().equals(INVOICE_REFERENCE)) {
				return invoice;
			}
		}
		throw new TaxException();
	}
	
	
}