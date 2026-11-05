package com.stylovibes.seeder;

import com.stylovibes.entity.*;
import com.stylovibes.repository.*;
import com.stylovibes.util.TextoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/** Categorías, artículos de ejemplo y recomendaciones iniciales. */
@Component
@Order(3)
@RequiredArgsConstructor
@ConditionalOnProperty(name = "stylovibes.seed.enabled", havingValue = "true", matchIfMissing = true)
public class BlogSeeder implements CommandLineRunner {

    private final CategoriaArticuloRepository categoriaRepository;
    private final ArticuloRepository articuloRepository;
    private final RecomendacionRepository recomendacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoCorporalRepository tipoCorporalRepository;
    private final TipoRostroRepository tipoRostroRepository;
    private final ColorimetriaRepository colorimetriaRepository;
    private final EstiloRepository estiloRepository;

    @Value("${stylovibes.seed.admin.email}")
    private String adminEmail;

    @Override
    public void run(String... args) {
        if (categoriaRepository.count() == 0) sembrarBlog();
        if (recomendacionRepository.count() == 0) sembrarRecomendaciones();
    }

    private CategoriaArticulo cat(String nombre, String desc) {
        return categoriaRepository.save(CategoriaArticulo.builder().nombre(nombre).descripcion(desc).build());
    }

    private void articulo(CategoriaArticulo c, Usuario autor, String titulo, String resumen, String contenido) {
        articuloRepository.save(Articulo.builder().titulo(titulo).slug(TextoUtil.slugify(titulo))
                .resumen(resumen).contenido(contenido).categoria(c).autor(autor).publicado(true).build());
    }

    private void sembrarBlog() {
        Usuario autor = usuarioRepository.findByEmailIgnoreCase(adminEmail).orElse(null);
        CategoriaArticulo imagen = cat("Imagen personal", "Cómo comunicas quién eres a través de tu aspecto.");
        CategoriaArticulo color = cat("Colorimetría", "Descubre los tonos que iluminan tu rostro.");
        CategoriaArticulo moda = cat("Moda", "Tendencias, estilos y guardarropa consciente.");
        CategoriaArticulo autoestima = cat("Autoestima", "Vestirte bien empieza por sentirte bien.");
        CategoriaArticulo desarrollo = cat("Desarrollo personal", "Hábitos y mentalidad para crecer con confianza.");

        articulo(imagen, autor, "Qué es la imagen personal y por qué importa",
                "La imagen personal es el mensaje que transmites antes de hablar. Aprende a gestionarla con intención.",
                """
                La imagen personal es el conjunto de elementos visibles —ropa, peinado, postura, cuidado personal— que comunican quién eres y cómo quieres ser percibido. No se trata de aparentar, sino de que tu exterior sea coherente con tu interior.

                Una imagen cuidada influye en la primera impresión, en la seguridad con la que te presentas y en las oportunidades académicas, laborales y sociales que encuentras. Y lo mejor: es una habilidad que se aprende, no un privilegio.

                En StyloVibes combinamos cuatro herramientas para ayudarte: la tipología corporal, el visagismo, la colorimetría y la exploración de estilos. Ninguna impone reglas; todas te dan información para decidir mejor.""");

        articulo(color, autor, "Colorimetría para principiantes: las 4 estaciones",
                "Primavera, Verano, Otoño o Invierno: una guía rápida para identificar tu paleta ideal.",
                """
                La colorimetría clasifica a las personas en cuatro estaciones según el subtono de la piel (cálido o frío), la claridad y la intensidad de sus rasgos. Primavera y Otoño son cálidas; Verano e Invierno son frías.

                Para una primera pista, observa las venas de tu muñeca (verdosas suelen indicar subtono cálido; azuladas, frío), qué metal te favorece más (oro o plata) y cómo reacciona tu piel al sol.

                Recuerda que la colorimetría es una guía, no una prohibición: puedes usar cualquier color si lo llevas lejos del rostro o equilibras con accesorios de tu paleta. Prueba nuestro test de colorimetría para obtener tu resultado.""");

        articulo(moda, autor, "Armar un guardarropa cápsula en 10 prendas",
                "Menos prendas, más combinaciones: una estrategia práctica para vestirte bien sin gastar de más.",
                """
                Un guardarropa cápsula reúne pocas prendas versátiles que combinan entre sí. Empieza por elegir una paleta base de tres colores neutros y uno o dos de acento, según tu estación de color.

                Una cápsula inicial podría incluir: dos camisetas básicas, una camisa, un suéter, un blazer o chaqueta, un jean, un pantalón de vestir, un calzado cómodo y uno más formal. Con estas piezas se pueden armar más de veinte looks.

                Antes de comprar pregúntate: ¿combina con al menos tres prendas que ya tengo? ¿favorece mi tipo de cuerpo? ¿me hace sentir yo? Si la respuesta es sí, entra en tu cápsula.""");

        articulo(autoestima, autor, "Vestirte para ti: moda y autoestima",
                "La ropa no cambia quien eres, pero sí puede cambiar cómo te sientes. Aprende a elegir desde el cariño propio.",
                """
                Cuando eliges ropa pensando en lo que otros esperan, te alejas de ti. Cuando eliges desde la comodidad, el gusto y el cuidado propio, la ropa se vuelve una forma de expresión y de respeto hacia tu cuerpo.

                Algunas ideas: viste el cuerpo que tienes hoy, no el que deseas tener; busca tallas que te queden bien en lugar de forzar una talla; y celebra lo que te gusta de ti en lugar de esconder lo que no.

                La asesoría de imagen no busca corregirte. Busca darte herramientas para resaltar tus fortalezas y sentirte seguro en cada espacio.""");

        articulo(desarrollo, autor, "Hábitos de imagen que construyen confianza",
                "Pequeñas rutinas diarias que fortalecen tu seguridad y tu presencia.",
                """
                La confianza se construye con acciones pequeñas y constantes. Planear tu ropa la noche anterior, mantener una rutina sencilla de cuidado personal y cuidar tu postura reducen el estrés y mejoran cómo te percibes.

                Dedica unos minutos a la semana a revisar tu armario: separa lo que usas, lo que necesita arreglo y lo que ya no te representa. Así, cada mañana tendrás menos decisiones y más opciones que te gustan.

                Por último, recuerda que tu estilo evoluciona contigo. Experimenta, observa qué te hace sentir bien y ajusta. El objetivo no es la perfección, sino la coherencia.""");
    }

    // -------------------------------------------------------- Recomendaciones
    private void sembrarRecomendaciones() {
        List<Recomendacion> lista = new ArrayList<>();

        for (Colorimetria c : colorimetriaRepository.findAll()) {
            lista.add(Recomendacion.builder().colorimetria(c)
                    .titulo("Cómo aprovechar tu paleta de " + c.getNombre())
                    .contenido("Usa tus colores ideales cerca del rostro (cuellos, bufandas, aretes) y deja los tonos a evitar para prendas inferiores o accesorios alejados de la cara.")
                    .build());
        }

        TipoRostro corazon = rostro("Corazón");
        TipoRostro redondo = rostro("Redondo");
        lista.add(Recomendacion.builder().tipoRostro(corazon)
                .titulo("Maquillaje y peinado para rostro corazón")
                .contenido("Equilibra la frente amplia con flequillo lateral y añade volumen a la altura de la mandíbula. Un labial protagonista atrae la mirada hacia la parte inferior del rostro.")
                .build());
        lista.add(Recomendacion.builder().tipoRostro(redondo)
                .titulo("Alarga visualmente un rostro redondo")
                .contenido("Las capas largas, los escotes en V y los accesorios alargados crean líneas verticales que estilizan.")
                .build());

        TipoCorporal reloj = cuerpo("Reloj de arena", "FEMENINO");
        TipoCorporal trapecio = cuerpo("Trapecio", "MASCULINO");
        lista.add(Recomendacion.builder().tipoCorporal(reloj)
                .titulo("Looks que celebran la cintura")
                .contenido("Un vestido cruzado, una falda lápiz con blusa metida y un blazer entallado resaltan tus proporciones sin esfuerzo.")
                .build());
        lista.add(Recomendacion.builder().tipoCorporal(trapecio)
                .titulo("Sastrería ligera para silueta trapecio")
                .contenido("Camisas bien ajustadas al torso y pantalones chinos de corte recto crean un look pulido y cómodo para el día a día.")
                .build());

        Estilo oldMoney = estilo("Old Money");
        Estilo y2k = estilo("Y2K");
        Colorimetria otono = colorimetria("Otoño");
        lista.add(Recomendacion.builder().estilo(oldMoney).colorimetria(otono)
                .titulo("Old Money en tonos otoño")
                .contenido("Combina suéter crema, pantalón camel y mocasines de cuero: calidez, elegancia y atemporalidad en una sola paleta.")
                .build());
        lista.add(Recomendacion.builder().estilo(y2k)
                .titulo("Y2K sin exagerar")
                .contenido("Elige una sola pieza protagonista (baby tee, jeans de tiro bajo o bolso baguette) y equilibra el resto con básicos.")
                .build());

        recomendacionRepository.saveAll(lista);
    }

    private TipoRostro rostro(String nombre) {
        return tipoRostroRepository.findAll().stream().filter(r -> r.getNombre().equals(nombre)).findFirst().orElse(null);
    }

    private TipoCorporal cuerpo(String nombre, String genero) {
        return tipoCorporalRepository.findAll().stream()
                .filter(t -> t.getNombre().equals(nombre) && t.getGenero().name().equals(genero)).findFirst().orElse(null);
    }

    private Estilo estilo(String nombre) {
        return estiloRepository.findAll().stream().filter(e -> e.getNombre().equals(nombre)).findFirst().orElse(null);
    }

    private Colorimetria colorimetria(String nombre) {
        return colorimetriaRepository.findByNombreIgnoreCase(nombre).orElse(null);
    }
}
