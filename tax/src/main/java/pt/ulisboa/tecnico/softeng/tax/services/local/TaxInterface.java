package pt.ulisboa.tecnico.softeng.tax.services.local;

import java.util.List;
import java.util.stream.Collectors;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.ItemType;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.ItemTypeData;

public class TaxInterface {

	@Atomic(mode = TxMode.READ)
	public static List<ItemTypeData> getItemTypes() {
		return IRS.getIRSInstance().getItemTypeSet().stream().map(i -> new ItemTypeData(i))
				.sorted((i1, i2) -> i1.getName().compareTo(i2.getName())).collect(Collectors.toList());
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createItemType(ItemTypeData itemTypeData) {
		new ItemType(IRS.getIRSInstance(), itemTypeData.getName(),
				itemTypeData.getTax() != null ? itemTypeData.getTax() : -1);
	}

}
