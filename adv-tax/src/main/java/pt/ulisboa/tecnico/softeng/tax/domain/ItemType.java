package pt.ulisboa.tecnico.softeng.tax.domain;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class ItemType {
	public final String name;
	public int tax;

	public ItemType(String name, int tax) {
		checkArguments(name, tax);
		this.name = name;
		this.tax = tax;

		IRS.getIRS().addItemType(this);
	}

	private void checkArguments(String name, int tax) {
		if (name == null || name.isEmpty()) {
			throw new TaxException();
		}

		if (IRS.getIRS().getItemTypeByName(name) != null) {
			throw new TaxException();
		}

		if (tax < 0) {
			throw new TaxException();
		}
	}

	public String getName() {
		return this.name;
	}

	public int getTax() {
		return this.tax;
	}

}
