package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;

public class InVoice {
		
	private String reference;
	private float value;
	private float IVA;
	private LocalDate date;
	
	public InVoice(float value, LocalDate date, String itemType, Seller seller, Buyer buyer) {
		
	}
	
	
}