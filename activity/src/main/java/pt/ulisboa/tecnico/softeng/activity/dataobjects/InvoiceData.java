package pt.ulisboa.tecnico.softeng.activity.dataobjects;

import org.joda.time.LocalDate;

public class InvoiceData {
	private String sellerNIF;
	private String buyerNIF;
	private String itemType;
	private float value;
	private LocalDate date;

	public InvoiceData() {
	}

	public InvoiceData(String sellerNIF, String buyerNIF, String itemType, float value, LocalDate date) {
		this.sellerNIF = sellerNIF;
		this.buyerNIF = buyerNIF;
		this.itemType = itemType;
		this.value = value;
		this.date = date;
	}

	public String getSellerNIF() {
		return this.sellerNIF;
	}

	public void setSellerNIF(String sellerNIF) {
		this.sellerNIF = sellerNIF;
	}

	public String getBuyerNIF() {
		return this.buyerNIF;
	}

	public void setBuyerNIF(String buyerNIF) {
		this.buyerNIF = buyerNIF;
	}

	public String getItemType() {
		return this.itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public float getValue() {
		return this.value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public LocalDate getDate() {
		return this.date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

}
