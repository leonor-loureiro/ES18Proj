package pt.ulisboa.tecnico.softeng.car.services.local;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.car.domain.Car;
import pt.ulisboa.tecnico.softeng.car.domain.Motorcycle;
import pt.ulisboa.tecnico.softeng.car.domain.RentACar;
import pt.ulisboa.tecnico.softeng.car.domain.RentACar_Base;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentACarData;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.VehicleData;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RentACarInterface {
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


}
