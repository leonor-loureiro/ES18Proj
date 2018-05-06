package pt.ulisboa.tecnico.softeng.car.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ulisboa.tecnico.softeng.car.services.local.RentACarInterface;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentACarData;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.VehicleData;

@Controller
@RequestMapping(value = "/rentACars/{code}/vehicles/{plate}/rentings")
public class RentingController {
    private static Logger logger = LoggerFactory.getLogger(RentingController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String rentingForm(Model model, @PathVariable String code, @PathVariable String plate) {
        logger.info("retingForm rentACarCode: {}, vehiclePlate: {}", code, plate);
        VehicleData vd = RentACarInterface.getVehicleDataByPlate(code, plate);

        if (vd == null) {
            model.addAttribute("error", "Error: Vehicle with plate " + plate + " does not exist in RentACar wiht code " + code);
            model.addAttribute("rentacar", new RentACarData());
            model.addAttribute("rentacars", RentACarInterface.listRentACars());
            return "rentACars";
        }

        model.addAttribute("vehicle", vd);
        return "rentings";
    }
}
