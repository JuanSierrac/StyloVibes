package com.stylovibes.controller.admin;

import com.stylovibes.dto.ArticuloForm;
import com.stylovibes.service.ArticuloService;
import com.stylovibes.service.CategoriaArticuloService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/admin/articulos")
@RequiredArgsConstructor
public class AdminArticuloController {

    private final ArticuloService articuloService;
    private final CategoriaArticuloService categoriaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("items", articuloService.listarTodos());
        return "admin/articulos";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("form", new ArticuloForm());
        model.addAttribute("categorias", categoriaService.listarActivos());
        return "admin/articulo-form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("form", articuloService.aFormulario(id));
        model.addAttribute("categorias", categoriaService.listarActivos());
        return "admin/articulo-form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("form") ArticuloForm form, BindingResult result,
                          Principal principal, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listarActivos());
            return "admin/articulo-form";
        }
        if (form.getId() == null) articuloService.crear(form, principal.getName());
        else articuloService.actualizar(form.getId(), form);
        ra.addFlashAttribute("ok", "Artículo guardado");
        return "redirect:/admin/articulos";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        articuloService.eliminar(id);
        ra.addFlashAttribute("ok", "Artículo eliminado");
        return "redirect:/admin/articulos";
    }
}
