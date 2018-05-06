package pt.ulisboa.tecnico.softeng.hotel.services.remote;

import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import pt.ulisboa.tecnico.softeng.hotel.services.remote.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.hotel.services.remote.exceptions.RemoteAccessException;

import java.util.Map;
import java.util.TreeMap;

public class TaxInterface {
	public static String submitInvoice(InvoiceData invoiceData) {
		try{

			String url = "http://localhost:8086/taxpayersControl/submitInvoice";
			RestTemplate rest = new RestTemplate();

			return rest.postForObject(url, invoiceData, String.class);
		}catch(ResourceAccessException e){
			throw new RemoteAccessException(e.getMessage());
		}
	}

	public static void cancelInvoice(String reference) {

		try {
			String url = "http://localhost:8086/taxpayersControl/cancelInvoice/{reference}";

			RestTemplate rest = new RestTemplate();

			Map<String, Object> map = new TreeMap<>();
			map.put("reference", reference);

			rest.getForObject(url, String.class, map);
		}
		catch (ResourceAccessException e){
			throw new RemoteAccessException(e.getMessage());
		}
	}
}
