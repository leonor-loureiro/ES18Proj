package pt.ulisboa.tecnico.softeng.car.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.services.local.RentACarInterface;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentACarData;

@Controller
@RequestMapping(value = "/rentACars")
public class RentACarController {
    private static Logger logger = LoggerFactory.getLogger(RentACarController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String rentACarForm(Model model) {
        model.addAttribute("rentacar", new RentACarData());
        return "rentACars";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String rentACarSubmit(Model model, @ModelAttribute RentACarData rentacar) {
        try {
            RentACarInterface.createRentACar(rentacar);
        } catch (CarException race) {
            model.addAttribute("error", "Error: it was not possible to create rentacar");
            model.addAttribute("rentacar", rentacar);
            return "rentacars";
        }
        return "redirect:/rentACars";
    }
}