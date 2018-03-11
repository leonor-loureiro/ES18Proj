package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.invoiceData;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public final class IRS {  // singleton
	private static final IRS irs = new IRS();
	
	private IRS() {}
	
	public static IRS getIRS() {
		return irs;
	}
	
	public static ItemType getItemTypeByName(String name) {
		for (ItemType items : ItemType.getItemTypes()) {
			if (items.getItemType().equals(name)) {
				return items;
			}
		}
		throw new TaxException();
	}
	
	public static TaxPayer getTaxPayerByNIF(String nif) {
		for (TaxPayer payer : TaxPayer.getTaxPayers()) {
			if (payer.getNif().equals(nif)) {
				return payer;
			}
		}
		throw new TaxException();
	}
	
	public static void submitInvoice(invoiceData invoiceData) {
		float value = invoiceData.getValue();
		LocalDate date = invoiceData.getDate();
		String itemType = invoiceData.getItemType();
		Seller seller = (Seller) getTaxPayerByNIF(invoiceData.getSellerNIF());
		Buyer buyer = (Buyer) getTaxPayerByNIF(invoiceData.getBuyerNIF());
		
		Invoice invoice = new Invoice(value, date, itemType, seller, buyer);
		
		Invoice.addInvoice(invoice);
	}
}