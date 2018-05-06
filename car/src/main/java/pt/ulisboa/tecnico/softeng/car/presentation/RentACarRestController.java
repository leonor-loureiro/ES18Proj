package pt.ulisboa.tecnico.softeng.car.presentation;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.services.local.RentACarInterface;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentingData;

@RestController
@RequestMapping(value = "/rentACarsControl")
public class RentACarRestController {

    private static Logger logger = LoggerFactory.getLogger(RentACarRestController.class);

    @RequestMapping(value = "/getRentingData/{reference}" , method = RequestMethod.GET)
    public RentingData getRenting(@PathVariable String reference) {
        logger.info("get car renting reference  reference:{}", reference);
        try {
            return RentACarInterface.getRentingByReference(reference);
        } catch (CarException ae) {
            logger.error("Error trying to get Car renting Data " + reference);
            return null;
        }
    }


    @RequestMapping(value="/cancelRenting/{reference}" ,method = RequestMethod.GET)
    public String cancelRenting(@PathVariable String reference) {
        logger.info("cancel Renting renting reference:{}", reference);
        try {
            return RentACarInterface.cancelRenting(reference);
        } catch (CarException be) {
            logger.error("Error trying to cancel renting " + reference);
            return null;
        }
    }


    @RequestMapping(value="/rent/type/{type}/license/{license}/begin/{begin}/end/{end}/nif/{nif}/iban/{iban}", method = RequestMethod.GET)
    public String rentCar(@PathVariable RentACarInterface.Type type, @PathVariable String license, @DateTimeFormat LocalDate begin,
                          @DateTimeFormat LocalDate end, @PathVariable String nif, @PathVariable String iban) {
        logger.info("rent a car type:{}, license:{}, begin:{} , end:{}, age:{} , buyerNif:{}, buyerIban:{}",
                type, license, begin, end, nif, iban);
        try {
            return RentACarInterface.rentVehicle(type, license, begin, end, nif, iban).getReference();
        } catch (CarException ae) {
            logger.error("Error trying to rent a car!");
            return null;
        }
    }
}