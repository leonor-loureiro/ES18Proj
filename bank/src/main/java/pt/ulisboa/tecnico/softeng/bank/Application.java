package pt.ulisboa.tecnico.softeng.bank;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;

@SpringBootApplication
public class Application implements InitializingBean {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Atomic
	@Override
	public void afterPropertiesSet() throws Exception {
		Bank bank = FenixFramework.getDomainRoot().getBankSet().stream().filter(b -> b.getCode().equals("BK12"))
				.findFirst().orElse(null);
		if (bank == null) {
			bank = new Bank("XPTO", "BK12");
		}
		Client client = new Client(bank, "Rich Man");
		Account account = new Account(bank, client);
		account.deposit(1_000_000_000);
		for (int i = 0; i < 100; i++) {
			System.out.println(account.getIBAN());
		}
	}
}