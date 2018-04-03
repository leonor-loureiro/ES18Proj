package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class CarPersistenceTest {
    private static final String NAME1 = "eartz";
    private static final String PLATE_CAR1 = "aa-00-11";
    private static final String DRIVING_LICENSE = "br123";
    private static final LocalDate date1 = LocalDate.parse("2018-01-06");
    private static final LocalDate date2 = LocalDate.parse("2018-01-07");
    private static final LocalDate date3 = LocalDate.parse("2018-01-08");
    private static final LocalDate date4 = LocalDate.parse("2018-01-09");
    private static final String NIF = "NIF";
    private static final String IBAN = "IBAN";
    private static final String IBAN_BUYER = "IBAN";

    @Test
    public void success() {
        atomicProcess();
        atomicAssert();
    }

    @Atomic(mode = TxMode.WRITE)
    public void atomicProcess() {
        RentACar rentACar = new RentACar(NAME1, NIF, IBAN);
        Car car = new Car(PLATE_CAR1, 10, 10, rentACar);
        car.rent(DRIVING_LICENSE, date1, date2, NIF, IBAN_BUYER);
    }

    @Atomic(mode = TxMode.READ)
    public void atomicAssert() {
        assertEquals(1, FenixFramework.getDomainRoot().getRentACarSet().size());

        RentACar rentACar = new ArrayList<>(FenixFramework.getDomainRoot().getRentACarSet()).get(0);
        assertEquals(1, rentACar.getVehicleSet().size());

        Vehicle car = new ArrayList<>(rentACar.getVehicleSet()).get(0);
        assertEquals(PLATE_CAR1, car.getPlate());
        assertEquals(10, car.getKilometers());
        assertEquals(10, car.getPrice(), 0);

        assertEquals(1, car.getRentingSet().size());
        Renting renting = new ArrayList<>(car.getRentingSet()).get(0);
        assertEquals(DRIVING_LICENSE, renting.getDrivingLicense());
        assertEquals(date1, renting.getBegin());
        assertEquals(date2, renting.getEnd());
        assertEquals(NIF, renting.getClientNIF());
        assertEquals(IBAN, renting.getClientIBAN());
    }

    @After
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void tearDown() {
        for (RentACar rentACar: FenixFramework.getDomainRoot().getRentACarSet()) {
            rentACar.delete();
        }
    }

}
