package pt.ulisboa.tecnico.softeng.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import pt.ulisboa.tecnico.softeng.bank.services.local.BankInterface;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}