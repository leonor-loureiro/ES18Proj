package pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects;

import java.util.List;
import java.util.stream.Collectors;

import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;
import pt.ulisboa.tecnico.softeng.tax.domain.TaxPayer;

public class TaxPayerData {
	private String nif;
	private String name;
	private String address;
	private IRS irs;
	private List<InvoiceData> invoices;

	public TaxPayerData() {
	}
	
	public TaxPayerData(TaxPayer taxPayer) {
		this.nif = taxPayer.getNif();
		this.name = taxPayer.getName();
		this.address = taxPayer.getAddress();
		this.irs = taxPayer.getIrs();
		
		if(taxPayer instanceof Buyer) {
			Buyer buyer = (Buyer) taxPayer;
			this.invoices = buyer.getInvoiceSet().stream().sorted((i1, i2) -> i1.getReference().compareTo(i2.getReference()))
					.map(i -> new InvoiceData(i)).collect(Collectors.toList());
		}
		else {
			Seller seller = (Seller) taxPayer;
			this.invoices = seller.getInvoiceSet().stream().sorted((i1, i2) -> i1.getReference().compareTo(i2.getReference()))
					.map(i -> new InvoiceData(i)).collect(Collectors.toList());
		}
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public IRS getIrs() {
		return irs;
	}

	public void setIrs(IRS irs) {
		this.irs = irs;
	}
	
	public List<InvoiceData> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<InvoiceData> invoices) {
		this.invoices = invoices;
	}	
}