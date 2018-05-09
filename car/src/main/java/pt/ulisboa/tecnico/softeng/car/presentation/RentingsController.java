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
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.VehicleData;

@Controller
@RequestMapping(value = "/rentacars/rentacar/{code}/vehicles/vehicle/{plate}/rentings")
public class RentingsController {
    private static Logger logger = LoggerFactory.getLogger(RentingsController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String rentingForm(Model model, @PathVariable String code, @PathVariable String plate) {

        VehicleData vehicleData = RentACarInterface.getVehicleByPlate(code, plate);
        if (vehicleData == null) {
            model.addAttribute("error",
                    "Error: it does not exist a vehicle with plate " + plate);
            model.addAttribute("rentacar", RentACarInterface.getRentACarData(code));
            model.addAttribute("renting", new RentingData());
            model.addAttribute("rentings", RentACarInterface.getRentings(code, plate));
            model.addAttribute("vehicle", new VehicleData());
            return "vehiclesView";
        } else {
            model.addAttribute("rentacar", RentACarInterface.getRentACarData(code));
            model.addAttribute("renting", new RentingData());
            model.addAttribute("rentings", RentACarInterface.getRentings(code, plate));
            model.addAttribute("vehicle", vehicleData);
            return "rentingsView";
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public String rentingSubmit(Model model,
                                @PathVariable String code,
                                @PathVariable String plate,
                                @ModelAttribute RentingData renting) {
        logger.debug("Renting a car.");

        try {
            RentACarInterface.rent(code, plate, renting.getDrivingLicense(),
                    renting.getBuyerNIF(), renting.getBuyerIBAN(),
                    renting.getBegin(), renting.getEnd(), renting.getAdventureId());
        } catch (CarException be) {
            model.addAttribute("error", "Error: it was not possible to rent the vehicle");
            model.addAttribute("rentacar", RentACarInterface.getRentACarData(code));
            model.addAttribute("renting", renting);
            model.addAttribute("rentings", RentACarInterface.getRentings(code, plate));
            model.addAttribute("vehicle", RentACarInterface.getVehicleData(code, plate));
            return "rentingsView";
        }

        return "redirect:/rentacars/rentacar/" + code + "/vehicles/vehicle/" + plate + "/rentings";
    }

}
