package pt.ulisboa.tecnico.softeng.tax.domain;

public class Buyer extends TaxPayer {
	private final static int PERCENTAGE = 5;

	public Buyer(String NIF, String name, String address) {
		super(NIF, name, address);
	}

	public float taxReturn(int year) {
		float result = 0;
		for (Invoice invoice : this.invoices) {
			if (invoice.getDate().getYear() == year) {
				result = result + invoice.getIva() * PERCENTAGE / 100;
			}
		}
		return result;
	}
}
