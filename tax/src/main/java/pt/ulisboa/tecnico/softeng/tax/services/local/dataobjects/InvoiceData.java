package pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import pt.ulisboa.tecnico.softeng.tax.domain.Invoice;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class InvoiceData {
	private String externalId;
	private String reference;
	private String sellerNif;
	private String buyerNif;
	private String itemType;
	private Double value;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	private Double iva;

	public InvoiceData() {
	}

	public InvoiceData(String reference, String sellerNif, String buyerNif, String itemType, Double value, LocalDate date) {
	    if (reference == null) {
	        throw new TaxException();
        }
	    this.externalId = reference;
	    this.reference = reference;
		this.sellerNif = sellerNif;
		this.buyerNif = buyerNif;
		this.itemType = itemType;
		this.value = value;
		this.date = date;
	}

	public InvoiceData(Invoice invoice) {
	    this.externalId = invoice.getExternalId();
		this.reference = invoice.getReference();
		this.sellerNif = invoice.getSeller().getNif();
		this.buyerNif = invoice.getBuyer().getNif();
		this.itemType = invoice.getItemType().getName();
		this.value = invoice.getValue();
		this.date = invoice.getDate();
		this.iva = invoice.getIva();
	}

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
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
