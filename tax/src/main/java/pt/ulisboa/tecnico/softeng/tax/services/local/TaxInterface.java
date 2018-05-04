package pt.ulisboa.tecnico.softeng.tax.services.local;

import java.util.List;
import java.util.stream.Collectors;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.TaxPayerData;

public class TaxInterface {

	@Atomic(mode = TxMode.WRITE)
	public static void initIRS() {
		IRS.getIRSInstance();
	}
	
	@Atomic(mode = TxMode.READ)
	public static List<TaxPayerData> getTaxPayers() {
		initIRS();
		return FenixFramework.getDomainRoot().getIrs().getTaxPayerSet().stream().sorted((tp1, tp2) -> tp1.getNif().compareTo(tp2.getNif())).map(tp -> new TaxPayerData(tp)).collect(Collectors.toList());
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static void createBuyer(TaxPayerData taxPayerData) {
		new Buyer(FenixFramework.getDomainRoot().getIrs(), taxPayerData.getNif(), taxPayerData.getName(), taxPayerData.getAddress());
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static void createSeller(TaxPayerData taxPayerData) {
		new Seller(FenixFramework.getDomainRoot().getIrs(), taxPayerData.getNif(), taxPayerData.getName(), taxPayerData.getAddress());
	}
}