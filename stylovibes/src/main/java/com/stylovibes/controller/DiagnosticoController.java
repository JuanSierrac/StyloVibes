package com.stylovibes.controller;

import com.stylovibes.entity.TipoCorporal;
import com.stylovibes.entity.enums.Genero;
import com.stylovibes.service.RecomendacionService;
import com.stylovibes.service.TipoCorporalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@org.springframework.web.bind.annotation.RequestMapping("/diagnostico")
@RequiredArgsConstructor
public class DiagnosticoController {

    private final TipoCorporalService tipoCorporalService;
    private final RecomendacionService recomendacionService;

    @GetMapping
    public String index(@RequestParam(defaultValue = "FEMENINO") Genero genero, Model model) {
        model.addAttribute("generos", Genero.values());
        model.addAttribute("genero", genero);
        model.addAttribute("tipos", tipoCorporalService.listarPorGenero(genero));
        return "diagnostico/index";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        TipoCorporal tipo = tipoCorporalService.obtener(id);
        model.addAttribute("item", tipo);
        model.addAttribute("recomendaciones", recomendacionService.porTipoCorporal(id));
        return "diagnostico/detalle";
    }
}
