package pt.ulisboa.tecnico.softeng.activity;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;

@SpringBootApplication
public class Application implements InitializingBean {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Atomic
	@Override
	public void afterPropertiesSet() throws Exception {
		ActivityProvider provider = FenixFramework.getDomainRoot().getActivityProviderSet().stream()
				.filter(b -> b.getCode().equals("AP1234")).findFirst().orElse(null);
		if (provider == null) {
			provider = new ActivityProvider("AP1234", "XPTO");
		}
		Activity activity = new Activity(provider, "CCCC", 18, 99, 20000);
		LocalDate begin = new LocalDate(2017, 5, 1);
		LocalDate end = new LocalDate(2017, 5, 2);
		new ActivityOffer(activity, begin, begin);
		new ActivityOffer(activity, begin, end);
	}
}