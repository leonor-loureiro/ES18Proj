package pt.ulisboa.tecnico.softeng.broker.interfaces;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.domain.RentACar;
import pt.ulisboa.tecnico.softeng.car.domain.Renting;

public class CarInterface {
	public static String rentVehicle(Class<?> cls, String driving_licence, LocalDate begin, LocalDate end, String clientNIF, String clientIBAN) {
		return RentACar.rentVehicle(cls, driving_licence, begin, end, clientNIF, clientIBAN);
	}

	public static String cancelRenting(String reference) {
		return RentACar.cancelRenting(reference);
	}
	
	public static Renting getRenting(String reference) {
		return RentACar.getRenting(reference);
	}
	
	public static RentingData getRentingData(String reference) {
		return RentACar.getRentingData(reference);
	}
}
