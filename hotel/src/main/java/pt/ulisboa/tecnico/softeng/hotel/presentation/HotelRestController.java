package pt.ulisboa.tecnico.softeng.hotel.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.services.local.HotelInterface;

import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.RoomBookingData;

import org.joda.time.LocalDate;

import java.util.Set;

import static java.time.temporal.WeekFields.ISO;

@RestController
@RequestMapping(value = "/hotelsControl")
public class HotelRestController {

    private static Logger logger = LoggerFactory.getLogger(HotelRestController.class);

    @RequestMapping(value = "/getRoomBookingData/{reference}" , method = RequestMethod.GET)
    public RoomBookingData getRoomBookingData(@PathVariable String reference) {
        logger.info("RoomBookingData reference:{}", reference);
        try {
            return HotelInterface.getRoomBookingData(reference);
        } catch (HotelException be) {
            logger.error("Hotel Exception caught - Error trying to get Room Booking Data " + reference);
            return null;
        }
    }



    @RequestMapping(value="/cancelBooking/{reference}" ,method = RequestMethod.GET)
    public String cancelBooking(@PathVariable String reference) {
        logger.info("cancelBooking roomConfirmation:{}", reference);
        try {
            return HotelInterface.cancelBooking(reference);
        } catch (HotelException be) {
            logger.error("Hotel Exception caught - Error trying to cancel Booking " + reference);
            return null;
        }
    }



    @RequestMapping(value="/reserveRoom/{type}/arrival/{arrival}/departure/{departure}/buyerNif/{buyerNif}/buyerIban/{buyerIban}", method = RequestMethod.GET)
    public String reserveRoom(@PathVariable Room.Type type, @DateTimeFormat LocalDate arrival, @DateTimeFormat LocalDate departure, @PathVariable String buyerNif, @PathVariable String buyerIban) {
        logger.info("reserveRoom type:{} , arrival:{}, departure:{}, buyerNif:{}, buyerIban:{}", type, arrival, departure, buyerNif, buyerIban);
        try {
            return HotelInterface.reserveRoom(type, arrival, departure, buyerNif, buyerIban);
        } catch (HotelException be) {
            logger.error("Hotel Exception caught - Error trying to reserve Room type: " + type + "   arrival:" + arrival + "   departure:" + departure + "   buyerNif:" + buyerNif + "   buyerIban:" + buyerIban);
            return null;
        }
    }

    @RequestMapping(value="/bulkBooking/{number}/arrival/{arrival}/departure/{departure}/buyerNif/{buyerNif}/buyerIban/{buyerIban}", method = RequestMethod.GET)
    public Set<String> bulkBooking(@PathVariable Integer number, @DateTimeFormat LocalDate arrival, @DateTimeFormat LocalDate departure, @PathVariable String buyerNif, @PathVariable String buyerIban) {
        logger.info("bulkBooking number:{} , arrival:{}, departure:{}, buyerNif:{}, buyerIban:{}", number, arrival, departure, buyerNif, buyerIban);
        try {
            return HotelInterface.bulkBooking(number, arrival, departure, buyerNif, buyerIban);
        } catch (HotelException be) {
            logger.error("Hotel Exception caught - Error trying to Bulk Bookings number: " + number + "   arrival:" + arrival + "   departure:" + departure + "   buyerNif:" + buyerNif + "   buyerIban:" + buyerIban);
            return null;
        }
    }

}