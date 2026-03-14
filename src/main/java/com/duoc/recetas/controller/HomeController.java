package com.duoc.recetas.controller;

import com.duoc.recetas.model.RecetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private RecetaData recetaData;

    @GetMapping("/")
    public String root(Model model) {
        model.addAttribute("recetasRecientes", recetaData.getRecetasRecientes());
        model.addAttribute("recetasPopulares", recetaData.getRecetasPopulares());
        return "home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("recetasRecientes", recetaData.getRecetasRecientes());
        model.addAttribute("recetasPopulares", recetaData.getRecetasPopulares());
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
