package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class Invoice extends Invoice_Base {

	private final Buyer buyer;

	Invoice(double value, LocalDate date, ItemType itemType, Seller seller, Buyer buyer) {
		checkArguments(value, date, itemType, seller, buyer);

		setCounter(getCounter() + 1);
		setReference(Integer.toString(getCounter()));
		setValue(value);
		setDate(date);
		setItemType(itemType);
		setSeller(seller);
		this.buyer = buyer;
		setIva(value * itemType.getTax() / 100);
		setCancelled(false);

		seller.addInvoice(this);
		buyer.addInvoice(this);
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



	public Buyer getBuyer() {
		return this.buyer;
	}

	public void cancel() {
		setCancelled(true);
	}

	public boolean isCancelled() {
		return getCancelled();
	}

	public void delete() {

		getItemType().delete();
		getSeller().delete();

		deleteDomainObject();
	}
}
