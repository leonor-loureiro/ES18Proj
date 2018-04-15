package pt.ulisboa.tecnico.softeng.broker.services.remote;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects.RentingData;

public class CarInterface {
	public static enum Type {
		CAR, MOTORCYCLE
	}

	public static String rentCar(Type vehicleType, String drivingLicense, String nif, String iban, LocalDate begin,
			LocalDate end) {
		// return RentACar.rent(vehicleType, drivingLicense, nif, iban, begin, end);
		// TODO: implement in the final version as a rest invocation
		return null;
	}

	public static String cancelRenting(String rentingReference) {
		// return RentACar.cancelRenting(rentingReference);
		// TODO: implement in the final version as a rest invocation
		return null;
	}

	public static RentingData getRentingData(String reference) {
		// return RentACar.getRentingData(reference);
		// TODO: implement in the final version as a rest invocation
		return null;
	}

}
