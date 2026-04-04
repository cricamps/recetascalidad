package com.duoc.recetas.controller;

import com.duoc.recetas.entity.RecetaEntity;
import com.duoc.recetas.service.RecetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private RecetaService recetaService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        List<RecetaEntity> recientes = recetaService.getRecientes();
        List<RecetaEntity> populares = recetaService.getPopulares();
        model.addAttribute("recetasRecientes", recientes);
        model.addAttribute("recetasPopulares", populares);
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
