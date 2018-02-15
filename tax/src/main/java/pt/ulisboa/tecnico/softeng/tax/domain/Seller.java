package pt.ulisboa.tecnico.softeng.tax.domain;

public class Seller extends TaxPayer {
	public Seller(String NIF, String name, String address) {
		super(NIF, name, address);
	}

	public float toPay(int year) {
		float result = 0;
		for (Invoice invoice : this.invoices) {
			if (invoice.getDate().getYear() == year) {
				result = result + invoice.getIva();
			}
		}
		return result;
	}

}
