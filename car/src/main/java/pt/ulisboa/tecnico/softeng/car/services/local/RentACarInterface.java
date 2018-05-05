package pt.ulisboa.tecnico.softeng.car.services.local;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.car.domain.RentACar;
import pt.ulisboa.tecnico.softeng.car.domain.RentACar_Base;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentACarData;

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
}
