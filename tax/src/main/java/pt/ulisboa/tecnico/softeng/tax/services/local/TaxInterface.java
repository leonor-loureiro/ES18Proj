package pt.ulisboa.tecnico.softeng.tax.services.local;

import java.util.List;
import java.util.stream.Collectors;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.ItemType;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.ItemTypeData;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.TaxPayerData;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.TaxPayerData.Type;

public class TaxInterface {

	@Atomic(mode = TxMode.READ)
	public static List<ItemTypeData> getItemTypeListData() {
		return IRS.getIRSInstance().getItemTypeSet().stream().map(i -> new ItemTypeData(i))
				.sorted((i1, i2) -> i1.getName().compareTo(i2.getName())).collect(Collectors.toList());
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createItemType(ItemTypeData itemTypeData) {
		new ItemType(IRS.getIRSInstance(), itemTypeData.getName(),
				itemTypeData.getTax() != null ? itemTypeData.getTax() : -1);
	}

	@Atomic(mode = TxMode.READ)
	public static List<TaxPayerData> getTaxPayerListData() {
		return IRS.getIRSInstance().getTaxPayerSet().stream().map(i -> new TaxPayerData(i))
				.sorted((i1, i2) -> i1.getNif().compareTo(i2.getNif())).collect(Collectors.toList());
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createTaxPayer(TaxPayerData taxPayerData) {
		if (taxPayerData.getType().equals(Type.BUYER)) {
			new Buyer(IRS.getIRSInstance(), taxPayerData.getNif(), taxPayerData.getName(), taxPayerData.getAddress());
		} else {
			new Seller(IRS.getIRSInstance(), taxPayerData.getNif(), taxPayerData.getName(), taxPayerData.getAddress());
		}
	}

}
