package com.stylovibes.controller;

import com.stylovibes.service.ArticuloService;
import com.stylovibes.service.EstiloService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ArticuloService articuloService;
    private final EstiloService estiloService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("ultimosArticulos", articuloService.ultimosPublicados());
        model.addAttribute("estilos", estiloService.listarActivos());
        return "home";
    }
}
