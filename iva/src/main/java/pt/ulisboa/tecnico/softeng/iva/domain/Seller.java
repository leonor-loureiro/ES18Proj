package pt.ulisboa.tecnico.softeng.iva.domain;

import java.util.HashSet;
import java.util.Set;

public class Seller extends TaxPayer {
	private final Set<Invoice> invoices = new HashSet<>();

	public Seller(String NIF, String name, String address) {
		super(NIF, name, address);
	}

	public void addInvoice(Invoice invoice) {
		this.invoices.add(invoice);
	}

	@Override
	public Invoice getInvoice(String invoiceReference) {
		for (Invoice invoice : this.invoices) {
			if (invoice.getReference().equals(invoiceReference)) {
				return invoice;
			}
		}
		return null;
	}

	public float toPay(int year) {
		float result = 0;
		for (Invoice invoice : this.invoices) {
			if (invoice.getDate().getYear() == year) {
				result = +invoice.getIva();
			}
		}
		return result;
	}

}
