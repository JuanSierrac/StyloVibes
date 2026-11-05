package com.stylovibes.controller;

import com.stylovibes.service.TipoRostroService;
import com.stylovibes.service.RecomendacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/visagismo")
@RequiredArgsConstructor
public class VisagismoController {

    private final TipoRostroService servicio;
    private final RecomendacionService recomendacionService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("items", servicio.listarActivos());
        return "visagismo/index";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("item", servicio.obtener(id));
        model.addAttribute("recomendaciones", recomendacionService.porTipoRostro(id));
        return "visagismo/detalle";
    }
}
