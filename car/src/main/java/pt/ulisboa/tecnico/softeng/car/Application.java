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
    }
}
