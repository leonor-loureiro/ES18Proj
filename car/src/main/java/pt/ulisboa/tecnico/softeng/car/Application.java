package pt.ulisboa.tecnico.softeng.car;

import org.joda.time.LocalDate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pt.ulisboa.tecnico.softeng.car.services.remote.TaxInterface;
import pt.ulisboa.tecnico.softeng.car.services.remote.dataobjects.InvoiceData;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        InvoiceData invoiceData = new InvoiceData();
        invoiceData.setBuyerNIF("123456789");
        invoiceData.setSellerNIF("123456788");
        invoiceData.setDate(LocalDate.parse("2017-12-12"));
        invoiceData.setItemType("GIT");
        invoiceData.setValue(1);
        TaxInterface.cancelInvoice(TaxInterface.submitInvoice(invoiceData));

    }
}
