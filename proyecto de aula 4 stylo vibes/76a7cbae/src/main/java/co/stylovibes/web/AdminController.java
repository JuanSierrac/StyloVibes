package co.stylovibes.web;

import co.stylovibes.dominio.Caracteristica;
import co.stylovibes.dominio.Contenido;
import co.stylovibes.dominio.Rol;
import co.stylovibes.dominio.SeccionContenido;
import co.stylovibes.dominio.Usuario;
import co.stylovibes.dominio.UsuarioRepository;
import co.stylovibes.servicio.ContenidoService;
import co.stylovibes.servicio.Cuestionario;
import co.stylovibes.servicio.DiagnosticoService;
import co.stylovibes.servicio.Recomendaciones;
import co.stylovibes.servicio.UsuarioService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    public static class EncuestaVista {
        private final Usuario usuario;
        private final Caracteristica caracteristica;
        private final List<RespuestaNivel> respuestas;

        public EncuestaVista(Usuario usuario, Caracteristica caracteristica, List<RespuestaNivel> respuestas) {
            this.usuario = usuario;
            this.caracteristica = caracteristica;
            this.respuestas = respuestas;
        }

        public Usuario getUsuario() { return usuario; }
        public Caracteristica getCaracteristica() { return caracteristica; }
        public List<RespuestaNivel> getRespuestas() { return respuestas; }
        public String getRostro() {
            return caracteristica.getTipoRostro() == null ? "—" : Cuestionario.nombreRostro(caracteristica.getTipoRostro());
        }
        public String getEstacion() {
            return caracteristica.getEstacion() == null ? "—" : Cuestionario.nombreEstacion(caracteristica.getEstacion());
        }
        public String getCuerpo() {
            return caracteristica.getTipoCuerpo() == null ? "—" : Cuestionario.nombreCuerpo(caracteristica.getTipoCuerpo());
        }
        public String getEstilos() {
            List<String> nombres = Recomendaciones.nombresEstilos(caracteristica.getEstilos());
            return nombres.isEmpty() ? "—" : String.join(", ", nombres);
        }
        public int getPorcentaje() { return caracteristica.porcentaje(); }
    }

    public static class RespuestaNivel {
        private final String titulo;
        private final String detalle;

        public RespuestaNivel(String titulo, String detalle) {
            this.titulo = titulo;
            this.detalle = detalle;
        }

        public String getTitulo() { return titulo; }
        public String getDetalle() { return detalle; }
    }

    private final UsuarioRepository usuarioRepository;
    private final ContenidoService contenidoService;
    private final DiagnosticoService diagnosticoService;
    private final UsuarioService usuarioService;
    private final DataSource dataSource;

    public AdminController(UsuarioRepository usuarioRepository, ContenidoService contenidoService,
                           DiagnosticoService diagnosticoService, UsuarioService usuarioService,
                           DataSource dataSource) {
        this.usuarioRepository = usuarioRepository;
        this.contenidoService = contenidoService;
        this.diagnosticoService = diagnosticoService;
        this.usuarioService = usuarioService;
        this.dataSource = dataSource;
    }

    @ModelAttribute("seccionesContenido")
    public SeccionContenido[] seccionesContenido() {
        return SeccionContenido.values();
    }

    @GetMapping("/login")
    public String login(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMINISTRADOR".equals(a.getAuthority()))) {
            return "redirect:/admin";
        }
        model.addAttribute("hayAdmin", usuarioService.existeAdministrador());
        return "admin-login";
    }

    @GetMapping("/configuracion-inicial")
    public String configuracionInicial(Model model, HttpServletRequest request, RedirectAttributes flash) {
        if (usuarioService.existeAdministrador()) {
            flash.addFlashAttribute("error", "Ya existe una cuenta administradora. Inicia sesión.");
            return "redirect:/admin/login";
        }
        if (!esLocal(request)) {
            return "redirect:/admin/login";
        }
        if (!model.containsAttribute("adminForm")) {
            model.addAttribute("adminForm", new AdminSetupForm());
        }
        return "admin-configuracion";
    }

    @PostMapping("/configuracion-inicial")
    public String crearAdministrador(@Valid @ModelAttribute("adminForm") AdminSetupForm form,
                                     BindingResult resultado, Model model, HttpServletRequest request,
                                     RedirectAttributes flash) {
        if (usuarioService.existeAdministrador()) {
            flash.addFlashAttribute("error", "Ya existe una cuenta administradora. Inicia sesión.");
            return "redirect:/admin/login";
        }
        if (!esLocal(request)) {
            return "redirect:/admin/login";
        }
        if (!form.getPassword().equals(form.getConfirmarPassword())) {
            resultado.rejectValue("confirmarPassword", "coincide", "Las contraseñas no coinciden");
        }
        if (resultado.hasErrors()) {
            return "admin-configuracion";
        }
        try {
            Usuario admin = usuarioService.registrarAdministrador(form.getNombre(), form.getApellido(),
                    form.getEmail(), form.getPassword());
            try {
                request.login(admin.getEmail(), form.getPassword());
            } catch (ServletException ex) {
                flash.addFlashAttribute("mensaje", "Cuenta administradora creada. Inicia sesión.");
                return "redirect:/admin/login?creado";
            }
            flash.addFlashAttribute("mensaje", "¡Bienvenido! La cuenta administradora quedó creada.");
            return "redirect:/admin";
        } catch (UsuarioService.CorreoDuplicadoException ex) {
            resultado.rejectValue("email", "duplicado", ex.getMessage());
            return "admin-configuracion";
        } catch (UsuarioService.AdministradorExistenteException ex) {
            flash.addFlashAttribute("error", ex.getMessage());
            return "redirect:/admin/login";
        }
    }

    private boolean esLocal(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        return "127.0.0.1".equals(ip) || "::1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip);
    }

    @GetMapping
    public String panel(Model model) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        long estudiantes = 0;
        long completos = 0;
        for (Usuario u : usuarios) {
            if (u.getRol() == Rol.USUARIO) {
                estudiantes++;
                if (u.getCaracteristica() != null && u.getCaracteristica().todoCompletado()) {
                    completos++;
                }
            }
        }
        List<Usuario> recientes = new ArrayList<Usuario>(usuarios);
        recientes.sort(Comparator.comparing(Usuario::getFechaRegistro).reversed());
        if (recientes.size() > 5) {
            recientes = recientes.subList(0, 5);
        }
        model.addAttribute("totalUsuarios", estudiantes);
        model.addAttribute("totalCompletos", completos);
        model.addAttribute("totalContenidos", contenidoService.todos().size());
        model.addAttribute("recientes", recientes);
        return "admin-inicio";
    }

    @GetMapping("/respaldo")
    public ResponseEntity<Resource> respaldo() throws IOException {
        Path temporal = Files.createTempFile("stylovibes-respaldo-", ".sql");
        try {
            String ruta = temporal.toAbsolutePath().toString().replace("'", "''");
            try (Connection conexion = dataSource.getConnection();
                 Statement sentencia = conexion.createStatement()) {
                sentencia.execute("SCRIPT TO '" + ruta + "'");
            } catch (SQLException ex) {
                throw new IOException("No se pudo generar el respaldo de la base de datos", ex);
            }
            byte[] contenido = Files.readAllBytes(temporal);
            String nombre = "stylovibes-respaldo-" + LocalDate.now() + ".sql";
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombre + "\"")
                    .header(HttpHeaders.CACHE_CONTROL, "no-store")
                    .contentType(MediaType.parseMediaType("application/sql;charset=UTF-8"))
                    .contentLength(contenido.length)
                    .body(new ByteArrayResource(contenido, nombre));
        } finally {
            Files.deleteIfExists(temporal);
        }
    }

    @GetMapping("/contenidos")
    public String contenidos(Model model) {
        model.addAttribute("contenidos", contenidoService.todos());
        return "admin-contenidos";
    }

    @GetMapping("/contenidos/nuevo")
    public String nuevo(Model model) {
        Contenido contenido = new Contenido();
        contenido.setOrden(1);
        contenido.setPublicado(true);
        model.addAttribute("contenido", contenido);
        model.addAttribute("editando", false);
        return "admin-contenido-form";
    }

    @GetMapping("/contenidos/{id}/editar")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Contenido contenido = contenidoService.porId(id);
        if (contenido == null) {
            flash.addFlashAttribute("error", "El contenido ya no existe.");
            return "redirect:/admin/contenidos";
        }
        model.addAttribute("contenido", contenido);
        model.addAttribute("editando", true);
        return "admin-contenido-form";
    }

    @PostMapping("/contenidos")
    public String guardar(@Valid @ModelAttribute("contenido") Contenido contenido, BindingResult resultado,
                          Model model, RedirectAttributes flash) {
        if (!contenidoService.claveDisponible(contenido.getClave(), contenido.getId())) {
            resultado.rejectValue("clave", "duplicada", "Ya existe un contenido con esta clave.");
        }
        if (resultado.hasErrors()) {
            model.addAttribute("editando", contenido.getId() != null);
            return "admin-contenido-form";
        }
        boolean nuevo = contenido.getId() == null;
        contenidoService.guardar(contenido);
        flash.addFlashAttribute("mensaje", nuevo ? "Contenido creado." : "Contenido actualizado.");
        return "redirect:/admin/contenidos";
    }

    @PostMapping("/contenidos/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        if (contenidoService.porId(id) == null) {
            flash.addFlashAttribute("error", "El contenido ya no existe.");
        } else {
            contenidoService.eliminar(id);
            flash.addFlashAttribute("mensaje", "Contenido eliminado.");
        }
        return "redirect:/admin/contenidos";
    }

    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        List<EncuestaVista> vistas = construirEncuestas();
        model.addAttribute("usuariosVista", vistas);
        model.addAttribute("totalUsuarios", vistas.size());
        return "admin-usuarios";
    }

    @GetMapping("/encuestas")
    public String encuestas(Model model) {
        model.addAttribute("encuestas", construirEncuestas());
        return "admin-encuestas";
    }

    private List<EncuestaVista> construirEncuestas() {
        List<EncuestaVista> encuestas = new ArrayList<EncuestaVista>();
        for (Usuario u : usuarioRepository.findAll()) {
            if (u.getRol() != Rol.USUARIO || u.getCaracteristica() == null) {
                continue;
            }
            List<RespuestaNivel> respuestas = new ArrayList<RespuestaNivel>();
            for (int i = 1; i <= 5; i++) {
                if (!u.getCaracteristica().nivelCompletado(i)) {
                    continue;
                }
                Cuestionario.Nivel nivel = diagnosticoService.nivel(i, u);
                String detalle = diagnosticoService.respuestasLegibles(i, u, u.getCaracteristica());
                respuestas.add(new RespuestaNivel("Nivel " + i + " · " + nivel.getTitulo(), detalle));
            }
            encuestas.add(new EncuestaVista(u, u.getCaracteristica(), respuestas));
        }
        encuestas.sort((a, b) -> b.getUsuario().getFechaRegistro()
                .compareTo(a.getUsuario().getFechaRegistro()));
        return encuestas;
    }
}
