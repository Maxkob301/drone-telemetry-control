package org.example.controller;


import org.example.services.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @Autowired
    FlightService flightService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/history")
    public String history(Model model) {
        model.addAttribute("records", flightService.findAll());
        return "history";
    }
}
