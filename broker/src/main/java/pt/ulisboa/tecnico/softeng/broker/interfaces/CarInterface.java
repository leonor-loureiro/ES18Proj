package pt.ulisboa.tecnico.softeng.broker.interfaces;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.car.domain.RentACar;
import pt.ulisboa.tecnico.softeng.car.domain.Vehicle;

public class CarInterface {
    public static String rentCar(Class<? extends Vehicle> vehicleType,
                                 String drivingLicense, String nif, LocalDate begin, LocalDate end) {
        return RentACar.rent(vehicleType, drivingLicense, nif, begin, end);
    }

    public static String cancelRenting(String rentingReference) {
        return RentACar.cancelRenting(rentingReference);
    }
}
