package pt.ulisboa.tecnico.softeng.tax.services.local;

import java.util.List;
import java.util.stream.Collectors;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.ItemType;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.ItemTypeData;
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
	
	@Atomic(mode = TxMode.READ)
	public static List<TaxPayerData> getBuyers() {
		return FenixFramework.getDomainRoot().getIrs().getTaxPayerSet().stream().filter(tp -> tp instanceof Buyer).sorted((tp1, tp2) -> tp1.getNif().compareTo(tp2.getNif())).map(tp -> new TaxPayerData(tp)).collect(Collectors.toList());
	}
	
	@Atomic(mode = TxMode.READ)
	public static List<TaxPayerData> getSellers() {
		return FenixFramework.getDomainRoot().getIrs().getTaxPayerSet().stream().filter(tp -> tp instanceof Seller).sorted((tp1, tp2) -> tp1.getNif().compareTo(tp2.getNif())).map(tp -> new TaxPayerData(tp)).collect(Collectors.toList());
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createBuyer(TaxPayerData taxPayerData) {
		new Buyer(FenixFramework.getDomainRoot().getIrs(), taxPayerData.getNif(), taxPayerData.getName(), taxPayerData.getAddress());
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static void createSeller(TaxPayerData taxPayerData) {
		new Seller(FenixFramework.getDomainRoot().getIrs(), taxPayerData.getNif(), taxPayerData.getName(), taxPayerData.getAddress());
	}
	
	@Atomic(mode = TxMode.READ)
	public static List<ItemTypeData> getItemTypes() {
		initIRS();
		return FenixFramework.getDomainRoot().getIrs().getItemTypeSet().stream().sorted((it1, it2) -> it1.getName().compareTo(it2.getName())).map(it -> new ItemTypeData(it)).collect(Collectors.toList());
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static void createItemType(ItemTypeData itemTypeData) {
		new ItemType(FenixFramework.getDomainRoot().getIrs(), itemTypeData.getName(), itemTypeData.getTax());
	}
}