package pt.ulisboa.tecnico.softeng.car.services.local;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ulisboa.tecnico.softeng.car.domain.RentACar;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentACarData;

public class RentACarInterface {
    @Atomic(mode = TxMode.WRITE)
    public static void createRentACar(RentACarData rentacar) {
        new RentACar(rentacar.getName(), rentacar.getNif(), rentacar.getIban());
    }
}
