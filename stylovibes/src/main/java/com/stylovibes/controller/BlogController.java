package com.stylovibes.controller;

import com.stylovibes.service.ArticuloService;
import com.stylovibes.service.CategoriaArticuloService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {

    private final ArticuloService articuloService;
    private final CategoriaArticuloService categoriaService;

    @GetMapping
    public String index(@RequestParam(required = false) Long categoria, Model model) {
        model.addAttribute("articulos", articuloService.listarPublicados(categoria));
        model.addAttribute("categorias", categoriaService.listarActivos());
        model.addAttribute("categoriaActual", categoria);
        return "blog/index";
    }

    @GetMapping("/{slug}")
    public String detalle(@PathVariable String slug, Model model) {
        model.addAttribute("articulo", articuloService.obtenerPublicadoPorSlug(slug));
        return "blog/detalle";
    }
}
