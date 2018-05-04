package pt.ulisboa.tecnico.softeng.activity.services.remote.dataobjects;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public class InvoiceData {
	private String sellerNif;
	private String buyerNif;
	private String itemType;
	private Double value;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	private Double iva;
	private DateTime time;

	public InvoiceData() {
	}

	public InvoiceData(String sellerNif, String buyerNif, String itemType, Double value, LocalDate date,
			DateTime time) {
		this.sellerNif = sellerNif;
		this.buyerNif = buyerNif;
		this.itemType = itemType;
		this.value = value;
		this.date = date;
		this.setTime(time);
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

	public DateTime getTime() {
		return this.time;
	}

	public void setTime(DateTime time) {
		this.time = time;
	}

}
