package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class RentACarPersistenceTest {
    private static final String NAME = "eartz";
    private static final String NIF = "NIF";
    private static final String IBAN = "IBAN";

    private static final String PLATE_CAR = "22-33-HZ";
    private static final String PLATE_MOTORCYCLE = "44-33-HZ";

    private static final String DRIVING_LICENSE = "lx1423";
    private static final LocalDate BEGIN = LocalDate.parse("2018-01-06");
    private static final LocalDate END = LocalDate.parse("2018-01-09");
    private static final String IBAN_BUYER = "IBAN";

    @Test
    public void success() {
        atomicProcess();
        atomicAssert();
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public void atomicProcess() {
        RentACar rentACar = new RentACar(NAME, NIF, IBAN);

        Vehicle car = new Car(PLATE_CAR, 10, 10, rentACar);

        Renting renting = RentACar.getRenting(RentACar.rent(Car.class, DRIVING_LICENSE, NIF, IBAN_BUYER, BEGIN, END));
        
        rentACar.getProcessor().submitRenting(renting);
    }

    @Atomic(mode = Atomic.TxMode.READ)
    public void atomicAssert() {
        Assert.assertEquals(1, FenixFramework.getDomainRoot().getRentACarSet().size());

        RentACar rentACar = FenixFramework.getDomainRoot().getRentACarSet().stream().findFirst().orElse(null);
        Assert.assertEquals(NIF, rentACar.getNIF());
        Assert.assertEquals(IBAN, rentACar.getIBAN());
        Assert.assertEquals(NAME, rentACar.getName());
        Assert.assertEquals(1, rentACar.getVehicleSet().size());
        Assert.assertNotNull(rentACar.getProcessor());
        
        Vehicle vehicle = rentACar.getVehicleSet().stream().findFirst().orElse(null);
        Assert.assertEquals(PLATE_CAR, vehicle.getPlate());
        Assert.assertEquals(10, vehicle.getKilometers());
        Assert.assertEquals(10, vehicle.getKilometers());
        Assert.assertEquals(1, vehicle.getRentingSet().size());
        
        List<Renting> rentings = new ArrayList<>(rentACar.getProcessor().getRentingSet());
        Assert.assertTrue(rentings.size() == 1);
        Assert.assertNotNull(rentings.get(0).getReference());
        
        Renting renting = vehicle.getRentingSet().stream().findFirst().orElse(null);
        Assert.assertEquals(BEGIN, renting.getBegin());
        Assert.assertEquals(END, renting.getEnd());
        Assert.assertEquals(NIF, renting.getClientNIF());
        Assert.assertEquals(IBAN, renting.getClientIBAN());
        Assert.assertEquals(DRIVING_LICENSE, renting.getDrivingLicense());
    }

    @After
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void tearDown() {
        FenixFramework.getDomainRoot().getRentACarSet().forEach(RentACar::delete);
    }

}
