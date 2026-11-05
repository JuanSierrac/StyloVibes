package com.stylovibes.controller.admin;

import com.stylovibes.exception.BusinessException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * CRUD genérico para catálogos (tipos corporales, rostros, colorimetrías,
 * estilos y categorías). Los campos se describen en {@link AdminResourceRegistry}.
 */
@Controller
@RequestMapping("/admin/catalogo")
@RequiredArgsConstructor
public class AdminCatalogoController {

    private final AdminResourceRegistry registry;
    private final Validator validator;

    @GetMapping("/{slug}")
    public String listar(@PathVariable String slug, Model model) {
        AdminResource r = registry.obtener(slug);
        model.addAttribute("recurso", r);
        model.addAttribute("items", r.service().listar());
        return "admin/catalogo-lista";
    }

    @GetMapping("/{slug}/nuevo")
    public String nuevo(@PathVariable String slug, Model model) {
        AdminResource r = registry.obtener(slug);
        Object item = r.nuevaInstancia();
        // Nuevos registros activos por defecto
        if (PropertyAccessorFactory.forBeanPropertyAccess(item).isWritableProperty("activo")) {
            PropertyAccessorFactory.forBeanPropertyAccess(item).setPropertyValue("activo", true);
        }
        return formulario(model, r, item, List.of());
    }

    @GetMapping("/{slug}/{id}/editar")
    public String editar(@PathVariable String slug, @PathVariable Long id, Model model) {
        AdminResource r = registry.obtener(slug);
        return formulario(model, r, r.service().obtener(id), List.of());
    }

    @PostMapping("/{slug}/guardar")
    public String guardar(@PathVariable String slug, @RequestParam(required = false) Long id,
                          @RequestParam Map<String, String> params, Model model, RedirectAttributes ra) {
        AdminResource r = registry.obtener(slug);
        Object item = r.nuevaInstancia();
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(item);

        for (FieldSpec campo : r.campos()) {
            String raw = params.get(campo.name());
            Object valor = campo.esCheckbox()
                    ? (Object) params.containsKey(campo.name())
                    : (raw == null || raw.isBlank() ? null : raw.trim());
            bw.setPropertyValue(campo.name(), valor);
        }

        List<String> errores = new ArrayList<>();
        Set<ConstraintViolation<Object>> violaciones = validator.validate(item);
        violaciones.forEach(v -> errores.add(v.getMessage()));

        if (errores.isEmpty()) {
            try {
                if (id == null) r.service().crear(item); else r.service().actualizar(id, item);
                ra.addFlashAttribute("ok", "Registro guardado correctamente");
                return "redirect:/admin/catalogo/" + slug;
            } catch (BusinessException ex) {
                errores.add(ex.getMessage());
            }
        }
        bw.setPropertyValue("id", id);
        return formulario(model, r, item, errores);
    }

    @PostMapping("/{slug}/{id}/eliminar")
    public String eliminar(@PathVariable String slug, @PathVariable Long id, RedirectAttributes ra) {
        AdminResource r = registry.obtener(slug);
        try {
            r.service().eliminar(id);
            ra.addFlashAttribute("ok", "Registro eliminado");
        } catch (BusinessException ex) {
            ra.addFlashAttribute("ko", ex.getMessage());
        }
        return "redirect:/admin/catalogo/" + slug;
    }

    private String formulario(Model model, AdminResource r, Object item, List<String> errores) {
        model.addAttribute("recurso", r);
        model.addAttribute("item", item);
        model.addAttribute("errores", errores);
        return "admin/catalogo-form";
    }
}
