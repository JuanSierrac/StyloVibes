package com.stylovibes.controller.admin;

import com.stylovibes.dto.UsuarioAdminForm;
import com.stylovibes.exception.BusinessException;
import com.stylovibes.exception.DuplicateResourceException;
import com.stylovibes.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin/usuarios")
@RequiredArgsConstructor
public class AdminUsuarioController {

    private static final List<String> ROLES = List.of("USER", "ADMIN");

    private final UsuarioService usuarioService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("items", usuarioService.listar());
        return "admin/usuarios";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        UsuarioAdminForm form = new UsuarioAdminForm();
        form.getRoles().add("USER");
        model.addAttribute("form", form);
        model.addAttribute("rolesDisponibles", ROLES);
        return "admin/usuario-form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("form", usuarioService.aFormulario(id));
        model.addAttribute("rolesDisponibles", ROLES);
        return "admin/usuario-form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("form") UsuarioAdminForm form, BindingResult result,
                          Model model, RedirectAttributes ra) {
        if (!result.hasErrors()) {
            try {
                if (form.getId() == null) usuarioService.crearDesdeAdmin(form);
                else usuarioService.actualizarDesdeAdmin(form.getId(), form);
                ra.addFlashAttribute("ok", "Usuario guardado");
                return "redirect:/admin/usuarios";
            } catch (DuplicateResourceException ex) {
                result.rejectValue("email", "duplicate", ex.getMessage());
            } catch (BusinessException ex) {
                model.addAttribute("ko", ex.getMessage());
            }
        }
        model.addAttribute("rolesDisponibles", ROLES);
        return "admin/usuario-form";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, Principal principal, RedirectAttributes ra) {
        try {
            usuarioService.eliminar(id, principal.getName());
            ra.addFlashAttribute("ok", "Usuario eliminado");
        } catch (BusinessException ex) {
            ra.addFlashAttribute("ko", ex.getMessage());
        }
        return "redirect:/admin/usuarios";
    }
}
