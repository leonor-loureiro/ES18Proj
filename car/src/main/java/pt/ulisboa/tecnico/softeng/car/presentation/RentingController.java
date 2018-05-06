package pt.ulisboa.tecnico.softeng.car.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.services.local.RentACarInterface;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentACarData;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.VehicleData;

@Controller
@RequestMapping(value = "/rentACars/{code}/vehicles/{plate}/rentings")
public class RentingController {
    private static Logger logger = LoggerFactory.getLogger(RentingController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String rentingForm(Model model, @PathVariable String code, @PathVariable String plate) {
        logger.info("rentingForm rentACarCode: {}, vehiclePlate: {}", code, plate);

        VehicleData vd = RentACarInterface.getVehicleDataByPlate(code, plate);
        model.addAttribute("vehicle", vd);
        model.addAttribute("rentings", RentACarInterface.getRentingsByPlate(code, plate));
        model.addAttribute("renting", new RentingData());

        if (vd == null) {
            model.addAttribute("error", "Error: Vehicle with plate " + plate + " does not exist in RentACar wiht code " + code);
            model.addAttribute("rentacar", new RentACarData());
            model.addAttribute("rentacars", RentACarInterface.listRentACars());
            return "rentACars";
        }

        return "rentings";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String rentingSubmit(Model model, @PathVariable String code, @PathVariable String plate, @ModelAttribute RentingData renting) {
        logger.info("rentingForm : rentACarCode{} plate: {}, drivingLicense:{}, begin:{}, end:{}, nif:{}, iban:{} ",
                code, plate, renting.getDrivingLicense(), renting.getBegin(), renting.getEnd(), renting.getNif(), renting.getIban());

        try {
            RentACarInterface.rentVehicle(code, plate, renting.getDrivingLicense(), renting.getBegin(), renting.getEnd(), renting.getNif(), renting.getIban());
        } catch (CarException ce) {
            model.addAttribute("error", "Error: It was not possible to create vehicle");
            model.addAttribute("renting", renting);
            model.addAttribute("rentacar", RentACarInterface.getRentACarDataByCode(code));
            return "vehicles";
        }
        return "rentings";
    }
}
