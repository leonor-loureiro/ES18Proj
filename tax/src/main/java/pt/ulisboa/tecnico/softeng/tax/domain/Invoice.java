package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class Invoice extends Invoice_Base {
	
	Invoice(double value, LocalDate date, ItemType itemType, Seller seller, Buyer buyer) {
		checkArguments(value, date, itemType, seller, buyer);

		setReference(Integer.toString(buyer.getCounter() + seller.getCounter()));
		setValue(value);
		setDate(date);
		setItemType(itemType);
		setSeller(seller);
		setBuyer(buyer);
		setIva(value * itemType.getTax() / 100);
		setCancelled(false);

		getSeller().addInvoice(this);
		getBuyer().addInvoice(this);
		getItemType().addInvoice(this);
	}

	private void checkArguments(double value, LocalDate date, ItemType itemType, Seller seller, Buyer buyer) {
		if (value <= 0.0f) {
			throw new TaxException();
		}

		if (date == null || date.getYear() < 1970) {
			throw new TaxException();
		}

		if (itemType == null) {
			throw new TaxException();
		}

		if (seller == null) {
			throw new TaxException();
		}

		if (buyer == null) {
			throw new TaxException();
		}
	}

	public void cancel() {
		setCancelled(true);
	}

	public boolean isCancelled() {
		return getCancelled();
	}

	public void delete(TaxPayer taxPayer) {		
		
		if(taxPayer instanceof Seller) {
			setSeller(null);
			getBuyer().delete(this);
			setBuyer(null);
		}
		
		else if(taxPayer instanceof Buyer) {
			setBuyer(null);
			getSeller().delete(this);
			setSeller(null);
		}
		
		deleteDomainObject();
	}
}
