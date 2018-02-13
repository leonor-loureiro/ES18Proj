package pt.ulisboa.tecnico.softeng.iva.domain;

import java.util.HashSet;
import java.util.Set;

public class Buyer extends TaxPayer {
	private final static int PERCENTAGE = 5;
	private final Set<Invoice> invoices = new HashSet<>();

	public Buyer(String NIF, String name, String address) {
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

	public float taxReturn(int year) {
		float result = 0;
		for (Invoice invoice : this.invoices) {
			if (invoice.getDate().getYear() == year) {
				result = +invoice.getIva() * PERCENTAGE / 100;
			}
		}
		return result;
	}
}
