package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;


public class Invoice {
	private static int counter = 0;
	
	public static Set<Invoice> invoices = new HashSet<>();
	
	private String reference;
	private float value;
	private float IVA;
	private LocalDate date;
	
	public Invoice(float value, LocalDate date, String itemType, Seller seller, Buyer buyer) {
		this.reference = seller + buyer + Integer.toString(++Invoice.counter);
	}
	
	
}