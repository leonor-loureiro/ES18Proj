package pt.ulisboa.tecnico.softeng.hotel.services.remote.dataobjects;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class RestInvoiceData {
	private String sellerNif;
	private String buyerNif;
	private String itemType;
	private Double value;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate date;
	private Double iva;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private DateTime time;

	public RestInvoiceData() {
	}

	public RestInvoiceData(String sellerNif, String buyerNif, String itemType, Double value, LocalDate date,
			DateTime time) {
		this.sellerNif = sellerNif;
		this.buyerNif = buyerNif;
		this.itemType = itemType;
		this.value = value;
		this.date = date;
		this.time = time;
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
