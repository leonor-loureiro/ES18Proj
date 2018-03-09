package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashSet;
import java.util.Set;

public class ItemType {
	
	public static Set<ItemType> itemTypes = new HashSet<>();
	
	private String itemType;
	private int tax;
	
	public ItemType(String itemType, int TAX) {
		
	}
}