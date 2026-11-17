package co.stylovibes.servicio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Recomendaciones {

    public static class DatosRostro {
        private final String titulo;
        private final String descripcion;
        private final String corte;
        private final String flequillo;
        private final String gafas;
        private final String evitar;

        public DatosRostro(String titulo, String descripcion, String corte, String flequillo,
                           String gafas, String evitar) {
            this.titulo = titulo;
            this.descripcion = descripcion;
            this.corte = corte;
            this.flequillo = flequillo;
            this.gafas = gafas;
            this.evitar = evitar;
        }

        public String getTitulo() { return titulo; }
        public String getDescripcion() { return descripcion; }
        public String getCorte() { return corte; }
        public String getFlequillo() { return flequillo; }
        public String getGafas() { return gafas; }
        public String getEvitar() { return evitar; }
    }

    public static class DatosFaccion {
        private final String titulo;
        private final String descripcion;
        private final List<String> maquillaje;
        private final String cuidado;

        public DatosFaccion(String titulo, String descripcion, List<String> maquillaje, String cuidado) {
            this.titulo = titulo;
            this.descripcion = descripcion;
            this.maquillaje = maquillaje;
            this.cuidado = cuidado;
        }

        public String getTitulo() { return titulo; }
        public String getDescripcion() { return descripcion; }
        public List<String> getMaquillaje() { return maquillaje; }
        public String getCuidado() { return cuidado; }
    }

    public static class Color {
        private final String nombre;
        private final String hex;

        public Color(String nombre, String hex) {
            this.nombre = nombre;
            this.hex = hex;
        }

        public String getNombre() { return nombre; }
        public String getHex() { return hex; }
    }

    public static class DatosColor {
        private final String titulo;
        private final String descripcion;
        private final List<Color> ropa;
        private final List<Color> maquillaje;
        private final List<Color> cabello;
        private final List<Color> accesorios;

        public DatosColor(String titulo, String descripcion, List<Color> ropa, List<Color> maquillaje,
                          List<Color> cabello, List<Color> accesorios) {
            this.titulo = titulo;
            this.descripcion = descripcion;
            this.ropa = ropa;
            this.maquillaje = maquillaje;
            this.cabello = cabello;
            this.accesorios = accesorios;
        }

        public String getTitulo() { return titulo; }
        public String getDescripcion() { return descripcion; }
        public List<Color> getRopa() { return ropa; }
        public List<Color> getMaquillaje() { return maquillaje; }
        public List<Color> getCabello() { return cabello; }
        public List<Color> getAccesorios() { return accesorios; }
        public String getRopaNombres() { return nombres(ropa); }
        public String getMaquillajeNombres() { return nombres(maquillaje); }
        public String getCabelloNombres() { return nombres(cabello); }
        public String getAccesoriosNombres() { return nombres(accesorios); }
    }

    static String nombres(List<Color> colores) {
        StringBuilder sb = new StringBuilder();
        for (Color c : colores) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(c.getNombre());
        }
        return sb.toString();
    }

    public static class DatosCuerpo {
        private final String titulo;
        private final String descripcion;
        private final List<String> prendas;
        private final List<String> evitar;

        public DatosCuerpo(String titulo, String descripcion, List<String> prendas, List<String> evitar) {
            this.titulo = titulo;
            this.descripcion = descripcion;
            this.prendas = prendas;
            this.evitar = evitar;
        }

        public String getTitulo() { return titulo; }
        public String getDescripcion() { return descripcion; }
        public List<String> getPrendas() { return prendas; }
        public List<String> getEvitar() { return evitar; }
    }

    public static class DatosEstilo {
        private final String titulo;
        private final String descripcion;
        private final List<String> claves;
        private final List<String> prendas;
        private final List<String> accesorios;

        public DatosEstilo(String titulo, String descripcion, List<String> claves, List<String> prendas,
                           List<String> accesorios) {
            this.titulo = titulo;
            this.descripcion = descripcion;
            this.claves = claves;
            this.prendas = prendas;
            this.accesorios = accesorios;
        }

        public String getTitulo() { return titulo; }
        public String getDescripcion() { return descripcion; }
        public List<String> getClaves() { return claves; }
        public List<String> getPrendas() { return prendas; }
        public List<String> getAccesorios() { return accesorios; }
        public String getPrendasTexto() { return String.join(", ", prendas); }
        public String getAccesoriosTexto() { return String.join(", ", accesorios); }
    }

    private static final Map<String, DatosRostro> ROSTROS = new LinkedHashMap<String, DatosRostro>();
    private static final Map<String, DatosFaccion> FACCIONES = new LinkedHashMap<String, DatosFaccion>();
    private static final Map<String, DatosColor> ESTACIONES = new LinkedHashMap<String, DatosColor>();
    private static final Map<String, DatosCuerpo> CUERPOS = new LinkedHashMap<String, DatosCuerpo>();
    private static final Map<String, DatosEstilo> ESTILOS = new LinkedHashMap<String, DatosEstilo>();

    static {
        ROSTROS.put("ovalado", new DatosRostro("Rostro óvalado",
                "Tu rostro tiene proporciones equilibradas y una mandíbula suavemente redondeada. Es una forma muy versátil.",
                "Casi cualquier corte te favorece: bob a la mandíbula, melena con capas largas o pixie suave.",
                "Flequillo cortina o lateral para enmarcar los ojos sin recargar.",
                "Gafas rectangulares suaves o de ojo de gato discreto.",
                "Evita cortes que cubran demasiado tu rostro; mantener la frente visible realza tu equilibrio."));
        ROSTROS.put("redondo", new DatosRostro("Rostro redondo",
                "Tus mejillas y contornos son suaves, con un ancho y largo similares. La clave es aportar verticalidad.",
                "Capas largas, bob largo o pixie con volumen en la parte superior.",
                "Flequillo lateral desmechado o cortina largo.",
                "Gafas angulosas o cuadradas para crear contraste.",
                "Evita volumen a los lados y flequillos rectos y gruesos."));
        ROSTROS.put("cuadrado", new DatosRostro("Rostro cuadrado",
                "Tienes una mandíbula definida y ángulos marcados que transmiten fuerza. Suavizarlos es el objetivo.",
                "Capas suaves, bob a la mandíbula o melena ondulada por debajo del mentón.",
                "Flequillo cortina o desmechado que rompa la línea recta.",
                "Gafas redondas u ovaladas.",
                "Evita cortes rectos y muy geométricos que refuercen los ángulos."));
        ROSTROS.put("rectangular", new DatosRostro("Rostro rectangular",
                "Tu rostro es más largo que ancho y con frente y mandíbula amplias. Acortar visualmente es la meta.",
                "Capas a la altura de las mejillas, bob con ondas o media melena texturizada.",
                "Flequillo recto suave o cortina para acortar la frente.",
                "Gafas grandes y redondeadas.",
                "Evita melenas muy largas y lacias que alarguen más el rostro."));
        ROSTROS.put("corazon", new DatosRostro("Rostro en corazón",
                "Tu frente es más amplia y tu mentón se afina en punta. Equilibrar la parte inferior es la clave.",
                "Capas a la altura de la barbilla, bob invertido o melena con movimiento en las puntas.",
                "Flequillo cortina largo o lateral.",
                "Gafas aviador o redondas.",
                "Evita volumen en la parte superior que aumente el ancho de la frente."));
        ROSTROS.put("diamante", new DatosRostro("Rostro diamante",
                "Tus pómulos son la parte más ancha y la frente y el mentón se estrechan. Resaltar tus pómulos es ideal.",
                "Capas laterales, pixie texturizado o media melena con volumen en las mejillas.",
                "Flequillo desmechado o cortina que aporte ancho a la frente.",
                "Gafas de ojo de gato u ovaladas.",
                "Evita cortes que estrechen aún más la frente o el mentón."));
        ROSTROS.put("triangular", new DatosRostro("Rostro triangular",
                "Tu mandíbula es la zona más ancha y la frente algo más estrecha. Aportar volumen arriba equilibra.",
                "Capas en la parte superior, bob con volumen o melena con capas que den amplitud arriba.",
                "Flequillo lateral o cortina.",
                "Gafas grandes rectangulares.",
                "Evita volumen en la mandíbula o cortes que terminen justo en la zona más ancha."));
        ROSTROS.put("oblongo", new DatosRostro("Rostro oblongo",
                "Tu rostro es alargado y estrecho. La meta es acortar y aportar amplitud a los lados.",
                "Bob corto, capas laterales u ondas a la altura de las mejillas.",
                "Flequillo recto o cortina para acortar la longitud.",
                "Gafas grandes y con presencia.",
                "Evita melenas largas sin capas que estiren más el rostro."));
    }

    static {
        FACCIONES.put("ojos_almendrados", new DatosFaccion("Ojos almendrados",
                "Son versátiles y elegantes; cualquier delineado les sienta bien.",
                Arrays.asList("Delineado fino pegado a la línea de las pestañas.",
                        "Sombras neutras con un punto de luz en el centro del párpado.",
                        "Máscara que separe las pestañas."),
                "Usa contorno de ojos hidratante y protector solar diario."));
        FACCIONES.put("ojos_redondos", new DatosFaccion("Ojos redondos",
                "Grandes y luminosos. El delineado alargado aporta equilibrio.",
                Arrays.asList("Delineado que se extienda ligeramente hacia afuera.",
                        "Sombras en tonos medios en la cuenca para dar profundidad.",
                        "Evita delineados muy gruesos en el párpado móvil."),
                "Descansa bien y aplica compresas frías si amaneces con hinchazón."));
        FACCIONES.put("ojos_caidos", new DatosFaccion("Ojos caídos",
                "La parte exterior desciende suavemente, lo que da una mirada dulce.",
                Arrays.asList("Delineado ascendente en el extremo exterior.",
                        "Máscara concentrada en las pestañas externas.",
                        "Sombras claras en el lagrimal para elevar la mirada."),
                "Trabaja la zona con suavidad al desmaquillar para no irritar."));
        FACCIONES.put("ojos_rasgados", new DatosFaccion("Ojos rasgados",
                "Finos y con mucha expresión; el eyeliner es tu aliado.",
                Arrays.asList("Eyeliner gráfico o clásico según la ocasión.",
                        "Sombras en tonos tierra o vino para resaltar la forma.",
                        "Rizado de pestañas para abrir la mirada."),
                "Hidrata la zona y usa protector solar específico para ojos."));
        FACCIONES.put("cejas_rectas", new DatosFaccion("Cejas rectas",
                "Aportan un aire moderno y equilibrado.",
                Arrays.asList("Rellena con trazos suaves siguiendo su forma natural.",
                        "Un toque de gel para peinarlas hacia arriba.",
                        "Limpia solo los vellos fuera de la línea."),
                "Cepíllalas a diario y evita la depilación agresiva."));
        FACCIONES.put("cejas_arqueadas", new DatosFaccion("Cejas arqueadas",
                "Su arco natural enmarca la mirada con elegancia.",
                Arrays.asList("Rellena el arco sin exagerarlo.",
                        "Fija con gel transparente o con color.",
                        "Un punto de iluminación bajo el arco."),
                "Mantén la forma con un repaso cada 3 o 4 semanas."));
        FACCIONES.put("cejas_redondas", new DatosFaccion("Cejas redondeadas",
                "Suavizan el rostro y le dan un aire amable.",
                Arrays.asList("Rellena con lápiz en trazos pequeños.",
                        "Define ligeramente la cola para dar estructura.",
                        "Evita arcos muy marcados."),
                "Hidrata la zona con aceite de almendras o sérum."));
        FACCIONES.put("cejas_gruesas", new DatosFaccion("Cejas gruesas",
                "Son un rasgo fuerte y expresivo; domarlas es cuestión de peinado.",
                Arrays.asList("Peina hacia arriba y fija con gel.",
                        "Rellena solo los huecos necesarios.",
                        "Recorta solo los vellos que salen de la línea."),
                "Cepíllalas y usa un sérum fortalecedor si quieres definirlas más."));
        FACCIONES.put("nariz_recta", new DatosFaccion("Nariz recta",
                "Armoniza con casi cualquier maquillaje.",
                Arrays.asList("Contornea suavemente los laterales con un tono frío.",
                        "Ilumina el puente con un toque discreto.",
                        "Difumina muy bien para un efecto natural."),
                "Limpieza suave y protector solar; usa tiras si hay puntos negros."));
        FACCIONES.put("nariz_respingada", new DatosFaccion("Nariz respingada",
                "Aporta un toque juvenil y simpático al rostro.",
                Arrays.asList("Ilumina la punta para resaltarla.",
                        "Un contorno muy ligero en la base de las narinas.",
                        "Evita contornos oscuros que la acorten."),
                "Hidrata con cuidado y protege del sol."));
        FACCIONES.put("nariz_aguilena", new DatosFaccion("Nariz con curva suave",
                "Su perfil es elegante y con carácter.",
                Arrays.asList("Contorno sutil en la curva del puente.",
                        "Iluminación en el centro para unificar.",
                        "Rostro con rubor difuminado para armonizar."),
                "Mantén la piel limpia e hidratada; el protector solar es clave."));
        FACCIONES.put("nariz_ancha", new DatosFaccion("Nariz ancha",
                "Su base amplia da fuerza al rostro.",
                Arrays.asList("Contornea los laterales desde la base hasta la punta.",
                        "Ilumina una línea fina en el centro.",
                        "Evita brillos fuertes en la punta."),
                "Usa limpiadores suaves y no presiones los poros."));
        FACCIONES.put("labios_finos", new DatosFaccion("Labios finos",
                "Delicados y elegantes; se pueden realzar con volumen visual.",
                Arrays.asList("Delinea ligeramente por fuera de la línea natural.",
                        "Gloss o bálsamo con color en el centro.",
                        "Tonos claros y luminosos."),
                "Exfolia una vez por semana y aplica bálsamo a diario."));
        FACCIONES.put("labios_carnosos", new DatosFaccion("Labios carnosos",
                "Tu volumen natural es protagonista.",
                Arrays.asList("Tonos mate o satinados intensos.",
                        "Difumina el borde para un efecto moderno.",
                        "Evita gloss excesivo si buscas discreción."),
                "Hidrata y protege del sol con bálsamo con FPS."));
        FACCIONES.put("labios_corazon", new DatosFaccion("Labios en corazón",
                "La forma natural es dulce y favorecedora.",
                Arrays.asList("Realza el arco de Cupido con un toque de luz.",
                        "Delinea siguiendo la forma natural.",
                        "Tonos rosados o durazno van muy bien."),
                "Exfoliación suave y bálsamo nocturno."));
        FACCIONES.put("labios_amplios", new DatosFaccion("Labios amplios",
                "Tienes presencia natural; menos es más.",
                Arrays.asList("Bálsamo con color para el día a día.",
                        "Delineado difuminado para ocasiones especiales.",
                        "Evita combinar delineado fuerte y gloss muy brillante."),
                "Hidratación constante para mantenerlos suaves."));
        FACCIONES.put("pomulos_altos", new DatosFaccion("Pómulos altos",
                "Tu estructura es definida y fotogénica.",
                Arrays.asList("Rubor en tono durazno aplicado hacia las sienes.",
                        "Iluminador suave en la parte alta del pómulo.",
                        "Contorno ligero debajo para realzar."),
                "Aplica protector solar en todo el rostro, sin olvidar los pómulos."));
        FACCIONES.put("pomulos_suaves", new DatosFaccion("Pómulos suaves",
                "Tu rostro es amable y redondeado.",
                Arrays.asList("Rubor rosado en círculos suaves.",
                        "Iluminador en el centro de la mejilla.",
                        "Contorno muy sutil para aportar estructura."),
                "Masaje facial suave para activar la circulación."));
        FACCIONES.put("pomulos_marcados", new DatosFaccion("Pómulos marcados",
                "Los ángulos de tu rostro son muy expresivos.",
                Arrays.asList("Rubor difuminado en tonos cálidos.",
                        "Iluminador discreto para no recargar.",
                        "Evita contornos fuertes que acentúen los ángulos."),
                "Hidrata bien para mantener la piel luminosa."));
        FACCIONES.put("mandibula_definida", new DatosFaccion("Mandíbula definida",
                "Transmite fuerza y estructura.",
                Arrays.asList("Rubor difuminado hacia la mandíbula.",
                        "Contorno ligero en el borde para suavizar.",
                        "Iluminación en el centro del rostro."),
                "Usa protector solar y limpia con suavidad la zona."));
        FACCIONES.put("mandibula_suave", new DatosFaccion("Mandíbula suave",
                "Tu rostro tiene líneas redondeadas y armónicas.",
                Arrays.asList("Rubor en las mejillas para dar dimensión.",
                        "Contorno suave bajo la mandíbula.",
                        "Iluminador en el mentón."),
                "Masaje con crema hidratante en movimientos ascendentes."));
        FACCIONES.put("mandibula_angulosa", new DatosFaccion("Mandíbula angulosa",
                "Tus ángulos son un rasgo distintivo.",
                Arrays.asList("Rubor cálido difuminado en diagonal.",
                        "Evita contornos oscuros en la mandíbula.",
                        "Iluminación central para equilibrar."),
                "Hidrata y protege la piel para mantenerla uniforme."));
    }

    static {
        ESTACIONES.put("primavera", new DatosColor("Primavera",
                "Eres luminoso y cálido. Los colores vivos y frescos hacen brillar tu piel.",
                Arrays.asList(new Color("Coral", "#FF7F6B"), new Color("Amarillo miel", "#F9D65C"),
                        new Color("Verde menta", "#9FE0C0"), new Color("Turquesa", "#58C9C1"),
                        new Color("Durazno", "#FFC89B")),
                Arrays.asList(new Color("Durazno", "#F7B08B"), new Color("Coral", "#F07C66"),
                        new Color("Dorado", "#D9A441")),
                Arrays.asList(new Color("Cobrizo", "#B5623C"), new Color("Dorado", "#C9A173"),
                        new Color("Miel", "#A9744F")),
                Arrays.asList(new Color("Oro", "#D4AF37"), new Color("Coral", "#FF7F6B"),
                        new Color("Turquesa", "#58C9C1"))));
        ESTACIONES.put("verano", new DatosColor("Verano",
                "Eres suave y frío. Los tonos empolvados y frescos te dan luminosidad natural.",
                Arrays.asList(new Color("Lavanda", "#C3B4E4"), new Color("Azul cielo", "#A9CFEA"),
                        new Color("Rosa cuarzo", "#F3C6D3"), new Color("Gris perla", "#D8DEE4"),
                        new Color("Verde salvia", "#B7CDBE")),
                Arrays.asList(new Color("Rosa frío", "#E9A7B8"), new Color("Malva", "#B98CA8"),
                        new Color("Plata", "#C9CDD4")),
                Arrays.asList(new Color("Cenizo", "#8E8A86"), new Color("Rubio frío", "#C7B49A"),
                        new Color("Castaño claro", "#8B6B57")),
                Arrays.asList(new Color("Plata", "#C0C6CE"), new Color("Perlas", "#F1EDE6"),
                        new Color("Rosa", "#F3C6D3"))));
        ESTACIONES.put("otono", new DatosColor("Otoño",
                "Eres cálido y profundo. Los tonos tierra y especiados realzan tu piel.",
                Arrays.asList(new Color("Terracota", "#C16B4F"), new Color("Mostaza", "#D9A441"),
                        new Color("Verde oliva", "#8A9A5B"), new Color("Café", "#7B5B45"),
                        new Color("Camello", "#C9A173")),
                Arrays.asList(new Color("Ladrillo", "#B5503A"), new Color("Bronce", "#B07B4F"),
                        new Color("Dorado", "#C9A24A")),
                Arrays.asList(new Color("Castaño cálido", "#7A4E2D"), new Color("Cobrizo", "#A9542F"),
                        new Color("Chocolate", "#5A3B28")),
                Arrays.asList(new Color("Oro viejo", "#B08D57"), new Color("Bronce", "#A97142"),
                        new Color("Madera", "#8B5E3C"))));
        ESTACIONES.put("invierno", new DatosColor("Invierno",
                "Eres frío e intenso. Los colores profundos y contrastados te favorecen.",
                Arrays.asList(new Color("Azul rey", "#2E4A8F"), new Color("Negro", "#232323"),
                        new Color("Blanco puro", "#FAFAFA"), new Color("Fucsia", "#C2185B"),
                        new Color("Esmeralda", "#0F6B52")),
                Arrays.asList(new Color("Rojo", "#B3202E"), new Color("Vino", "#6E1F2E"),
                        new Color("Plata", "#C9CDD4")),
                Arrays.asList(new Color("Negro", "#1F1F1F"), new Color("Castaño oscuro", "#3E2A20"),
                        new Color("Platino", "#D8D3C8")),
                Arrays.asList(new Color("Plata", "#C0C6CE"), new Color("Acero", "#8E9AA6"),
                        new Color("Cristal", "#DDE5EC"))));
    }

    static {
        CUERPOS.put("femenino_reloj", new DatosCuerpo("Reloj de arena",
                "Hombros y caderas equilibrados con cintura marcada. La meta es celebrar tus curvas.",
                Arrays.asList("Vestidos y faldas que sigan la línea del cuerpo.",
                        "Jeans de tiro alto y cinturón para destacar la cintura.",
                        "Tops ajustados con blazers entallados."),
                Arrays.asList("Prendas demasiado holgadas que oculten la cintura.",
                        "Cortes rectos rígidos que escondan tu silueta.")));
        CUERPOS.put("femenino_triangulo", new DatosCuerpo("Triángulo",
                "Caderas más amplias que hombros. La meta es equilibrar resaltando la parte superior.",
                Arrays.asList("Tops con detalle en hombros y escotes.",
                        "Blazers estructurados y chaquetas cortas.",
                        "Pantalones rectos oscuros y faldas evasé."),
                Arrays.asList("Volumen extra en caderas y bolsillos grandes.",
                        "Prendas muy ajustadas en la zona baja.")));
        CUERPOS.put("femenino_triangulo_invertido", new DatosCuerpo("Triángulo invertido",
                "Hombros más amplios que caderas. La meta es aportar volumen a la parte inferior.",
                Arrays.asList("Pantalones anchos, palazzo o faldas plisadas.",
                        "Tops sencillos y escotes en V.",
                        "Chaquetas entalladas sin hombreras."),
                Arrays.asList("Hombreras y cuellos muy anchos.",
                        "Prendas que aumenten el ancho de los hombros.")));
        CUERPOS.put("femenino_rectangulo", new DatosCuerpo("Rectángulo",
                "Hombros y caderas similares con poca cintura marcada. La meta es crear curvas visuales.",
                Arrays.asList("Cinturones y faldas con vuelo.",
                        "Capas, nudos y texturas en el torso.",
                        "Vestidos con pinzas o fruncidos."),
                Arrays.asList("Prendas rectas y rígidas sin forma.",
                        "Looks totalmente ajustados de un solo tono.")));
        CUERPOS.put("femenino_ovalado", new DatosCuerpo("Ovalado",
                "La zona abdominal es la más amplia. La meta es alargar la silueta con fluidez.",
                Arrays.asList("Vestidos rectos y fluidos.",
                        "Escotes en V y prendas con caída.",
                        "Pantalones de tiro medio con pierna recta."),
                Arrays.asList("Prendas ceñidas en el abdomen.",
                        "Cinturones anchos sobre la cintura.")));
        CUERPOS.put("masculino_trapecio", new DatosCuerpo("Trapecio",
                "Hombros anchos con cintura definida; una estructura naturalmente atlética.",
                Arrays.asList("Camisetas y camisas entalladas.",
                        "Chaquetas y blazers estructurados.",
                        "Jeans rectos o slim."),
                Arrays.asList("Ropa demasiado holgada que esconda la estructura.",
                        "Capas excesivas en el torso.")));
        CUERPOS.put("masculino_rectangulo", new DatosCuerpo("Rectángulo",
                "Hombros y cintura similares. La meta es aportar estructura y movimiento.",
                Arrays.asList("Capas ligeras: camisa abierta, jerséis texturizados.",
                        "Blazers y chaquetas de hombro suave.",
                        "Pantalones con buena caída."),
                Arrays.asList("Prendas muy ajustadas sin estructura.",
                        "Looks completamente lacios y planos.")));
        CUERPOS.put("masculino_triangulo", new DatosCuerpo("Triángulo",
                "Cintura o abdomen más amplios que los hombros. La meta es equilibrar con capas superiores.",
                Arrays.asList("Camisas estructuradas y chaquetas abiertas.",
                        "Estampados y texturas en la parte superior.",
                        "Pantalones oscuros y rectos."),
                Arrays.asList("Pantalones muy ajustados.",
                        "Camisetas largas y ajustadas en el abdomen.")));
        CUERPOS.put("masculino_triangulo_invertido", new DatosCuerpo("Triángulo invertido",
                "Hombros amplios y caderas estrechas. La meta es equilibrar con la parte inferior.",
                Arrays.asList("Pantalones rectos o ligeramente amplios.",
                        "Tops sencillos sin hombreras.",
                        "Chaquetas de línea limpia."),
                Arrays.asList("Hombreras y cuellos muy anchos.",
                        "Prendas que aumenten el volumen de los hombros.")));
        CUERPOS.put("masculino_ovalado", new DatosCuerpo("Ovalado",
                "Mayor volumen en la zona media. La meta es crear líneas verticales y fluidez.",
                Arrays.asList("Camisas abiertas y blazers de una sola línea.",
                        "Prendas fluidas y escotes en V.",
                        "Pantalones rectos de tiro medio."),
                Arrays.asList("Cinturas muy marcadas y prendas ceñidas.",
                        "Estampados grandes en la zona media.")));
    }

    static {
        ESTILOS.put("casual", new DatosEstilo("Casual",
                "Cómodo, relajado y versátil para el día a día.",
                Arrays.asList("Comodidad sin perder intención.", "Mezcla de básicos y denim.",
                        "Colores neutros con acentos suaves."),
                Arrays.asList("Jeans rectos o mom fit.", "Camisetas de algodón.",
                        "Zapatillas limpias y sudaderas ligeras."),
                Arrays.asList("Mochila o bandolera.", "Gorra discreta.", "Reloj sencillo.")));
        ESTILOS.put("elegante", new DatosEstilo("Elegante",
                "Sofisticado y cuidado, con atención a los detalles.",
                Arrays.asList("Líneas limpias y buena caída.", "Paleta reducida y armónica.",
                        "Accesorios que suman sin recargar."),
                Arrays.asList("Blazer entallado.", "Pantalón de vestir o falda midi.",
                        "Camisa de seda o blusa fluida."),
                Arrays.asList("Reloj clásico.", "Pendientes discretos.", "Bolso estructurado.")));
        ESTILOS.put("deportivo", new DatosEstilo("Deportivo",
                "Práctico y activo, pensado para moverte con libertad.",
                Arrays.asList("Tejidos técnicos y transpirables.", "Siluetas cómodas.",
                        "Detalles deportivos bien ubicados."),
                Arrays.asList("Leggings o joggers.", "Sudadera o chaqueta ligera.",
                        "Zapatillas de running o lifestyle."),
                Arrays.asList("Gorra o cinta.", "Mochila deportiva.", "Smartwatch.")));
        ESTILOS.put("urbano", new DatosEstilo("Urbano",
                "Con actitud, inspirado en la calle y la cultura actual.",
                Arrays.asList("Capas y proporciones oversize.", "Mezcla de texturas.",
                        "Accesorios con personalidad."),
                Arrays.asList("Cargo pants o jeans anchos.", "Hoodie o camiseta gráfica.",
                        "Chaqueta bomber o denim."),
                Arrays.asList("Gorra o beanie.", "Cadena o anillos.", "Zapatillas chunky.")));
        ESTILOS.put("clasico", new DatosEstilo("Clásico",
                "Atemporal y pulido, siempre vigente.",
                Arrays.asList("Cortes tradicionales.", "Colores sobrios.",
                        "Calidad sobre cantidad."),
                Arrays.asList("Trench o blazer.", "Camisa blanca.", "Pantalón de pinza."),
                Arrays.asList("Reloj de cuero.", "Mocasines.", "Pañuelo de seda.")));
        ESTILOS.put("minimalista", new DatosEstilo("Minimalista",
                "Simple y esencial, sin excesos.",
                Arrays.asList("Menos piezas, mejor combinadas.", "Paleta neutra.",
                        "Siluetas limpias."),
                Arrays.asList("Camiseta de buena calidad.", "Pantalón recto.",
                        "Abrigo de línea simple."),
                Arrays.asList("Accesorios finos.", "Bolso de líneas rectas.", "Zapatillas blancas.")));
        ESTILOS.put("formal", new DatosEstilo("Formal",
                "Estructurado y profesional para contextos de etiqueta.",
                Arrays.asList("Sastrería bien ajustada.", "Colores clásicos.",
                        "Detalles impecables."),
                Arrays.asList("Traje o conjunto sastre.", "Camisa de vestir.",
                        "Zapatos de cuero."),
                Arrays.asList("Corbata o pañuelo.", "Gemelos.", "Maletín o bolso estructurado.")));
        ESTILOS.put("romantico", new DatosEstilo("Romántico",
                "Suave y con detalles que abrazan la feminidad y la delicadeza.",
                Arrays.asList("Tejidos suaves y vaporosos.", "Detalles de encaje o volantes.",
                        "Paleta de rosados y cremas."),
                Arrays.asList("Vestido fluido.", "Blusa con detalles.",
                        "Falda midi con movimiento."),
                Arrays.asList("Perlas.", "Cintas para el cabello.", "Bolso pequeño con textura.")));
        ESTILOS.put("bohemio", new DatosEstilo("Bohemio",
                "Libre, con texturas, capas y espíritu artesanal.",
                Arrays.asList("Mezcla de estampados y texturas.", "Prendas con movimiento.",
                        "Accesorios artesanales."),
                Arrays.asList("Vestido largo fluido.", "Chaleco o kimono.",
                        "Jeans desgastados."),
                Arrays.asList("Pendientes largos.", "Pulseras apiladas.", "Sombrero o pañuelo.")));
    }

    private Recomendaciones() {
    }

    public static DatosRostro rostro(String clave) {
        return ROSTROS.getOrDefault(clave, ROSTROS.get("ovalado"));
    }

    public static DatosFaccion faccion(String clave) {
        return FACCIONES.get(clave);
    }

    public static DatosColor color(String estacion) {
        return ESTACIONES.getOrDefault(estacion, ESTACIONES.get("verano"));
    }

    public static DatosCuerpo cuerpo(String genero, String clave) {
        String base = "masculino".equalsIgnoreCase(genero) ? "masculino_" : "femenino_";
        DatosCuerpo datos = CUERPOS.get(base + clave);
        if (datos == null) {
            datos = CUERPOS.get(base + "rectangulo");
        }
        return datos;
    }

    public static DatosEstilo estilo(String clave) {
        return ESTILOS.get(clave);
    }

    public static List<DatosFaccion> facciones(String faccionesCsv) {
        List<DatosFaccion> lista = new ArrayList<DatosFaccion>();
        if (faccionesCsv == null || faccionesCsv.trim().isEmpty()) {
            return lista;
        }
        for (String clave : faccionesCsv.split(",")) {
            DatosFaccion datos = faccion(clave.trim());
            if (datos != null) {
                lista.add(datos);
            }
        }
        return lista;
    }

    public static List<DatosEstilo> estilos(String estilosCsv) {
        List<DatosEstilo> lista = new ArrayList<DatosEstilo>();
        if (estilosCsv == null || estilosCsv.trim().isEmpty()) {
            return lista;
        }
        for (String clave : estilosCsv.split(",")) {
            DatosEstilo datos = estilo(clave.trim());
            if (datos != null) {
                lista.add(datos);
            }
        }
        return lista;
    }

    public static List<String> nombresEstilos(String estilosCsv) {
        List<String> lista = new ArrayList<String>();
        if (estilosCsv == null) {
            return lista;
        }
        for (String clave : estilosCsv.split(",")) {
            if (!clave.trim().isEmpty()) {
                lista.add(Cuestionario.nombreEstilo(clave.trim()));
            }
        }
        return lista;
    }
}
