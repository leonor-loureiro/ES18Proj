package pt.ulisboa.tecnico.softeng.tax.domain;

public class TaxPayer {
	public static Set<TaxPayer> taxPayers = new HashSet<>();
	
	private String nif;
	private String name;
	private String address;
	
	public TaxPayer(String nif, String name, String address) {
		
	}
}