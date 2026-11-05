package com.stylovibes.seeder;

import com.stylovibes.entity.*;
import com.stylovibes.entity.enums.Genero;
import com.stylovibes.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/** Contenido educativo inicial: cuerpos, rostros, estaciones de color y estilos. */
@Component
@Order(2)
@RequiredArgsConstructor
@ConditionalOnProperty(name = "stylovibes.seed.enabled", havingValue = "true", matchIfMissing = true)
public class CatalogoSeeder implements CommandLineRunner {

    private final TipoCorporalRepository tipoCorporalRepository;
    private final TipoRostroRepository tipoRostroRepository;
    private final ColorimetriaRepository colorimetriaRepository;
    private final EstiloRepository estiloRepository;

    @Override
    public void run(String... args) {
        if (tipoCorporalRepository.count() == 0) sembrarCuerpos();
        if (tipoRostroRepository.count() == 0) sembrarRostros();
        if (colorimetriaRepository.count() == 0) sembrarColorimetrias();
        if (estiloRepository.count() == 0) sembrarEstilos();
    }

    private static String l(String... items) {
        return String.join(";", items);
    }

    private TipoCorporal cuerpo(String nombre, Genero g, String desc, String fort, String vest, String rec, String evitar) {
        return TipoCorporal.builder().nombre(nombre).genero(g).descripcion(desc).fortalezas(fort)
                .recomendacionesVestuario(vest).prendasRecomendadas(rec).prendasEvitar(evitar).build();
    }

    // ------------------------------------------------------------------ CUERPOS
    private void sembrarCuerpos() {
        Genero f = Genero.FEMENINO;
        Genero m = Genero.MASCULINO;
        tipoCorporalRepository.saveAll(List.of(
            cuerpo("Reloj de arena", f,
                "Hombros y caderas de ancho similar con una cintura claramente marcada.",
                l("Proporciones equilibradas", "Cintura definida", "Versatilidad con prendas entalladas"),
                l("Marca la cintura con cinturones o cortes entallados", "Elige telas fluidas que acompañen la silueta", "Los escotes en V o cruzados equilibran el busto"),
                l("Vestido cruzado", "Falda lápiz", "Pantalón de tiro alto", "Blazer entallado", "Top cache-coeur"),
                l("Prendas excesivamente holgadas", "Cortes rectos sin definición", "Tejidos rígidos que añaden volumen")),
            cuerpo("Triángulo", f,
                "Hombros más estrechos que las caderas; el volumen se concentra en la parte inferior.",
                l("Cintura definida", "Piernas estilizadas", "Parte superior delicada"),
                l("Aporta volumen y color en la parte superior", "Usa prendas inferiores lisas y oscuras", "Los escotes barco o bardot amplían visualmente los hombros"),
                l("Blusa con hombreras suaves", "Cuello barco", "Falda en A", "Pantalón recto oscuro", "Chaqueta estructurada"),
                l("Bolsillos voluminosos en la cadera", "Pantalón pitillo muy claro", "Faldas con mucho volumen en cadera")),
            cuerpo("Triángulo invertido", f,
                "Hombros más anchos que las caderas, con presencia en la parte superior.",
                l("Hombros fuertes y elegantes", "Piernas estilizadas", "Postura segura"),
                l("Suaviza los hombros con escotes en V", "Añade volumen en la parte inferior", "Usa colores claros o estampados abajo"),
                l("Cuello en V", "Falda con vuelo", "Pantalón palazzo", "Vestido línea A", "Top de tirantes finos"),
                l("Hombreras", "Cuello barco", "Mangas abullonadas", "Estampados grandes en la parte superior")),
            cuerpo("Rectángulo", f,
                "Hombros, cintura y caderas de medidas parecidas, con silueta recta y atlética.",
                l("Silueta atlética", "Facilidad para estilos oversize", "Versatilidad andrógina"),
                l("Crea curvas con cinturones y cortes marcados", "Juega con capas y volúmenes", "Peplum y texturas aportan forma"),
                l("Peplum", "Cinturón ancho", "Vestido con frunces", "Falda con volumen", "Blazer cruzado"),
                l("Vestidos rectos sin definición", "Prendas totalmente rígidas", "Total look monocromo sin cinturón")),
            cuerpo("Ovalado", f,
                "Mayor volumen en el torso y la zona media, con piernas y busto como protagonistas.",
                l("Busto y piernas protagonistas", "Presencia y calidez", "Estilo cómodo y elegante"),
                l("Los cortes imperio estilizan", "El escote en V alarga el torso", "Elige telas fluidas que no se adhieran"),
                l("Vestido imperio", "Túnica larga", "Pantalón recto de tiro medio", "Blazer largo abierto", "Camisa fluida"),
                l("Cinturones apretados en la cintura", "Telas muy adheridas", "Estampados grandes en el abdomen")),
            cuerpo("Trapecio", m,
                "Hombros más anchos que la cintura, con proporciones clásicas y equilibradas.",
                l("Proporciones clásicas", "Gran versatilidad", "Hombros marcados"),
                l("Las prendas entalladas funcionan muy bien", "Camisas ajustadas al torso", "Pantalón recto o slim"),
                l("Camisa slim fit", "Blazer estructurado", "Polo ajustado", "Chinos", "Jeans rectos"),
                l("Ropa excesivamente holgada", "Mangas demasiado largas", "Estampados muy recargados")),
            cuerpo("Triángulo invertido", m,
                "Hombros y pecho notablemente más anchos que la cintura y las caderas (silueta en V).",
                l("Torso atlético", "Silueta en V", "Presencia fuerte"),
                l("Equilibra con pantalones de más volumen", "Evita añadir volumen en los hombros", "Prefiere tonos sobrios en la parte superior"),
                l("Pantalón recto o relaxed", "Camiseta de cuello redondo", "Cárdigan", "Camisa de corte recto", "Jeans straight"),
                l("Hombreras", "Pantalón pitillo ajustadísimo", "Rayas horizontales anchas")),
            cuerpo("Rectángulo", m,
                "Hombros, cintura y caderas de anchos similares; silueta recta y equilibrada.",
                l("Silueta equilibrada", "Se ve bien con capas", "Funciona con sastrería y oversize"),
                l("Añade estructura en los hombros", "Combina capas y texturas", "Las rayas horizontales sutiles aportan volumen"),
                l("Chaqueta con estructura", "Overshirt", "Cuello redondo", "Suéter de punto grueso", "Bomber"),
                l("Ropa pegada sin estructura", "Prendas totalmente lisas y rígidas", "Pantalones demasiado cortos")),
            cuerpo("Ovalado", m,
                "Mayor volumen en la zona media del torso, con aspecto cálido y cercano.",
                l("Aspecto cálido y cercano", "Comodidad", "Presencia"),
                l("Las líneas verticales alargan la figura", "Tonos oscuros y monocromo estilizan", "Elige tejidos con buena caída"),
                l("Camisa recta sin pinzas", "Blazer sin acolchado", "Pantalón de tiro medio", "Chaqueta larga", "Cuello en V"),
                l("Camisas muy ajustadas", "Estampados horizontales", "Cinturones muy anchos", "Telas brillantes pegadas"))
        ));
    }

    // ------------------------------------------------------------------ ROSTROS
    private TipoRostro rostro(String nombre, String car, String peinados, String accesorios, String gafas, String general) {
        return TipoRostro.builder().nombre(nombre).caracteristicas(car).peinadosRecomendados(peinados)
                .accesoriosRecomendados(accesorios).gafasRecomendadas(gafas).recomendacionesGenerales(general).build();
    }

    private void sembrarRostros() {
        tipoRostroRepository.saveAll(List.of(
            rostro("Ovalado",
                "Frente ligeramente más ancha que la barbilla, pómulos suaves y proporciones equilibradas.",
                l("Casi cualquier corte le favorece", "Bob largo", "Capas suaves", "Recogidos altos", "Flequillo cortina"),
                l("Aros medianos", "Collares cortos", "Diademas y gorros", "Pañuelos"),
                l("Aviador", "Wayfarer", "Redondas", "Cat-eye suaves"),
                l("Mantén el equilibrio: puedes experimentar con confianza", "En maquillaje, realza mirada o labios sin saturar ambos")),
            rostro("Redondo",
                "Ancho y largo similares, mejillas llenas y barbilla suave.",
                l("Capas largas", "Lob asimétrico", "Volumen en la corona", "Raya lateral", "Flequillo lateral"),
                l("Pendientes alargados", "Collares largos en V", "Sombreros de ala mediana"),
                l("Rectangulares", "Cat-eye angulares", "Wayfarer"),
                l("Crea líneas verticales para estilizar", "Un contorno suave bajo los pómulos aporta definición")),
            rostro("Cuadrado",
                "Mandíbula marcada, frente amplia y ángulos bien definidos.",
                l("Ondas suaves", "Capas largas", "Flequillo ladeado", "Pixie texturizado", "Raya lateral"),
                l("Pendientes redondos o de gota", "Collares con formas curvas", "Diademas anchas y suaves"),
                l("Redondas", "Ovaladas", "Aviador"),
                l("Suaviza los ángulos con curvas", "Evita cortes rectos a la altura de la mandíbula")),
            rostro("Corazón",
                "Frente amplia, pómulos altos y barbilla estrecha.",
                l("Flequillo lateral", "Bob a la altura del mentón", "Ondas desde las orejas", "Capas con volumen abajo", "Raya lateral profunda"),
                l("Pendientes de gota o campana", "Collares cortos", "Pendientes más anchos abajo"),
                l("Montura inferior ancha", "Redondas", "Aviador", "Sin montura"),
                l("Equilibra el volumen hacia la mandíbula", "Los labios pueden ser protagonistas")),
            rostro("Diamante",
                "Pómulos prominentes con frente y barbilla más estrechas.",
                l("Flequillo completo", "Volumen en frente y mandíbula", "Bob con capas", "Ondas suaves", "Recogido con mechones sueltos"),
                l("Pendientes de botón", "Aros pequeños", "Gargantillas"),
                l("Ovaladas", "Cat-eye", "Sin montura", "Montura superior marcada"),
                l("Suaviza la zona de los pómulos", "Resalta la mirada")),
            rostro("Alargado",
                "Rostro más largo que ancho, frente alta y barbilla larga.",
                l("Flequillo recto", "Volumen lateral", "Ondas a la altura de las mejillas", "Bob con volumen", "Raya al medio con ondas"),
                l("Pendientes anchos o aros grandes", "Chokers", "Sombreros de ala ancha"),
                l("Oversize", "Cuadradas", "Monturas anchas y altas", "Wayfarer grande"),
                l("Crea sensación de ancho, no de largo", "Evita el volumen excesivo en la corona"))
        ));
    }

    // ------------------------------------------------------------------ COLORIMETRÍA
    private Colorimetria color(String nombre, String desc, String paleta, String ideales, String evitar, String ejemplos) {
        return Colorimetria.builder().nombre(nombre).descripcion(desc).paletaColores(paleta)
                .coloresIdeales(ideales).coloresEvitar(evitar).ejemplos(ejemplos).build();
    }

    private void sembrarColorimetrias() {
        colorimetriaRepository.saveAll(List.of(
            color("Primavera",
                "Subtono cálido, claro y luminoso. Te favorecen los colores frescos, vivos y con base dorada.",
                l("Coral:#FF7F6B", "Durazno:#FFCBA4", "Amarillo mantequilla:#FFE680", "Verde manzana:#A8D86E", "Turquesa:#4FD1C5", "Marfil:#FFF8E7"),
                l("Coral:#FF7F6B", "Durazno:#FFCBA4", "Turquesa:#4FD1C5", "Verde menta:#98E3B8", "Camel claro:#D9B38C"),
                l("Negro:#0D0D0D", "Gris frío:#808A95", "Borgoña:#6B1E3A", "Azul marino:#1B2A4A"),
                l("Blusa coral con jeans claros", "Vestido verde manzana con sandalias doradas", "Blazer camel con top marfil")),
            color("Verano",
                "Subtono frío, suave y delicado. Brillan los tonos empolvados, azulados y de baja saturación.",
                l("Rosa empolvado:#E8B4C8", "Lavanda:#C3B1E1", "Azul cielo:#A7C7E7", "Gris perla:#C9CED6", "Malva:#B784A7", "Menta suave:#B5E3D0"),
                l("Rosa empolvado:#E8B4C8", "Lavanda:#C3B1E1", "Azul cielo:#A7C7E7", "Gris azulado:#8DA0B6", "Malva:#B784A7"),
                l("Naranja:#FF7F00", "Mostaza:#D4A017", "Marrón cálido:#8B5A2B", "Negro puro:#000000"),
                l("Camisa azul cielo con pantalón gris perla", "Vestido lavanda con accesorios plateados", "Suéter malva con jeans claros")),
            color("Otoño",
                "Subtono cálido, profundo y terroso. Favorecen los colores ricos, especiados y naturales.",
                l("Terracota:#C8643C", "Mostaza:#D4A017", "Verde oliva:#6B7A2F", "Chocolate:#5C3A21", "Burdeos cálido:#7B2D26", "Crema:#F3E5C8"),
                l("Terracota:#C8643C", "Mostaza:#D4A017", "Verde oliva:#6B7A2F", "Chocolate:#5C3A21", "Crema:#F3E5C8"),
                l("Rosa chicle:#FF69B4", "Azul hielo:#CFE8FF", "Fucsia:#E6007E", "Gris plata:#B5B9BF"),
                l("Suéter mostaza con pantalón chocolate", "Vestido terracota con botines de cuero", "Chaqueta verde oliva con top crema")),
            color("Invierno",
                "Subtono frío, intenso y de alto contraste. Resaltan los colores puros, saturados y los fuertes contrastes.",
                l("Negro:#0D0D0D", "Blanco puro:#FFFFFF", "Azul real:#1F4FD8", "Fucsia:#D6006F", "Esmeralda:#0B7A4B", "Rojo cereza:#B0122F"),
                l("Negro:#0D0D0D", "Blanco puro:#FFFFFF", "Azul real:#1F4FD8", "Esmeralda:#0B7A4B", "Fucsia:#D6006F"),
                l("Beige:#D9C3A0", "Naranja quemado:#CC5500", "Mostaza:#D4A017", "Camel:#C19A6B"),
                l("Blazer negro con camisa blanca", "Vestido azul real con accesorios plateados", "Abrigo esmeralda con jeans oscuros"))
        ));
    }

    // ------------------------------------------------------------------ ESTILOS
    private Estilo estilo(String nombre, String desc, String car, String ej, String prendas, String insp, String color) {
        return Estilo.builder().nombre(nombre).descripcion(desc).caracteristicas(car).ejemplos(ej)
                .prendasRepresentativas(prendas).inspiracionVisual(insp).colorPrincipal(color).build();
    }

    private void sembrarEstilos() {
        estiloRepository.saveAll(List.of(
            estilo("Old Money",
                "Elegancia discreta y atemporal inspirada en la alta sociedad clásica: calidad por encima de logos.",
                l("Colores neutros y sobrios", "Tejidos nobles (lana, cachemir, lino)", "Cortes clásicos y bien ajustados", "Sin logos visibles"),
                l("Suéter de cachemir sobre los hombros", "Blazer azul marino con pantalón beige", "Vestido midi de lino"),
                l("Blazer azul marino", "Camisa oxford", "Suéter de cachemir", "Mocasines", "Pantalón de pinzas", "Gabardina beige"),
                "Veleros, clubes de golf y veranos en la costa mediterránea, con paleta crema, marino y verde bosque.", "#C9B79C"),
            estilo("Minimalista",
                "Menos es más: líneas limpias, paleta neutra y básicos de buena calidad.",
                l("Paleta monocromática", "Siluetas simples", "Pocos accesorios", "Guardarropa cápsula"),
                l("Total look blanco y beige", "Camiseta básica, pantalón recto y sneakers blancos", "Abrigo largo gris"),
                l("Camiseta blanca", "Pantalón recto negro", "Blazer oversize", "Sneakers blancos", "Abrigo largo", "Tote bag lisa"),
                "Galerías de arte, arquitectura escandinava y espacios luminosos.", "#D8D4CF"),
            estilo("Streetwear",
                "Moda urbana nacida del skate y la cultura callejera: comodidad, actitud y personalidad.",
                l("Siluetas oversize", "Zapatillas protagonistas", "Gráficos y logos", "Capas y accesorios deportivos"),
                l("Hoodie oversize, cargo y sneakers chunky", "Bomber con camiseta gráfica", "Gorra y riñonera cruzada"),
                l("Hoodie oversize", "Pantalón cargo", "Sneakers chunky", "Camiseta gráfica", "Gorra", "Chaqueta bomber"),
                "Skateparks, grafiti, cultura sneaker y música urbana.", "#9DB4D6"),
            estilo("Coquette",
                "Estética romántica y femenina: lazos, encaje y tonos pastel con un aire delicado y juguetón.",
                l("Lazos y moños", "Encaje y volantes", "Tonos rosa y crema", "Detalles de perlas"),
                l("Blusa de encaje con falda plisada rosa", "Vestido con lazos y balerinas", "Cárdigan corto con collar de perlas"),
                l("Moños para el cabello", "Blusa de encaje", "Mini falda plisada", "Balerinas", "Cárdigan corto", "Collar de perlas"),
                "Ballet, cartas de amor, flores secas y meriendas vintage.", "#F4B8CF"),
            estilo("Cottagecore",
                "Vida rural idealizada: vestidos vaporosos, estampados florales y materiales naturales.",
                l("Estampados florales", "Mangas abullonadas", "Telas naturales (algodón, lino)", "Colores tierra y pastel"),
                l("Vestido floral midi con cesta de mimbre", "Blusa de mangas abullonadas con falda larga", "Cárdigan tejido a mano"),
                l("Vestido floral midi", "Blusa de mangas abullonadas", "Delantal de lino", "Cárdigan tejido", "Botines de cuero", "Sombrero de paja"),
                "Campos, jardines, picnics y cabañas de campo.", "#BFD3A8"),
            estilo("Y2K",
                "Nostalgia de los años 2000: brillo, colores pop, tiro bajo y accesorios divertidos.",
                l("Tiro bajo", "Brillos y metalizados", "Colores pop y rosa", "Accesorios llamativos"),
                l("Jeans de tiro bajo con baby tee", "Mini falda metalizada con top corto", "Gafas pequeñas y bolso baguette"),
                l("Jeans de tiro bajo", "Baby tee", "Mini falda", "Bolso baguette", "Gafas pequeñas", "Chaqueta de mezclilla corta"),
                "Pop de los 2000, revistas juveniles, MTV y estética cibernética.", "#D9B8F0")
        ));
    }
}
