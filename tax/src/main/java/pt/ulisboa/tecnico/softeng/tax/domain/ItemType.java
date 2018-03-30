package pt.ulisboa.tecnico.softeng.tax.domain;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class ItemType {
	public final String name;
	public int tax;

	public ItemType(IRS irs, String name, int tax) {
		checkArguments(irs, name, tax);
		this.name = name;
		this.tax = tax;

		irs.addItemType(this);
	}

	private void checkArguments(IRS irs, String name, int tax) {
		if (name == null || name.isEmpty()) {
			throw new TaxException();
		}

		if (irs.getItemTypeByName(name) != null) {
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
