package pt.ulisboa.tecnico.softeng.broker.services.remote;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.web.client.RestTemplate;

import pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects.InvoiceData;

public class TaxInterface {
	public static String submitInvoice(InvoiceData invoiceData) {
		String url = "http://localhost:8086/taxpayersControl/submitInvoice";
		RestTemplate rest = new RestTemplate();

		return rest.postForObject(url, invoiceData, String.class);
	}

	public static void cancelInvoice(String invoiceReference) {
		String url = "http://localhost:8086/taxpayersControl/cancelInvoice/{reference}";

		RestTemplate rest = new RestTemplate();

		Map<String, Object> map = new TreeMap<>();
		map.put("reference", invoiceReference);

		rest.getForObject(url, String.class, map);
	}
}
