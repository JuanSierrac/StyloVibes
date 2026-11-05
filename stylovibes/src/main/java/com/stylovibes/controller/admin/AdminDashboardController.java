package com.stylovibes.controller.admin;

import com.stylovibes.service.EstadisticasService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final EstadisticasService estadisticasService;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("stats", estadisticasService.resumen());
        return "admin/dashboard";
    }
}
