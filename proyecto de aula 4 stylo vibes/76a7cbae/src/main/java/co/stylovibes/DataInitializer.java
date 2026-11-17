package co.stylovibes;

import co.stylovibes.dominio.Contenido;
import co.stylovibes.dominio.ContenidoRepository;
import co.stylovibes.dominio.Rol;
import co.stylovibes.dominio.SeccionContenido;
import co.stylovibes.dominio.Usuario;
import co.stylovibes.dominio.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private static final String ALFABETO = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";

    private final ContenidoRepository contenidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(ContenidoRepository contenidoRepository, UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder) {
        this.contenidoRepository = contenidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        sembrarContenidos();
        actualizarPortadasMixtas();
        sembrarAdministrador();
    }

    private void sembrarContenidos() {
        contenido("hero", "Tu estilo, tu esencia", SeccionContenido.INICIO, 0, "/img/hero.png",
                "Un espacio juvenil para descubrir lo que te hace único. Cinco niveles de diagnóstico, "
                        + "recomendaciones con cariño y un catálogo hecho a tu medida.");
        contenido("bienvenida", "Un diagnóstico sin reglas rígidas", SeccionContenido.INICIO, 1, "/img/catalogo-mixto.png",
                "Aquí no hay cuerpos ni rostros equivocados. Respondemos preguntas sencillas y te damos ideas "
                        + "para que te sientas cómodo, seguro y con ganas de expresarte.");
        contenido("nivel-1", "Nivel 1 · Tipo de rostro", SeccionContenido.NIVELES, 1, "/img/nivel-1-mixto.png",
                "Descubre si tu rostro es ovalado, redondo, cuadrado, rectangular, en corazón, diamante, "
                        + "triangular u oblongo, y qué cortes, flequillos y accesorios lo favorecen.");
        contenido("nivel-2", "Nivel 2 · Facciones", SeccionContenido.NIVELES, 2, "/img/nivel-2-mixto.png",
                "Ojos, cejas, nariz, labios, pómulos y mandíbula. Aprende trucos de maquillaje y cuidado facial "
                        + "para resaltar tus rasgos con naturalidad.");
        contenido("nivel-3", "Nivel 3 · Colorimetría", SeccionContenido.NIVELES, 3, "/img/nivel-3-mixto.png",
                "Identifica tu subtono (cálido, frío o neutro) y tu estación (primavera, verano, otoño o invierno) "
                        + "para elegir colores de ropa, maquillaje, cabello y accesorios.");
        contenido("nivel-4", "Nivel 4 · Tipo de cuerpo", SeccionContenido.NIVELES, 4, "/img/nivel-4-mixto.png",
                "Recomendaciones de prendas según tu silueta, con un enfoque respetuoso: la ropa se adapta a ti, "
                        + "nunca al revés.");
        contenido("nivel-5", "Nivel 5 · Estilo personal", SeccionContenido.NIVELES, 5, "/img/nivel-5-mixto.png",
                "Casual, elegante, deportivo, urbano, clásico, minimalista, formal, romántico o bohemio. "
                        + "Combina lo que más te identifica.");
        contenido("catalogo", "Mi estilo, mi esencia", SeccionContenido.CATALOGO, 1, "/img/catalogo-mixto.png",
                "Tu catálogo personal reúne recomendaciones de rostro, facciones, colorimetría, cuerpo, estilo, "
                        + "outfits, accesorios y consejos. Se guarda en tu espacio y puedes consultarlo cuando quieras.");
        contenido("consejos", "Consejos de presentación", SeccionContenido.CONSEJOS, 1, "/img/catalogo-mixto.png",
                "La confianza es tu mejor accesorio. Duerme bien, hidrátate, cuida tu piel y elige prendas "
                        + "cómodas que te permitan moverte con seguridad.");
        contenido("diversidad", "Diversidad y respeto", SeccionContenido.CONSEJOS, 2, "/img/hero.png",
                "Stylo Vibes celebra todos los cuerpos, rostros, identidades y estilos. Nuestras guías son "
                        + "sugerencias para explorar, nunca reglas para encajar.");
    }

    private void actualizarPortadasMixtas() {
        String[] claves = {"bienvenida", "catalogo", "consejos"};
        for (String clave : claves) {
            Contenido c = contenidoRepository.findByClave(clave).orElse(null);
            if (c != null && ("/img/catalogo.png".equals(c.getImagen()) || "/img/catalogo-femenino.png".equals(c.getImagen()))) {
                c.setImagen("/img/catalogo-mixto.png");
                contenidoRepository.save(c);
            }
        }
    }

    private void contenido(String clave, String titulo, SeccionContenido seccion, int orden, String imagen, String texto) {
        if (contenidoRepository.findByClave(clave).isPresent()) {
            return;
        }
        Contenido c = new Contenido();
        c.setClave(clave);
        c.setTitulo(titulo);
        c.setSeccion(seccion);
        c.setOrden(orden);
        c.setImagen(imagen);
        c.setTexto(texto);
        c.setPublicado(true);
        c.setFechaActualizacion(LocalDateTime.now());
        contenidoRepository.save(c);
    }

    private void sembrarAdministrador() {
        if (usuarioRepository.countByRol(Rol.ADMINISTRADOR) > 0) {
            return;
        }
        String email = System.getenv("STYLO_ADMIN_EMAIL");
        String password = System.getenv("STYLO_ADMIN_PASSWORD");
        if (email == null || email.trim().isEmpty()) {
            log.info("No hay cuenta administradora todavía. Puedes crearla desde /admin/configuracion-inicial "
                    + "en este equipo.");
            return;
        }
        if (usuarioRepository.existsByEmailIgnoreCase(email.trim())) {
            return;
        }
        boolean generada = password == null || password.trim().length() < 10;
        if (generada) {
            password = generarPassword();
        }
        Usuario admin = new Usuario();
        admin.setNombre("Administración");
        admin.setApellido("Stylo Vibes");
        admin.setEdad(30);
        admin.setGenero("Prefiero no decirlo");
        admin.setEmail(email.trim().toLowerCase());
        admin.setPasswordHash(passwordEncoder.encode(password));
        admin.setRol(Rol.ADMINISTRADOR);
        usuarioRepository.save(admin);
        if (generada) {
            log.warn("Cuenta administradora creada para {}. Contraseña temporal generada: {} "
                    + "(guárdala y define STYLO_ADMIN_PASSWORD para futuros entornos).", admin.getEmail(), password);
        } else {
            log.info("Cuenta administradora creada para {}.", admin.getEmail());
        }
    }

    private String generarPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(ALFABETO.charAt(random.nextInt(ALFABETO.length())));
        }
        return sb.toString();
    }
}
