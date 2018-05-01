package pt.ulisboa.tecnico.softeng.tax.services.local;

import java.util.List;
import java.util.stream.Collectors;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.Invoice;
import pt.ulisboa.tecnico.softeng.tax.domain.ItemType;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;
import pt.ulisboa.tecnico.softeng.tax.domain.TaxPayer;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.ItemTypeData;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.TaxPayerData;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.TaxPayerData.Type;

public class TaxInterface {

	@Atomic(mode = TxMode.READ)
	public static List<ItemTypeData> getItemTypeDataList() {
		return IRS.getIRSInstance().getItemTypeSet().stream().map(i -> new ItemTypeData(i))
				.sorted((i1, i2) -> i1.getName().compareTo(i2.getName())).collect(Collectors.toList());
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createItemType(ItemTypeData itemTypeData) {
		new ItemType(IRS.getIRSInstance(), itemTypeData.getName(),
				itemTypeData.getTax() != null ? itemTypeData.getTax() : -1);
	}

	@Atomic(mode = TxMode.READ)
	public static List<TaxPayerData> getTaxPayerDataList() {
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

	@Atomic(mode = TxMode.WRITE)
	public static TaxPayerData getTaxPayerDataByNif(String nif) {
		TaxPayer taxPayer = IRS.getIRSInstance().getTaxPayerByNIF(nif);
		return new TaxPayerData(taxPayer);
	}

	@Atomic(mode = TxMode.READ)
	public static List<InvoiceData> getInvoiceDataList(String nif) {
		TaxPayer taxPayer = IRS.getIRSInstance().getTaxPayerByNIF(nif);
		if (taxPayer == null) {
			throw new TaxException();
		}

		if (taxPayer instanceof Buyer) {
			return ((Buyer) taxPayer).getInvoiceSet().stream().map(i -> new InvoiceData(i))
					.sorted((i1, i2) -> i1.getSellerNif().compareTo(i2.getSellerNif())).collect(Collectors.toList());
		} else {
			return ((Seller) taxPayer).getInvoiceSet().stream().map(i -> new InvoiceData(i))
					.sorted((i1, i2) -> i1.getBuyerNif().compareTo(i2.getBuyerNif())).collect(Collectors.toList());
		}
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createInvoice(String nif, InvoiceData invoiceData) {
		if (invoiceData.getValue() == null || invoiceData.getItemType() == null || invoiceData.getDate() == null
				|| invoiceData.getBuyerNif() == null && invoiceData.getSellerNif() == null) {
			throw new TaxException();
		}

		TaxPayer taxPayer = IRS.getIRSInstance().getTaxPayerByNIF(nif);
		ItemType itemType = IRS.getIRSInstance().getItemTypeByName(invoiceData.getItemType());

		Seller seller;
		Buyer buyer;
		if (invoiceData.getSellerNif() != null) {
			seller = (Seller) IRS.getIRSInstance().getTaxPayerByNIF(invoiceData.getSellerNif());
			buyer = (Buyer) taxPayer;
		} else {
			seller = (Seller) taxPayer;
			buyer = (Buyer) IRS.getIRSInstance().getTaxPayerByNIF(invoiceData.getBuyerNif());
		}

		new Invoice(invoiceData.getValue(), invoiceData.getDate(), itemType, seller, buyer);
	}

	@Atomic(mode = TxMode.WRITE)
	public static String submitInvoice(InvoiceData invoiceData) {
		Seller seller = (Seller) IRS.getIRSInstance().getTaxPayerByNIF(invoiceData.getSellerNif());
		Buyer buyer = (Buyer) IRS.getIRSInstance().getTaxPayerByNIF(invoiceData.getBuyerNif());
		ItemType itemType = IRS.getIRSInstance().getItemTypeByName(invoiceData.getItemType());

		Invoice invoice = new Invoice(invoiceData.getValue(), invoiceData.getDate(), itemType, seller, buyer);

		return invoice.getReference();
	}

	@Atomic(mode = TxMode.WRITE)
	public static void cancelInvoice(String reference) {
		Invoice invoice = IRS.getIRSInstance().getInvoiceSet().stream().filter(i -> i.getReference().equals(reference))
				.findFirst().orElseThrow(() -> new TaxException());
		invoice.cancel();
	}

    @Atomic(mode = TxMode.WRITE)
    public static void deleteIRS() {
        FenixFramework.getDomainRoot().getIrs().delete();
    }
}
