package co.stylovibes.web;

import co.stylovibes.dominio.Caracteristica;
import co.stylovibes.dominio.Catalogo;
import co.stylovibes.dominio.CatalogoItem;
import co.stylovibes.dominio.CatalogoRepository;
import co.stylovibes.dominio.Seccion;
import co.stylovibes.dominio.Usuario;
import co.stylovibes.servicio.CatalogoService;
import co.stylovibes.servicio.ContenidoService;
import co.stylovibes.servicio.Cuestionario;
import co.stylovibes.servicio.DiagnosticoService;
import co.stylovibes.servicio.Recomendaciones;
import co.stylovibes.servicio.UsuarioService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/app")
public class AppController {

    public static class NivelEstado {
        private final int numero;
        private final String titulo;
        private final String imagen;
        private final Caracteristica.Estado estado;

        public NivelEstado(int numero, String titulo, String imagen, Caracteristica.Estado estado) {
            this.numero = numero;
            this.titulo = titulo;
            this.imagen = imagen;
            this.estado = estado;
        }

        public int getNumero() { return numero; }
        public String getTitulo() { return titulo; }
        public String getImagen() { return imagen; }
        public Caracteristica.Estado getEstado() { return estado; }
        public String getEstadoClase() { return estado.name().toLowerCase().replace('_', '-'); }
        public String getEstadoTexto() {
            switch (estado) {
                case COMPLETADO: return "Completado";
                case EN_CURSO: return "En curso";
                default: return "Pendiente";
            }
        }
    }

    public static class SeccionVista {
        private final Seccion seccion;
        private final List<CatalogoItem> items;

        public SeccionVista(Seccion seccion, List<CatalogoItem> items) {
            this.seccion = seccion;
            this.items = items;
        }

        public String getTitulo() { return seccion.getTitulo(); }
        public List<CatalogoItem> getItems() { return items; }
    }

    private final UsuarioService usuarioService;
    private final DiagnosticoService diagnosticoService;
    private final CatalogoService catalogoService;
    private final CatalogoRepository catalogoRepository;
    private final ContenidoService contenidoService;

    public AppController(UsuarioService usuarioService, DiagnosticoService diagnosticoService,
                         CatalogoService catalogoService, CatalogoRepository catalogoRepository,
                         ContenidoService contenidoService) {
        this.usuarioService = usuarioService;
        this.diagnosticoService = diagnosticoService;
        this.catalogoService = catalogoService;
        this.catalogoRepository = catalogoRepository;
        this.contenidoService = contenidoService;
    }

    @GetMapping
    public String panel(@AuthenticationPrincipal UserDetails principal, Model model) {
        Usuario usuario = usuario(principal);
        Caracteristica c = diagnosticoService.obtenerOCrear(usuario);
        model.addAttribute("usuario", usuario);
        model.addAttribute("caracteristica", c);
        model.addAttribute("niveles", estados(usuario, c));
        model.addAttribute("siguiente", siguiente(c));
        return "app-inicio";
    }

    @GetMapping("/nivel/{numero}")
    public String verNivel(@PathVariable int numero, @AuthenticationPrincipal UserDetails principal,
                           Model model, RedirectAttributes flash) {
        if (numero < 1 || numero > 5) {
            return "redirect:/app";
        }
        Usuario usuario = usuario(principal);
        Caracteristica c = diagnosticoService.obtenerOCrear(usuario);
        if (!c.desbloqueado(numero)) {
            flash.addFlashAttribute("error", "Completa los niveles anteriores para desbloquear este nivel.");
            return "redirect:/app";
        }
        Cuestionario.Nivel nivel = diagnosticoService.nivel(numero, usuario);
        model.addAttribute("usuario", usuario);
        model.addAttribute("caracteristica", c);
        model.addAttribute("nivel", nivel);
        model.addAttribute("valores", diagnosticoService.valoresGuardados(numero, c));
        model.addAttribute("errores", new ArrayList<String>());
        model.addAttribute("contenido", contenidoService.porClave("nivel-" + numero));
        return "app-nivel";
    }

    @PostMapping("/nivel/{numero}")
    public String guardarNivel(@PathVariable int numero, @AuthenticationPrincipal UserDetails principal,
                               HttpServletRequest request, Model model, RedirectAttributes flash) {
        if (numero < 1 || numero > 5) {
            return "redirect:/app";
        }
        Usuario usuario = usuario(principal);
        Caracteristica c = diagnosticoService.obtenerOCrear(usuario);
        if (!c.desbloqueado(numero)) {
            flash.addFlashAttribute("error", "Completa los niveles anteriores para desbloquear este nivel.");
            return "redirect:/app";
        }
        DiagnosticoService.Resultado resultado = diagnosticoService.procesar(numero, usuario, request.getParameterMap());
        if (!resultado.isOk()) {
            Cuestionario.Nivel nivel = diagnosticoService.nivel(numero, usuario);
            model.addAttribute("usuario", usuario);
            model.addAttribute("caracteristica", c);
            model.addAttribute("nivel", nivel);
            model.addAttribute("valores", resultado.getRespuestas());
            model.addAttribute("errores", resultado.getErrores());
            model.addAttribute("contenido", contenidoService.porClave("nivel-" + numero));
            return "app-nivel";
        }
        if (c.todoCompletado()) {
            flash.addFlashAttribute("mensaje", "¡Completaste los 5 niveles! Este es tu resultado.");
            return "redirect:/app/resultados";
        }
        flash.addFlashAttribute("mensaje", "Nivel " + numero + " completado. ¡Sigue con el siguiente!");
        return "redirect:/app";
    }

    @GetMapping("/resultados")
    public String resultados(@AuthenticationPrincipal UserDetails principal, Model model,
                             RedirectAttributes flash) {
        Usuario usuario = usuario(principal);
        Caracteristica c = diagnosticoService.obtenerOCrear(usuario);
        if (!c.todoCompletado()) {
            flash.addFlashAttribute("error", "Aún faltan niveles por completar.");
            return "redirect:/app";
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("caracteristica", c);
        model.addAttribute("rostro", Recomendaciones.rostro(c.getTipoRostro()));
        model.addAttribute("facciones", Recomendaciones.facciones(c.getFacciones()));
        model.addAttribute("color", Recomendaciones.color(c.getEstacion()));
        model.addAttribute("subtonoNombre", Cuestionario.nombreSubtono(c.getSubtono()));
        model.addAttribute("estacionNombre", Cuestionario.nombreEstacion(c.getEstacion()));
        model.addAttribute("cuerpo", Recomendaciones.cuerpo(usuario.getGenero(), c.getTipoCuerpo()));
        model.addAttribute("cuerpoNombre", Cuestionario.nombreCuerpo(c.getTipoCuerpo()));
        model.addAttribute("estilos", Recomendaciones.estilos(c.getEstilos()));
        Map<String, String> imagenesNivel = new LinkedHashMap<String, String>();
        for (int i = 1; i <= 5; i++) {
            imagenesNivel.put(String.valueOf(i), Cuestionario.imagenNivel(i, usuario.getGenero()));
        }
        model.addAttribute("imagenesNivel", imagenesNivel);
        return "app-resultados";
    }

    @GetMapping("/catalogo")
    public String catalogo(@AuthenticationPrincipal UserDetails principal, Model model, RedirectAttributes flash) {
        Usuario usuario = usuario(principal);
        Caracteristica c = diagnosticoService.obtenerOCrear(usuario);
        if (!c.todoCompletado()) {
            flash.addFlashAttribute("error", "Completa los 5 niveles para generar tu catálogo personal.");
            return "redirect:/app";
        }
        Catalogo catalogo = catalogoRepository.findByUsuario(usuario).orElse(null);
        if (catalogo == null || !catalogo.usaImagenesDetalladas()) {
            catalogo = catalogoService.generar(usuario, c);
        }
        List<SeccionVista> secciones = new ArrayList<SeccionVista>();
        for (Seccion seccion : Seccion.values()) {
            secciones.add(new SeccionVista(seccion, catalogo.itemsDe(seccion)));
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("catalogo", catalogo);
        model.addAttribute("secciones", secciones);
        model.addAttribute("intro", contenidoService.porClave("catalogo"));
        return "app-catalogo";
    }

    @GetMapping("/perfil")
    public String perfil(@AuthenticationPrincipal UserDetails principal, Model model) {
        Usuario usuario = usuario(principal);
        Caracteristica c = diagnosticoService.obtenerOCrear(usuario);
        model.addAttribute("usuario", usuario);
        model.addAttribute("caracteristica", c);
        model.addAttribute("niveles", estados(usuario, c));
        model.addAttribute("rostroNombre", c.getTipoRostro() == null ? null : Cuestionario.nombreRostro(c.getTipoRostro()));
        model.addAttribute("subtonoNombre", c.getSubtono() == null ? null : Cuestionario.nombreSubtono(c.getSubtono()));
        model.addAttribute("estacionNombre", c.getEstacion() == null ? null : Cuestionario.nombreEstacion(c.getEstacion()));
        model.addAttribute("cuerpoNombre", c.getTipoCuerpo() == null ? null : Cuestionario.nombreCuerpo(c.getTipoCuerpo()));
        model.addAttribute("estilosNombres", Recomendaciones.nombresEstilos(c.getEstilos()));
        return "app-perfil";
    }

    private Usuario usuario(UserDetails principal) {
        Usuario usuario = usuarioService.porEmail(principal.getUsername());
        if (usuario == null) {
            throw new IllegalStateException("Sesión sin usuario válido");
        }
        return usuario;
    }

    private List<NivelEstado> estados(Usuario usuario, Caracteristica c) {
        List<NivelEstado> lista = new ArrayList<NivelEstado>();
        for (int i = 1; i <= 5; i++) {
            Cuestionario.Nivel nivel = diagnosticoService.nivel(i, usuario);
            lista.add(new NivelEstado(i, nivel.getTitulo(), Cuestionario.imagenNivel(i, "mixto"), c.estado(i)));
        }
        return lista;
    }

    private int siguiente(Caracteristica c) {
        for (int i = 1; i <= 5; i++) {
            if (!c.nivelCompletado(i)) {
                return i;
            }
        }
        return 0;
    }
}
