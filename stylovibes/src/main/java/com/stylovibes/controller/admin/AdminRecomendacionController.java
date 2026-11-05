package com.stylovibes.controller.admin;

import com.stylovibes.dto.RecomendacionForm;
import com.stylovibes.exception.BusinessException;
import com.stylovibes.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/recomendaciones")
@RequiredArgsConstructor
public class AdminRecomendacionController {

    private final RecomendacionService recomendacionService;
    private final TipoCorporalService tipoCorporalService;
    private final TipoRostroService tipoRostroService;
    private final ColorimetriaService colorimetriaService;
    private final EstiloService estiloService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("items", recomendacionService.listar());
        return "admin/recomendaciones";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("form", new RecomendacionForm());
        cargarCatalogos(model);
        return "admin/recomendacion-form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("form", recomendacionService.aFormulario(id));
        cargarCatalogos(model);
        return "admin/recomendacion-form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("form") RecomendacionForm form, BindingResult result,
                          Model model, RedirectAttributes ra) {
        if (!result.hasErrors()) {
            try {
                if (form.getId() == null) recomendacionService.crear(form);
                else recomendacionService.actualizar(form.getId(), form);
                ra.addFlashAttribute("ok", "Recomendación guardada");
                return "redirect:/admin/recomendaciones";
            } catch (BusinessException ex) {
                model.addAttribute("ko", ex.getMessage());
            }
        }
        cargarCatalogos(model);
        return "admin/recomendacion-form";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        recomendacionService.eliminar(id);
        ra.addFlashAttribute("ok", "Recomendación eliminada");
        return "redirect:/admin/recomendaciones";
    }

    private void cargarCatalogos(Model model) {
        model.addAttribute("tiposCorporales", tipoCorporalService.listarActivos());
        model.addAttribute("tiposRostro", tipoRostroService.listarActivos());
        model.addAttribute("colorimetrias", colorimetriaService.listarActivos());
        model.addAttribute("estilos", estiloService.listarActivos());
    }
}
