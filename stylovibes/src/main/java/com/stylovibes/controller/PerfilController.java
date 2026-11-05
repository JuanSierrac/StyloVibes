package com.stylovibes.controller;

import com.stylovibes.dto.PerfilRequest;
import com.stylovibes.entity.PerfilUsuario;
import com.stylovibes.entity.enums.Genero;
import com.stylovibes.exception.BusinessException;
import com.stylovibes.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilService perfilService;
    private final TipoCorporalService tipoCorporalService;
    private final TipoRostroService tipoRostroService;
    private final ColorimetriaService colorimetriaService;
    private final EstiloService estiloService;

    @GetMapping
    public String ver(Principal principal, Model model) {
        PerfilUsuario p = perfilService.obtenerPorEmail(principal.getName());
        PerfilRequest form = new PerfilRequest();
        form.setNombre(p.getUsuario().getNombre());
        form.setApellido(p.getUsuario().getApellido());
        form.setGenero(p.getGenero());
        form.setBio(p.getBio());
        if (p.getTipoCorporal() != null) form.setTipoCorporalId(p.getTipoCorporal().getId());
        if (p.getTipoRostro() != null) form.setTipoRostroId(p.getTipoRostro().getId());
        if (p.getColorimetria() != null) form.setColorimetriaId(p.getColorimetria().getId());
        if (p.getEstiloPreferido() != null) form.setEstiloId(p.getEstiloPreferido().getId());
        model.addAttribute("form", form);
        cargarCatalogos(model);
        return "usuario/perfil";
    }

    @PostMapping
    public String guardar(@Valid @ModelAttribute("form") PerfilRequest form, BindingResult result,
                          Principal principal, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            cargarCatalogos(model);
            return "usuario/perfil";
        }
        try {
            perfilService.actualizar(principal.getName(), form);
        } catch (BusinessException ex) {
            model.addAttribute("ko", ex.getMessage());
            cargarCatalogos(model);
            return "usuario/perfil";
        }
        ra.addFlashAttribute("ok", "Perfil actualizado");
        return "redirect:/dashboard";
    }

    // ---- Guardar un resultado del diagnóstico en el perfil ----
    @PostMapping("/asignar/cuerpo/{id}")
    public String asignarCuerpo(@PathVariable Long id, Principal principal, RedirectAttributes ra) {
        perfilService.asignarTipoCorporal(principal.getName(), id);
        return guardado(ra, "Tipo corporal guardado en tu perfil");
    }

    @PostMapping("/asignar/rostro/{id}")
    public String asignarRostro(@PathVariable Long id, Principal principal, RedirectAttributes ra) {
        perfilService.asignarTipoRostro(principal.getName(), id);
        return guardado(ra, "Tipo de rostro guardado en tu perfil");
    }

    @PostMapping("/asignar/color/{id}")
    public String asignarColor(@PathVariable Long id, Principal principal, RedirectAttributes ra) {
        perfilService.asignarColorimetria(principal.getName(), id);
        return guardado(ra, "Tu estación de color quedó guardada");
    }

    @PostMapping("/asignar/estilo/{id}")
    public String asignarEstilo(@PathVariable Long id, Principal principal, RedirectAttributes ra) {
        perfilService.asignarEstilo(principal.getName(), id);
        return guardado(ra, "Estilo preferido guardado");
    }

    private String guardado(RedirectAttributes ra, String mensaje) {
        ra.addFlashAttribute("ok", mensaje);
        return "redirect:/resultado";
    }

    private void cargarCatalogos(Model model) {
        model.addAttribute("generos", Genero.values());
        model.addAttribute("tiposCorporales", tipoCorporalService.listarActivos());
        model.addAttribute("tiposRostro", tipoRostroService.listarActivos());
        model.addAttribute("colorimetrias", colorimetriaService.listarActivos());
        model.addAttribute("estilos", estiloService.listarActivos());
    }
}
