package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.service.CarService;
import web.service.CarServiceImp;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CarController {

    @GetMapping(value = "/cars")
    public String showCars(@RequestParam(value = "count", required = false) Integer count, ModelMap model){
        CarService carService = new CarServiceImp();
        count = count == null ? 7 : count;
        model.addAttribute("cars", carService.getCars(count));
        return "cars";
    }

}