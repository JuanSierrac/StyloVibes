package com.stylovibes.controller;

import com.stylovibes.service.ArticuloService;
import com.stylovibes.service.MotorRecomendacionService;
import com.stylovibes.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final PerfilService perfilService;
    private final MotorRecomendacionService motorService;
    private final ArticuloService articuloService;

    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model) {
        model.addAttribute("perfil", perfilService.obtenerPorEmail(principal.getName()));
        model.addAttribute("ultimosArticulos", articuloService.ultimosPublicados());
        return "usuario/dashboard";
    }

    @GetMapping("/resultado")
    public String resultado(Principal principal, Model model) {
        model.addAttribute("resultado", motorService.generar(principal.getName()));
        return "usuario/resultado";
    }
}
