package co.stylovibes.web;

import co.stylovibes.dominio.SeccionContenido;
import co.stylovibes.servicio.ContenidoService;
import co.stylovibes.servicio.Cuestionario;
import co.stylovibes.servicio.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class InicioController {

    private final UsuarioService usuarioService;
    private final ContenidoService contenidoService;

    public InicioController(UsuarioService usuarioService, ContenidoService contenidoService) {
        this.usuarioService = usuarioService;
        this.contenidoService = contenidoService;
    }

    @ModelAttribute("niveles")
    public List<Cuestionario.Nivel> niveles() {
        List<Cuestionario.Nivel> niveles = new ArrayList<Cuestionario.Nivel>();
        for (int i = 1; i <= 5; i++) {
            niveles.add(Cuestionario.nivel(i, "mixto"));
        }
        return niveles;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("hero", contenidoService.porClave("hero"));
        model.addAttribute("bienvenida", contenidoService.porClave("bienvenida"));
        model.addAttribute("catalogoIntro", contenidoService.porClave("catalogo"));
        model.addAttribute("consejos", contenidoService.porSeccion(SeccionContenido.CONSEJOS));
        return "inicio";
    }

    @GetMapping("/login")
    public String login(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            boolean admin = authentication.getAuthorities().stream()
                    .anyMatch(a -> "ROLE_ADMINISTRADOR".equals(a.getAuthority()));
            return admin ? "redirect:/admin" : "redirect:/app";
        }
        return "login";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        if (!model.containsAttribute("registroForm")) {
            model.addAttribute("registroForm", new RegistroForm());
        }
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("registroForm") RegistroForm form, BindingResult resultado,
                            Model model, RedirectAttributes flash) {
        if (!form.getPassword().equals(form.getConfirmarPassword())) {
            resultado.rejectValue("confirmarPassword", "coincide", "Las contraseñas no coinciden");
        }
        if (resultado.hasErrors()) {
            return "registro";
        }
        try {
            usuarioService.registrar(form.getNombre(), form.getApellido(), form.getEdad(), form.getGenero(),
                    form.getEmail(), form.getPassword());
        } catch (UsuarioService.CorreoDuplicadoException ex) {
            resultado.rejectValue("email", "duplicado", ex.getMessage());
            return "registro";
        }
        flash.addFlashAttribute("mensaje", "¡Cuenta creada! Ahora inicia sesión para comenzar tu diagnóstico.");
        return "redirect:/login?registrado";
    }
}
