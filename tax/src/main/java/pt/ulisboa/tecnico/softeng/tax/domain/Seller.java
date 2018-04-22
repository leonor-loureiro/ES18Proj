package pt.ulisboa.tecnico.softeng.tax.domain;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class Seller extends Seller_Base {
	public Seller(IRS irs, String NIF, String name, String address) {
		init(irs, NIF, name, address);
	}

	public double toPay(int year) {
		if (year < 1970) {
			throw new TaxException();
		}

		double result = 0;
		for (Invoice invoice : getInvoiceSet()) {
			if (!invoice.isCancelled() && invoice.getDate().getYear() == year) {
				result = result + invoice.getIva();
			}
		}
		return result;
	}

	public void delete() {
		setIrs(null);

		for(Invoice inv : getInvoiceSet()) {
			inv.delete();
		}
		
		deleteDomainObject();		
	}
}
