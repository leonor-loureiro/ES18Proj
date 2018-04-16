package pt.ulisboa.tecnico.softeng.tax.domain;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class Buyer extends Buyer_Base {
	private final static int PERCENTAGE = 5;

	public Buyer(IRS irs, String NIF, String name, String address) {
		init(irs, NIF, name, address);
	}

	public double taxReturn(int year) {
		if (year < 1970) {
			throw new TaxException();
		}

		double result = 0;
		for (Invoice invoice : getInvoiceSet()) {
			if (!invoice.isCancelled() && invoice.getDate().getYear() == year) {
				result = result + invoice.getIva() * PERCENTAGE / 100;
			}
		}
		return result;
	}
	
	public void delete() {
		setIrs(null);
		setInvoice2(null);
		deleteDomainObject();
	}
}