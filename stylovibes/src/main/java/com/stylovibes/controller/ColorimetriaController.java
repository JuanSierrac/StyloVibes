package com.stylovibes.controller;

import com.stylovibes.entity.Colorimetria;
import com.stylovibes.service.ColorimetriaService;
import com.stylovibes.service.RecomendacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/colorimetria")
@RequiredArgsConstructor
public class ColorimetriaController {

    private final ColorimetriaService colorimetriaService;
    private final RecomendacionService recomendacionService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("items", colorimetriaService.listarActivos());
        return "colorimetria/index";
    }

    @GetMapping("/test")
    public String test() {
        return "colorimetria/test";
    }

    /**
     * Test de 6 preguntas: 3 de subtono (C=cálido, F=frío) y 3 de intensidad
     * (L=claro, O=oscuro). Primavera/Otoño son cálidas; Verano/Invierno frías.
     */
    @PostMapping("/test")
    public String resolver(@RequestParam(defaultValue = "") String q1, @RequestParam(defaultValue = "") String q2,
                           @RequestParam(defaultValue = "") String q3, @RequestParam(defaultValue = "") String q4,
                           @RequestParam(defaultValue = "") String q5, @RequestParam(defaultValue = "") String q6) {
        int calidos = contar("C", q1, q2, q3);
        int frios = contar("F", q1, q2, q3);
        int claros = contar("L", q4, q5, q6);
        int oscuros = contar("O", q4, q5, q6);

        boolean calido = calidos >= frios;
        boolean claro = claros >= oscuros;
        String estacion = calido ? (claro ? "Primavera" : "Otoño") : (claro ? "Verano" : "Invierno");

        Colorimetria resultado = colorimetriaService.obtenerPorNombre(estacion);
        return "redirect:/colorimetria/" + resultado.getId() + "?test";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("item", colorimetriaService.obtener(id));
        model.addAttribute("recomendaciones", recomendacionService.porColorimetria(id));
        return "colorimetria/detalle";
    }

    private int contar(String valor, String... respuestas) {
        int n = 0;
        for (String r : respuestas) if (valor.equals(r)) n++;
        return n;
    }
}
