package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class Buyer extends TaxPayer {
	
	public Buyer(String nif, String name, String address) {
		super(nif, name, address);
	}
	
	private double taxReturn(int year) {
		checkArguments(year);
		
		Set<Invoice> invoices = new HashSet<>();
		invoices = Invoice.getInvoiceByYear(year);
		
		double returnValue = 0;
		double itemIvaValue;
		for (Invoice invoice : invoices) {
			itemIvaValue = (invoice.getValue() / ( 1 + (invoice.getItemType().getTax() / 100))) * (invoice.getItemType().getTax());
			returnValue = returnValue + (itemIvaValue * 0.05);
		}
		
		return returnValue;
	}
	
	private void checkArguments(int year) {
		if (year < 1970) {
			throw new TaxException();
		}
	}
}