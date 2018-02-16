package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;

public class RentACarConstructorTest {
    private static final String NAME = "eartz";

    @Test
    public void success() {
        RentACar rentACar = new RentACar(NAME);

        assertEquals(NAME, rentACar.getName());
    }

    @After
    public void tearDown() {
        RentACar.rentACars.clear();
    }

}
