package pt.ulisboa.tecnico.softeng.tax.dataobjects;

import org.joda.time.LocalDate;


public class invoiceData {
	private String sellerNIF;
	private String buyerNIF;
	private String itemType;
	private float value;
	private LocalDate date;

	public invoiceData(String sellerNIF, String buyerNIF, String itemType, float value, LocalDate date) {
		
		this.sellerNIF = sellerNIF;
		this.buyerNIF = buyerNIF;
		this.itemType = itemType;
		this.value = value;
		this.date = date;
		
	}

	public getSellerNIF() {
		return this.sellerNIF;
	}
	
	public setSellerNIF(String sellerNIF) {
		this.sellerNIF = sellerNIF;
	}
	
	public getBuyerNIF() {
		return this.buyerNIF;
	}
	
	public setBuyerNIF(String buyerNIF) {
		this.buyerNIF = buyerNIF;
	}
	
	public getItemType() {
		return this.itemType;
	}
	
	public setItemType(String itemType) {
		this.itemType = itemType;
	}
	
	public getValue() {
		return this.value;
	}
	
	public setValue(float value) {
		this.value = value;
	}
	
	public getDate() {
		return this.date;
	}
	
	public setDate(LocalData date) {
		this.date = date;
	}

}
