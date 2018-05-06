package pt.ulisboa.tecnico.softeng.activity.presentation;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.services.local.ActivityInterface;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityReservationData;


@RestController
@RequestMapping(value = "/activitiesControl")
public class ActivityRestController {

    private static Logger logger = LoggerFactory.getLogger(ActivityRestController.class);

    @RequestMapping(value = "/getReservationData/{reference}" , method = RequestMethod.GET)
    public ActivityReservationData getReservationData(@PathVariable String reference) {
        logger.info("ActivityBookingData reference:{}", reference);
        try {
            return ActivityInterface.getActivityReservationData(reference);
        } catch (ActivityException ae) {
            logger.error("Activity Exception caught - Error trying to get Room Booking Data " + reference);
            return null;
        }
    }


    @RequestMapping(value="/cancelReservation/{reference}" ,method = RequestMethod.GET)
    public String cancelReservation(@PathVariable String reference) {
        logger.info("cancelBooking roomConfirmation:{}", reference);
        try {
            return ActivityInterface.cancelReservation(reference);
        } catch (ActivityException be) {
            logger.error("Activity Exception caught - Error trying to cancel Reservation " + reference);
            return null;
        }
    }


    @RequestMapping(value="/reserveActivity/begin/{begin}/end/{end}/age/{age}/nif/{nif}/iban/{iban}", method = RequestMethod.GET)
    public String reserveActivity(@DateTimeFormat LocalDate begin, @DateTimeFormat LocalDate end, @PathVariable int age,
                                  @PathVariable String nif, @PathVariable String iban) {
        logger.info("reserveRoom begin:{} , end:{}, age:{} , buyerNif:{}, buyerIban:{}", begin, end, age, nif, iban);
        try {
            return ActivityInterface.reserveActivity(begin, end, age, nif, iban);
        } catch (ActivityException ae) {
            logger.error("Activity Exception caught - Error trying to reserve Activity");
            return null;
        }
    }
}