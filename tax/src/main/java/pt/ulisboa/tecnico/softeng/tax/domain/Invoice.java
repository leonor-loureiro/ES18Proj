package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.tax.exception.IvaException;

public class Invoice {
	private static int counter = 0;

	private final String reference;
	private final float value;
	private float iva;
	private final LocalDate date;
	private final ItemType itemType;
	private final Seller seller;
	private final Buyer buyer;

	Invoice(float value, LocalDate date, ItemType itemType, Seller seller, Buyer buyer) {
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

	private void checkArguments(float value, LocalDate date, ItemType itemType, Seller seller, Buyer buyer) {
		if (value <= 0.0f) {
			throw new IvaException();
		}

		if (date == null) {
			throw new IvaException();
		}

		if (itemType == null) {
			throw new IvaException();
		}

		if (seller == null) {
			throw new IvaException();
		}

		if (buyer == null) {
			throw new IvaException();
		}
	}

	public String getReference() {
		return this.reference;
	}

	public float getIva() {
		return this.iva;
	}

	public void setIva(float iva) {
		this.iva = iva;
	}

	public float getValue() {
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
}
