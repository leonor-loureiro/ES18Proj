package pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import pt.ulisboa.tecnico.softeng.tax.domain.Invoice;

public class InvoiceData {
	private String sellerNIF;
	private String buyerNIF;
	private String itemType;
	private Double value;
	@DateTimeFormat (pattern="yy-mm-dd")
	private LocalDate date;

	public InvoiceData() {
	}

	public InvoiceData(Invoice invoice) {
		this.sellerNIF = invoice.getSeller().getNif();
		this.buyerNIF = invoice.getBuyer().getNif();
		this.itemType = invoice.getItemType().getName();
		this.value = invoice.getValue();
		this.date = invoice.getDate();
	}
	
	public InvoiceData(String sellerNIF, String buyerNIF, String itemType, double value, LocalDate date) {
		this.sellerNIF = sellerNIF;
		this.buyerNIF = buyerNIF;
		this.itemType = itemType;
		this.value = new Double(value);
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

}
