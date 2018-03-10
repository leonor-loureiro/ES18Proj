package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public abstract class TaxPayer {
	public static Set<TaxPayer> taxPayers = new HashSet<>();
	
	static final int NIF_SIZE = 9;
	
	protected final String nif;
	protected final String name;
	protected String address;

	public TaxPayer(String nif, String name, String address) {
		checkArguments(nif, name, address);
		
		this.nif = nif;
		this.name = name;
		this.address = address;
		
		TaxPayer.taxPayers.add(this);
	}
	
	private void checkArguments(String nif, String name, String address) {
		if (nif == null || name == null || address == null || name.trim().length() == 0 || address.trim().length() == 0) {
			throw new TaxException();
		}

		if (nif.length() != TaxPayer.NIF_SIZE) {
			throw new TaxException();
		}

		for (TaxPayer payer : taxPayers) {
			if (payer.getNif().equals(nif)) {
				throw new TaxException();
			}
		}
	}
	
	public String getNif() {
		return this.nif;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getAddress() {
		return this.address;
	}
}