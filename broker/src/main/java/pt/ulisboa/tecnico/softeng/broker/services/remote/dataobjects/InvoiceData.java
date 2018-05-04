package pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public class InvoiceData {
    private String id;
	private String sellerNif;
	private String buyerNif;
	private String itemType;
	private Double value;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	private Double iva;

	public InvoiceData() {
	}

	public InvoiceData(String id, String sellerNif, String buyerNif, String itemType, Double value, LocalDate date) {
		this.id = id;
	    this.sellerNif = sellerNif;
		this.buyerNif = buyerNif;
		this.itemType = itemType;
		this.value = value;
		this.date = date;
	}

	public String getSellerNif() {
		return this.sellerNif;
	}

	public void setSellerNif(String sellerNif) {
		this.sellerNif = sellerNif;
	}

	public String getBuyerNif() {
		return this.buyerNif;
	}

	public void setBuyerNif(String buyerNif) {
		this.buyerNif = buyerNif;
	}

	public String getItemType() {
		return this.itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public Double getValue() {
		return this.value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public LocalDate getDate() {
		return this.date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Double getIva() {
		return this.iva;
	}

	public void setIva(Double iva) {
		this.iva = iva;
	}

}
