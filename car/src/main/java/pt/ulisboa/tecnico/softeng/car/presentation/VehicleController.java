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
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.VehicleData;

@Controller
@RequestMapping(value = "rentACars/{code}/vehicles")
public class VehicleController {
    private static Logger logger = LoggerFactory.getLogger(VehicleController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String vehicleForm(Model model, @PathVariable String code) {

        RentACarData rentACarData = RentACarInterface.getRentACarDataByCode(code);

        if (rentACarData == null) {
            model.addAttribute("error", "Error: RentACar with code " + code + " does not exist");
            model.addAttribute("rentacar", new RentACarData());
            return "rentACars";
        }

        model.addAttribute("rentacar", rentACarData);
        model.addAttribute("vehicle", new VehicleData());
        return "vehicles";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String vehicleSubmit(Model model, @PathVariable String code, @ModelAttribute VehicleData vd) {
        logger.info("vehicleForm RentAcarCode: {} isCar: {}", code, vd.getIsCar());

        try {
            RentACarInterface.createVehicle(code, vd);
        } catch (CarException ce) {
            model.addAttribute("error", "Error: It was not possible to create vehicle");
            model.addAttribute("vehicle", vd);
            model.addAttribute("rentacar", RentACarInterface.getRentACarDataByCode(code));
            return "vehicles";
        }
        return "redirect:/rentACars/" + code + "/vehicles";
    }
}
