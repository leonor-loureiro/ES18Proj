package pt.ulisboa.tecnico.softeng.tax.domain;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class Seller extends Seller_Base {


    public Seller(IRS irs, String NIF, String name, String address) {
        checkArguments(irs, NIF, name, address);

        setIrs(irs);
        setNif(NIF);
        setName(name);
        setAddress(address);
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

	public double toPay(int year) {
		if (year < 1970) {
			throw new TaxException();
		}

		double result = 0;
		for (Invoice invoice : getInvoiceSet()) {
			if (!invoice.isCancelled() && invoice.getDate().getYear() == year) {
				result = result + invoice.getIva();
			}
		}
		return result;
	}

    public Invoice getInvoiceByReference(String invoiceReference) {
        if (invoiceReference == null || invoiceReference.isEmpty()) {
            throw new TaxException();
        }

        for (Invoice invoice : getInvoiceSet() ) {
            if (invoice.getReference().equals(invoiceReference)) {
                return invoice;
            }
        }
        return null;
    }
}
