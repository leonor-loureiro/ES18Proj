package pt.ulisboa.tecnico.softeng.tax.domain;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public abstract class TaxPayer extends TaxPayer_Base {
	
	public TaxPayer(IRS irs, String NIF, String name, String address) {
		init(irs, NIF, name, address);
	}
	
	protected TaxPayer() {
	}
	
	public void init(IRS irs, String NIF, String name, String address) {
		checkArguments(irs, NIF, name, address);

		setIrs(irs);
		setNIF(NIF);
		setName(name);
		setAddress(address);
		setCounter(Integer.parseInt(getNIF()));
	}
	
	@Override
	public int getCounter() {
		int counter = super.getCounter() + 1;
		setCounter(counter);
		return counter;
	}
	
	private void checkArguments(IRS irs, String NIF, String name, String address) {
		if (NIF == null || NIF.length() != 9) {
			throw new TaxException();
		}

		if (name == null || name.length() == 0) {
			throw new TaxException();
		}

		if (address == null || address.length() == 0) {
			throw new TaxException();
		}

		if (irs.getTaxPayerByNIF(NIF) != null) {
			throw new TaxException();
		}

	}

	public Invoice getInvoiceByReference(String invoiceReference) {
		if (invoiceReference == null || invoiceReference.isEmpty()) {
			throw new TaxException();
		}
		
		for(TaxPayer taxPayer : FenixFramework.getDomainRoot().getIrs().getTaxPayerSet()) {
			if(taxPayer instanceof Buyer) {
				Buyer buyer = (Buyer) taxPayer;
				for(Invoice invoice : buyer.getInvoiceSet()) {
					if (invoice.getReference().equals(invoiceReference)) {
						return invoice;
					}
				}
			}
			else {
				Seller seller = (Seller) taxPayer;
				for(Invoice invoice : seller.getInvoiceSet()) {
					if (invoice.getReference().equals(invoiceReference)) {
						return invoice;
					}
				}
			}
		}
		return null;
	}

	public abstract void delete(Invoice invoice);
}