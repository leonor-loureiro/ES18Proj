package pt.ulisboa.tecnico.softeng.iva.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.iva.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.iva.exception.IvaException;

public class IRS {
	private final Set<TaxPayer> taxPayers = new HashSet<>();
	private final Set<ItemType> itemTypes = new HashSet<>();

	private static IRS instance;

	public static IRS getIRS() {
		if (instance == null) {
			instance = new IRS();
		}
		return instance;
	}

	private IRS() {
	}

	void addTaxPayer(TaxPayer taxPayer) {
		this.taxPayers.add(taxPayer);
	}

	public TaxPayer getTaxPayerByNIF(String NIF) {
		for (TaxPayer taxPayer : this.taxPayers) {
			if (taxPayer.getNIF().equals(NIF)) {
				return taxPayer;
			}
		}
		return null;
	}

	void addItemType(ItemType itemType) {
		if (getTaxPayerByNIF(itemType.getName()) != null) {
			throw new IvaException();
		}

		this.itemTypes.add(itemType);
	}

	public ItemType getItemTypeByName(String name) {
		for (ItemType itemType : this.itemTypes) {
			if (itemType.getName().equals(name)) {
				return itemType;
			}
		}
		return null;
	}

	public String submitInvoice(InvoiceData invoiceData) {
		Seller seller = (Seller) getTaxPayerByNIF(invoiceData.getSellerNIF());
		Buyer buyer = (Buyer) getTaxPayerByNIF(invoiceData.getBuyerNIF());
		ItemType itemType = getItemTypeByName(invoiceData.getItemType());
		Invoice invoice = new Invoice(invoiceData.getValue(), invoiceData.getDate(), itemType, seller, buyer);

		return invoice.getReference();
	}

	public void removeItemTypes() {
		this.itemTypes.clear();
	}

	public void removeTaxPayers() {
		this.taxPayers.clear();
	}

	public void clearAll() {
		removeItemTypes();
		removeTaxPayers();
	}

}
