package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class ItemType {
	
	public static Set<ItemType> itemTypes = new HashSet<>();
	
	private String itemType;
	private int tax;
	
	public ItemType(String itemType, int TAX) {
		
		checkArguments(itemType, TAX);
		this.itemType = itemType;
		this.tax = TAX;
		ItemType.itemTypes.add(this);
	}
	
	private void checkArguments(String itemType, int TAX) {
		if (itemType == null || itemType.trim().length() == 0) {
			throw new TaxException();
		}

		if (TAX < 0) {
			throw new TaxException();
		}

		for (ItemType items : itemTypes) {
			if (items.getItemType().equals(this.itemType)) {
				throw new TaxException();
			}
		}
	}
	
	public String getItemType() {
		return this.itemType;
	}
	
	public int getTax() {
		return this.tax;
	}
	
	public static int findTaxByType(String itemType) {
		for (ItemType items : itemTypes) {
			if (items.getItemType().equals(this.itemType)) {
				return items.getTax();
			}
		}
		throw new TaxException();
	}
}