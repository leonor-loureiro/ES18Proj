package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public abstract class TaxPayer {
	protected final Set<Invoice> invoices = new HashSet<>();

	private final String NIF;
	private final String name;
	private final String address;

	public TaxPayer(IRS irs, String NIF, String name, String address) {
		checkArguments(irs, NIF, name, address);

		this.NIF = NIF;
		this.name = name;
		this.address = address;

		irs.addTaxPayer(this);
	}

	private void checkArguments(IRS irs, String NIF, String name, String address) {
		if (NIF == null || NIF.length() != 9) {
			throw new TaxException();
		}

		if (name == null || name.length() == 0) {
			throw new TaxException();
		}

		if (address == null || address.length() == 0) {
			throw new TaxException();
		}

		if (irs.getTaxPayerByNIF(NIF) != null) {
			throw new TaxException();
		}

	}

	public void addInvoice(Invoice invoice) {
		this.invoices.add(invoice);
	}

	public Invoice getInvoiceByReference(String invoiceReference) {
		if (invoiceReference == null || invoiceReference.isEmpty()) {
			throw new TaxException();
		}

		for (Invoice invoice : this.invoices) {
			if (invoice.getReference().equals(invoiceReference)) {
				return invoice;
			}
		}
		return null;
	}

	public String getNIF() {
		return this.NIF;
	}

	public String getName() {
		return this.name;
	}

	public String getAddress() {
		return this.address;
	}

}
