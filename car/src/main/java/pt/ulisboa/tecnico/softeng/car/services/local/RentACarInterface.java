package pt.ulisboa.tecnico.softeng.car.services.local;

import org.joda.time.LocalDate;
import org.thymeleaf.util.ListUtils;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.car.domain.*;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentACarData;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.VehicleData;
import pt.ulisboa.tecnico.softeng.car.services.remote.TaxInterface;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RentACarInterface {

    public static enum Type{
        CAR, MOTORCYCLE
    }

    @Atomic(mode = TxMode.WRITE)
    public static void createRentACar(RentACarData rentacar) {
        new RentACar(rentacar.getName(), rentacar.getNif(), rentacar.getIban());
    }

    @Atomic(mode = TxMode.READ)
    public static List<RentACarData> listRentACars() {
        return FenixFramework.getDomainRoot().getRentACarSet().stream()
                .sorted(Comparator.comparing(RentACar_Base::getName))
                .map(RentACarData::new).collect(Collectors.toList());
    }

    @Atomic(mode = TxMode.READ)
    public static RentACarData getRentACarDataByCode(String code) {
        RentACar rac = getRentACarByCode(code);
        if (rac == null)
            return null;
        return new RentACarData(rac);
    }

    @Atomic(mode = TxMode.WRITE)
    public static void createVehicle(String code, VehicleData vd) {
        RentACar rac = getRentACarByCode(code);
        if (rac == null || vd.getIsCar() == null)
            throw new CarException();

        switch(vd.getIsCar()) {
            case "Motorcycle":
                new Motorcycle(vd.getPlate(), vd.getKilometers(), vd.getPrice(), rac);
                break;
            case "Car":
                new Car(vd.getPlate(), vd.getKilometers(), vd.getPrice(), rac);
                break;
            default:
                throw new CarException();
        }
    }

    private static RentACar getRentACarByCode(String code) {
        return FenixFramework.getDomainRoot().getRentACarSet().stream()
                .filter(a -> a.getCode().equals(code))
                .findFirst().orElse(null);
    }


    @Atomic(mode = TxMode.READ)
    public static VehicleData getVehicleDataByPlate(String code, String plate) {
        RentACar rac = getRentACarByCode(code);
        if (rac == null)
            return null;

        Vehicle v = rac.getVehicleByPlate(plate);
        if (v == null)
            return null;

        return new VehicleData(v);
    }

    @Atomic(mode = TxMode.READ)
    public static Set<RentingData> getRentingsByPlate(String rentACarCode, String plate){
        return getRentACarByCode(rentACarCode).getVehicleByPlate(plate).getRentingSet().stream().
                map(RentingData::new).collect(Collectors.toSet());
    }

    @Atomic(mode = TxMode.WRITE)
    public static RentingData rentVehicle(String rentACarCode, String plate, String drivingLicense,
                                                      LocalDate begin, LocalDate end, String nif, String iban){
        return new RentingData (getRentACarByCode(rentACarCode).getVehicleByPlate(plate).rent(drivingLicense, begin, end, nif, iban));
    }

    @Atomic(mode = TxMode.WRITE)
    public static void checkOut(String rentACarCode, String plate, String reference, int kilometers) {
        getRentACarByCode(rentACarCode).getVehicleByPlate(plate).getRenting(reference).checkout(kilometers);
    }

    @Atomic(mode = TxMode.WRITE)
    public static RentingData rentVehicle(RentACarInterface.Type type, String license, LocalDate begin, LocalDate end, String nif, String iban) {
        for(RentACar rentACar : FenixFramework.getDomainRoot().getRentACarSet()){
            if (type.equals(Type.CAR)){
                for(Vehicle v : rentACar.getAvailableVehicles(Car.class, begin, end)){
                    return new RentingData(v.rent(license, begin, end, nif, iban));
                }
            }else{
                for(Vehicle v : rentACar.getAvailableVehicles(Motorcycle.class, begin, end)){
                    return new RentingData(v.rent(license, begin, end, nif, iban));
                }
            }
        }
        return null;
    }

    @Atomic(mode = TxMode.WRITE)
    public static String cancelRenting(String reference) {
        return RentACar.cancelRenting(reference);
    }
    @Atomic(mode = TxMode.READ)
    public static RentingData getRentingByReference(String reference) {
        return RentACar.getRentingData(reference);
    }

}
