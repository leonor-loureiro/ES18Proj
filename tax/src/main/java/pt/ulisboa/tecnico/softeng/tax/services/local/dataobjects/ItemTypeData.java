package pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.ItemType;

public class ItemTypeData {
	private String name;
	private int tax;
	private IRS irs;
	
	public ItemTypeData() {
	}
	
	public ItemTypeData(ItemType itemType) {
		this.name = itemType.getName();
		this.tax = itemType.getTax();
		this.irs = itemType.getIrs();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTax() {
		return tax;
	}

	public void setTax(int tax) {
		this.tax = tax;
	}

	public IRS getIrs() {
		return irs;
	}

	public void setIrs(IRS irs) {
		this.irs = irs;
	}
}