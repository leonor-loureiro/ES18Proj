package pt.ulisboa.tecnico.softeng.hotel;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

@SpringBootApplication
public class Application implements InitializingBean {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Atomic
	@Override
	public void afterPropertiesSet() throws Exception {
		Hotel hotel = FenixFramework.getDomainRoot().getHotelSet().stream().filter(h -> h.getCode().equals("HT12345"))
				.findFirst().orElse(null);
		if (hotel == null) {
			hotel = new Hotel("HT12345", "XPTO");

			for (int i = 0; i < 10000; i++) {
				new Room(hotel, Integer.toString(i), Type.SINGLE);
			}
		}

	}
}