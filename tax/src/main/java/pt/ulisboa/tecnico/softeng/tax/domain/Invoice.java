package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashSet;
import java.util.Set;
import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class Invoice {
	private static int counter = 0;
	
	public static Set<Invoice> invoices = new HashSet<>();
	
	private String reference;
	private float value;
	private float IVA;
	private LocalDate date;
	private String itemType;
	private Seller seller;
	private Buyer buyer;
	
	public Invoice(float value, LocalDate date, String itemType, Seller seller, Buyer buyer) {
		checkArguments(value, date, itemType, seller, buyer);		
		
		this.itemType = itemType;
		this.seller = seller;
		this.buyer = buyer;	
		this.reference = seller.getNif() + buyer.getNif() + Integer.toString(++Invoice.counter);
		this.IVA = getIVA();
		this.value = value + value * (this.IVA/100);
		this.date = date;
			
		
		Invoice.invoices.add(this);
	}
	
	private void checkArguments(float value, LocalDate date, String itemType, Seller seller, Buyer buyer) {
		if (itemType == null || itemType.trim().length() == 0 || value < 0 || date == null || seller == null || buyer == null) {
			throw new TaxException();
		}

		if (date.getYear() < 1970) {
			throw new TaxException();
		}

		for (Invoice invoice : invoices) {
			if (invoice.getReference().equals(this.reference)) {
				throw new TaxException();
			}
		}
	}
	
	public float getValue() {
		return this.value;
	}
	
	public LocalDate getDate() {
		return this.date;
	}
	
	public String getItemType() {
		return this.itemType;
	}
	
	public float getIVA() {
		int iva = ItemType.findTaxByType(this.itemType);
		return iva;
	}
	
	public Seller getSeller() {
		return this.seller;
	}
	
	public Buyer getBuyer() {
		return this.buyer;
	}
	
	public String getReference() {
		return this.reference;
	}
	
	public static void addInvoice(Invoice invoice) {
		Invoice.invoices.add(invoice);
	}
	
	public static Set<Invoice> getInvoiceByYear(int YEAR) {
		Set<Invoice> temp = new HashSet<>();;
		
		for (Invoice invoice : invoices) {
			if (invoice.getDate().getYear() == YEAR) {
				temp.add(invoice);
			}
		}		
		return temp;
	}
	
	public static Set<Invoice> getInvoiceBySeller(Seller seller) {
		Set<Invoice> temp = new HashSet<>();;
		
		for (Invoice invoice : invoices) {
			if (invoice.getSeller() == seller) {
				temp.add(invoice);
			}
		}		
		return temp;
	}
	
	public static Set<Invoice> getInvoiceByBuyer(Buyer buyer) {
		Set<Invoice> temp = new HashSet<>();;
		
		for (Invoice invoice : invoices) {
			if (invoice.getBuyer() == buyer) {
				temp.add(invoice);
			}
		}		
		return temp;
	}
}