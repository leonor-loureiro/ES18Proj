package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class Invoice {
	private static int counter = 0;

	private final String reference;
	private final double value;
	private final double iva;
	private final LocalDate date;
	private final ItemType itemType;
	private final Seller seller;
	private final Buyer buyer;
	private boolean cancelled = false;

	Invoice(double value, LocalDate date, ItemType itemType, Seller seller, Buyer buyer) {
		checkArguments(value, date, itemType, seller, buyer);

		this.reference = Integer.toString(++Invoice.counter);
		this.value = value;
		this.date = date;
		this.itemType = itemType;
		this.seller = seller;
		this.buyer = buyer;
		this.iva = value * itemType.getTax() / 100;

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

	public String getReference() {
		return this.reference;
	}

	public double getIva() {
		return this.iva;
	}

	public double getValue() {
		return this.value;
	}

	public LocalDate getDate() {
		return this.date;
	}

	public ItemType getItemType() {
		return this.itemType;
	}

	public Seller getSeller() {
		return this.seller;
	}

	public Buyer getBuyer() {
		return this.buyer;
	}

	public void cancel() {
		this.cancelled = true;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

}
