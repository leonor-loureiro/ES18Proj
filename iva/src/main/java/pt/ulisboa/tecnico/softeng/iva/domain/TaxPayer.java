package pt.ulisboa.tecnico.softeng.iva.domain;

import pt.ulisboa.tecnico.softeng.iva.exception.IvaException;

public abstract class TaxPayer {
	private final String NIF;
	private String name;
	private String address;

	public TaxPayer(String NIF, String name, String address) {
		checkArguments(NIF, name, address);

		this.NIF = NIF;
		this.name = name;
		this.address = address;

		IRS.getIRS().addTaxPayer(this);
	}

	private void checkArguments(String NIF, String name, String address) {
		if (name == null || name.length() == 0) {
			throw new IvaException();
		}

		if (address == null || address.length() == 0) {
			throw new IvaException();
		}

		if (IRS.getIRS().getTaxPayerByNIF(NIF) != null) {
			throw new IvaException();
		}

	}

	public String getNIF() {
		return this.NIF;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public abstract Invoice getInvoice(String invoiceReference);
}
