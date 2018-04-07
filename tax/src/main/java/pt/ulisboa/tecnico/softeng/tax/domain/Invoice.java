package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class Invoice extends Invoice_Base {

	Invoice(double value, LocalDate date, ItemType itemType, Seller seller, Buyer buyer) {
		checkArguments(value, date, itemType, seller, buyer);

		setReference(Integer.toString(seller.getIrs().getCounter()));
		setValue(value);
		setDate(date);
		setCancelled(false);
		setItemType(itemType);
		setSeller(seller);
		setBuyer(buyer);

		setIva(value * itemType.getTax() / 100);

		getSeller().addInvoice(this);
		getBuyer().addInvoice(this);

		setIrs(getSeller().getIrs());
	}

	public void delete() {
		setIrs(null);
		setSeller(null);
		setBuyer(null);
		setItemType(null);
		deleteDomainObject();
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

}
