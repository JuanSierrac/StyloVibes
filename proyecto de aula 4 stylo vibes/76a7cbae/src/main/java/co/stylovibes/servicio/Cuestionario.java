package co.stylovibes.servicio;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Cuestionario {

    public static final String[] ROSTROS = {
            "ovalado", "redondo", "cuadrado", "rectangular",
            "corazon", "diamante", "triangular", "oblongo"
    };

    public static class Opcion {
        private final String valor;
        private final String texto;
        private final Map<String, Integer> puntos;

        public Opcion(String valor, String texto) {
            this(valor, texto, new LinkedHashMap<String, Integer>());
        }

        public Opcion(String valor, String texto, Map<String, Integer> puntos) {
            this.valor = valor;
            this.texto = texto;
            this.puntos = puntos;
        }

        public String getValor() { return valor; }
        public String getTexto() { return texto; }
        public Map<String, Integer> getPuntos() { return puntos; }
    }

    public static class Pregunta {
        private final String id;
        private final String enunciado;
        private final String ayuda;
        private final boolean multiple;
        private final int maxSeleccion;
        private final List<Opcion> opciones;

        public Pregunta(String id, String enunciado, String ayuda, boolean multiple,
                        int maxSeleccion, List<Opcion> opciones) {
            this.id = id;
            this.enunciado = enunciado;
            this.ayuda = ayuda;
            this.multiple = multiple;
            this.maxSeleccion = maxSeleccion;
            this.opciones = opciones;
        }

        public String getId() { return id; }
        public String getEnunciado() { return enunciado; }
        public String getAyuda() { return ayuda; }
        public boolean isMultiple() { return multiple; }
        public int getMaxSeleccion() { return maxSeleccion; }
        public List<Opcion> getOpciones() { return opciones; }

        public Opcion opcion(String valor) {
            for (Opcion o : opciones) {
                if (o.getValor().equals(valor)) {
                    return o;
                }
            }
            return null;
        }
    }

    public static class Nivel {
        private final int numero;
        private final String titulo;
        private final String subtitulo;
        private final String imagen;
        private final List<Pregunta> preguntas;

        public Nivel(int numero, String titulo, String subtitulo, String imagen, List<Pregunta> preguntas) {
            this.numero = numero;
            this.titulo = titulo;
            this.subtitulo = subtitulo;
            this.imagen = imagen;
            this.preguntas = preguntas;
        }

        public int getNumero() { return numero; }
        public String getTitulo() { return titulo; }
        public String getSubtitulo() { return subtitulo; }
        public String getImagen() { return imagen; }
        public List<Pregunta> getPreguntas() { return preguntas; }
    }

    private Cuestionario() {
    }

    private static Map<String, Integer> pts(Object... pares) {
        Map<String, Integer> mapa = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < pares.length; i += 2) {
            mapa.put((String) pares[i], (Integer) pares[i + 1]);
        }
        return mapa;
    }

    private static Opcion o(String valor, String texto, Object... pares) {
        return new Opcion(valor, texto, pts(pares));
    }

    private static Opcion o(String valor, String texto) {
        return new Opcion(valor, texto);
    }

    private static Pregunta radio(String id, String enunciado, String ayuda, List<Opcion> opciones) {
        return new Pregunta(id, enunciado, ayuda, false, 1, opciones);
    }

    private static Pregunta multi(String id, String enunciado, String ayuda, int max, List<Opcion> opciones) {
        return new Pregunta(id, enunciado, ayuda, true, max, opciones);
    }

    public static List<Pregunta> preguntas(int nivel, String genero) {
        switch (nivel) {
            case 1: return rostro();
            case 2: return facciones();
            case 3: return colorimetria();
            case 4: return cuerpo(genero);
            case 5: return estilos();
            default: return new ArrayList<Pregunta>();
        }
    }

    public static String imagenNivel(int numero, String genero) {
        String sufijo = "mixto";
        if ("masculino".equalsIgnoreCase(genero)) {
            sufijo = "masculino";
        } else if ("femenino".equalsIgnoreCase(genero)) {
            sufijo = "femenino";
        }
        return "/img/nivel-" + numero + "-" + sufijo + ".png";
    }

    public static Nivel nivel(int numero, String genero) {
        switch (numero) {
            case 1:
                return new Nivel(1, "Tipo de rostro",
                        "Descubre la forma de tu rostro y los cortes, flequillos y accesorios que más te favorecen.",
                        imagenNivel(1, genero), rostro());
            case 2:
                return new Nivel(2, "Facciones",
                        "Ojos, cejas, nariz, labios, pómulos y mandíbula: aprende a resaltar lo que te hace único.",
                        imagenNivel(2, genero), facciones());
            case 3:
                return new Nivel(3, "Colorimetría",
                        "Encuentra tu subtono y tu estación para elegir colores que iluminen tu piel.",
                        imagenNivel(3, genero), colorimetria());
            case 4:
                return new Nivel(4, "Tipo de cuerpo",
                        "Recomendaciones de prendas pensadas para tu silueta, con total respeto por tu cuerpo.",
                        imagenNivel(4, genero), cuerpo(genero));
            case 5:
                return new Nivel(5, "Estilo personal",
                        "Casual, elegante, deportivo, urbano, clásico, minimalista, formal, romántico o bohemio.",
                        imagenNivel(5, genero), estilos());
            default:
                return null;
        }
    }

    private static List<Pregunta> rostro() {
        List<Pregunta> lista = new ArrayList<Pregunta>();
        lista.add(radio("largo", "¿Cómo es la longitud de tu rostro?",
                "Mira tu rostro de frente, sin maquillaje, con el cabello recogido.",
                opciones(
                        o("largo", "Más largo que ancho", "oblongo", 3, "rectangular", 1),
                        o("medio", "Casi igual de largo que de ancho", "redondo", 2, "cuadrado", 1, "corazon", 1),
                        o("equilibrado", "Equilibrado, ni muy largo ni muy corto", "ovalado", 2, "diamante", 1),
                        o("largo_mandibula", "Largo, con mandíbula marcada", "rectangular", 2, "oblongo", 1))));
        lista.add(radio("frente", "¿Cómo describirías tu frente?",
                "Observa su ancho y la línea del cabello.",
                opciones(
                        o("amplia", "Amplia y despejada", "ovalado", 1, "rectangular", 2, "oblongo", 1),
                        o("angulos", "Ancha, con esquinas marcadas", "cuadrado", 3, "rectangular", 1),
                        o("redonda", "Redondeada y suave", "redondo", 3, "corazon", 1),
                        o("estrecha", "Estrecha o con entradas", "diamante", 3, "triangular", 1))));
        lista.add(radio("pomulos", "¿Cómo son tus pómulos?",
                "Toca suavemente con tus dedos para notar el hueso.",
                opciones(
                        o("anchos", "Son la parte más ancha de mi rostro", "diamante", 3, "corazon", 1),
                        o("suaves", "Suaves, poco marcados", "ovalado", 2, "redondo", 2),
                        o("marcados", "Marcados y altos", "cuadrado", 2, "diamante", 2),
                        o("medios", "Equilibrados con el resto", "ovalado", 2, "rectangular", 1))));
        lista.add(radio("mandibula", "¿Cómo es tu mandíbula?",
                "Fíjate en la línea que va desde las orejas hasta el mentón.",
                opciones(
                        o("angulosa", "Angulosa y definida", "cuadrado", 3, "rectangular", 1),
                        o("redondeada", "Redondeada", "redondo", 3, "ovalado", 1),
                        o("v", "En punta o en forma de V", "corazon", 3, "diamante", 1),
                        o("ancha", "Ancha en la base", "triangular", 3, "cuadrado", 1))));
        lista.add(radio("menton", "¿Y tu mentón?",
                "Es la parte final de tu rostro.",
                opciones(
                        o("redondo", "Redondeado", "redondo", 1, "ovalado", 1),
                        o("cuadrado", "Cuadrado", "cuadrado", 3, "rectangular", 1),
                        o("puntiagudo", "Puntiagudo", "corazon", 3, "diamante", 1),
                        o("ancho", "Ligeramente ancho", "triangular", 3, "oblongo", 1))));
        lista.add(radio("rasgo", "¿Qué rasgo sientes que más se nota en tu rostro?",
                "No hay respuestas correctas: todas las formas son válidas y bonitas.",
                opciones(
                        o("curvas", "Mis curvas y mejillas", "redondo", 2, "corazon", 1, "ovalado", 1),
                        o("angulos", "Mis ángulos y mandíbula", "cuadrado", 2, "rectangular", 2),
                        o("largo", "Mi rostro alargado", "oblongo", 3, "rectangular", 1),
                        o("equilibrio", "Mi equilibrio general", "ovalado", 3, "diamante", 1))));
        return lista;
    }

    private static List<Pregunta> facciones() {
        List<Pregunta> lista = new ArrayList<Pregunta>();
        lista.add(radio("ojos", "Tus ojos se parecen a...",
                "Elige la descripción más cercana.",
                opciones(
                        o("ojos_almendrados", "Almendrados: alargados y ligeramente elevados"),
                        o("ojos_redondos", "Redondos: grandes y con mirada abierta"),
                        o("ojos_caidos", "Caídos: la parte exterior desciende suavemente"),
                        o("ojos_rasgados", "Rasgados: finos y con mucha expresión"))));
        lista.add(radio("cejas", "Tus cejas son...",
                "La forma natural de tus cejas guía el maquillaje.",
                opciones(
                        o("cejas_rectas", "Rectas y horizontales"),
                        o("cejas_arqueadas", "Con un arco suave"),
                        o("cejas_redondas", "Redondeadas y cortas"),
                        o("cejas_gruesas", "Gruesas y pobladas"))));
        lista.add(radio("nariz", "Tu nariz tiene...",
                "Mírala de frente y de perfil.",
                opciones(
                        o("nariz_recta", "Un puente recto"),
                        o("nariz_respingada", "La punta ligeramente respingada"),
                        o("nariz_aguilena", "Un puente con curva suave"),
                        o("nariz_ancha", "Base ancha y narinas amplias"))));
        lista.add(radio("labios", "Tus labios son...",
                "Observa su volumen y proporción.",
                opciones(
                        o("labios_finos", "Finos y delicados"),
                        o("labios_carnosos", "Carnosos y con volumen"),
                        o("labios_corazon", "En forma de corazón"),
                        o("labios_amplios", "Amplios, con el labio inferior más lleno"))));
        lista.add(radio("pomulos", "Tus pómulos son...",
                "El rubor y la iluminación se adaptan a esta forma.",
                opciones(
                        o("pomulos_altos", "Altos y visibles"),
                        o("pomulos_suaves", "Suaves y redondeados"),
                        o("pomulos_marcados", "Marcados y angulosos"))));
        lista.add(radio("mandibula", "Tu mandíbula es...",
                "La última pieza de tu estructura facial.",
                opciones(
                        o("mandibula_definida", "Definida y firme"),
                        o("mandibula_suave", "Suave y redondeada"),
                        o("mandibula_angulosa", "Angulosa y marcada"))));
        return lista;
    }

    private static List<Pregunta> colorimetria() {
        List<Pregunta> lista = new ArrayList<Pregunta>();
        lista.add(radio("venas", "¿De qué color se ven las venas de tu muñeca?",
                "Con buena luz natural.",
                opciones(
                        o("azuladas", "Azuladas o moradas", "frio", 2, "invierno", 2, "verano", 1),
                        o("verdosas", "Verdosas", "calido", 2, "otono", 1, "primavera", 2),
                        o("indefinidas", "No alcanzo a distinguirlo", "neutro", 2))));
        lista.add(radio("sol", "¿Qué pasa cuando te expones al sol?",
                "Piensa en tu reacción habitual.",
                opciones(
                        o("quemo", "Me quemo y casi no me bronceo", "frio", 2, "invierno", 2, "verano", 1),
                        o("bronceo", "Me bronceo con facilidad", "calido", 2, "otono", 2, "primavera", 1),
                        o("progresivo", "Me pongo moreno poco a poco", "neutro", 1, "otono", 1, "verano", 1))));
        lista.add(radio("joyas", "¿Qué joyería te queda mejor?",
                "La que hace que tu piel se vea más luminosa.",
                opciones(
                        o("plata", "Plata, platino o blanco", "frio", 2, "invierno", 1, "verano", 2),
                        o("oro", "Oro, bronce o dorado", "calido", 2, "otono", 2, "primavera", 1),
                        o("ambas", "Me quedan bien ambas", "neutro", 2))));
        lista.add(radio("contraste", "¿Cómo es el contraste entre tu cabello y tu piel?",
                "Piensa en tu tono natural.",
                opciones(
                        o("alto", "Muy alto: cabello oscuro y piel clara", "frio", 1, "invierno", 3),
                        o("medio", "Medio: ni muy claro ni muy oscuro", "verano", 1, "otono", 1, "neutro", 1),
                        o("bajo", "Bajo: tonos parecidos y suaves", "frio", 1, "verano", 2, "primavera", 1),
                        o("calido", "Cabello cobrizo, dorado o rojizo", "calido", 2, "primavera", 1, "otono", 2))));
        lista.add(radio("ojos", "¿Cuál se parece más al color de tus ojos?",
                "Observa el brillo y los matices.",
                opciones(
                        o("oscuros", "Muy oscuros, casi negros", "invierno", 3, "frio", 1),
                        o("miel", "Miel, ámbar o avellana", "otono", 3, "calido", 1),
                        o("verdes", "Verdes", "primavera", 2, "otono", 1),
                        o("claros", "Azules o grises", "verano", 3, "frio", 1))));
        lista.add(radio("colores", "¿Qué colores sientes que más te iluminan?",
                "Piensa en la ropa que más te gusta cómo te queda.",
                opciones(
                        o("frios", "Fríos e intensos, como el azul rey", "invierno", 3, "frio", 1),
                        o("suaves", "Suaves y claros, como el lila o el azul cielo", "verano", 3, "frio", 1),
                        o("vivos", "Cálidos y vivos, como el coral o el amarillo", "primavera", 3, "calido", 1),
                        o("profundos", "Cálidos y profundos, como el terracota", "otono", 3, "calido", 1))));
        return lista;
    }

    private static List<Pregunta> cuerpo(String genero) {
        boolean masculino = "masculino".equalsIgnoreCase(genero);
        List<Pregunta> lista = new ArrayList<Pregunta>();
        if (masculino) {
            lista.add(radio("hombros", "¿Cómo es la relación entre tus hombros y tu cintura?",
                    "Ponte frente a un espejo y observa tu silueta con ropa cómoda.",
                    opciones(
                            o("hombros_anchos", "Mis hombros son claramente más anchos", "trapecio", 3, "triangulo_invertido", 1),
                            o("similares", "Hombros y cintura son similares", "rectangulo", 3, "trapecio", 1),
                            o("cintura_ancha", "Mi cintura o abdomen es más ancho", "triangulo", 3, "ovalado", 1),
                            o("hombros_estrechos", "Mis hombros son estrechos y mi cintura ancha", "ovalado", 2, "triangulo", 1))));
            lista.add(radio("abdomen", "¿Cómo describirías tu abdomen?",
                    "No se trata de tallas: se trata de proporciones.",
                    opciones(
                            o("plano", "Plano y compacto", "rectangulo", 2, "trapecio", 1),
                            o("curvo", "Con una ligera curva", "ovalado", 2, "triangulo", 1),
                            o("marcado", "Es la zona más amplia", "ovalado", 3, "triangulo", 1),
                            o("atletico", "Atlético, en forma de V", "trapecio", 2, "triangulo_invertido", 2))));
            lista.add(radio("estructura", "¿Qué parte de tu cuerpo sientes más fuerte?",
                    "Todas las estructuras son válidas.",
                    opciones(
                            o("piernas", "Piernas y caderas", "triangulo", 3),
                            o("torso", "Torso largo y piernas delgadas", "rectangulo", 3),
                            o("hombros", "Hombros, espalda y brazos", "trapecio", 2, "triangulo_invertido", 2),
                            o("equilibrio", "Todo está equilibrado", "rectangulo", 2, "trapecio", 1))));
        } else {
            lista.add(radio("hombros", "¿Cómo es la relación entre tus hombros y tus caderas?",
                    "Ponte frente a un espejo y observa tu silueta con ropa cómoda.",
                    opciones(
                            o("hombros_anchos", "Mis hombros son más anchos que mis caderas", "triangulo_invertido", 3, "reloj", 1),
                            o("caderas_anchas", "Mis caderas son más anchas que mis hombros", "triangulo", 3, "reloj", 1),
                            o("similares", "Son similares, con cintura marcada", "reloj", 3),
                            o("sin_curva", "Son similares, con poca cintura marcada", "rectangulo", 3, "ovalado", 1))));
            lista.add(radio("cintura", "¿Cómo es tu cintura?",
                    "Observa si tu cintura se marca de forma natural.",
                    opciones(
                            o("marcada", "Muy marcada", "reloj", 2, "triangulo", 1),
                            o("poco", "Poco marcada", "rectangulo", 3, "ovalado", 1),
                            o("abdomen", "Suele acumularse un poco en el abdomen", "ovalado", 3, "triangulo", 1),
                            o("definida", "Definida, con caderas más amplias", "triangulo", 2, "reloj", 1))));
            lista.add(radio("estructura", "¿Qué parte de tu cuerpo sientes más fuerte?",
                    "Todas las estructuras son válidas.",
                    opciones(
                            o("curvas", "Curvas en pecho y caderas", "reloj", 2, "ovalado", 1),
                            o("recta", "Silueta recta o atlética", "rectangulo", 3),
                            o("caderas", "Piernas fuertes y caderas amplias", "triangulo", 3),
                            o("hombros", "Hombros y espalda amplios", "triangulo_invertido", 3))));
        }
        return lista;
    }

    private static List<Pregunta> estilos() {
        List<Pregunta> lista = new ArrayList<Pregunta>();
        lista.add(multi("estilos", "¿Con cuáles estilos te identificas?",
                "Elige entre 1 y 3. No hay un estilo mejor que otro.", 3,
                opciones(
                        o("casual", "Casual: cómodo, relajado y versátil"),
                        o("elegante", "Elegante: sofisticado y cuidado"),
                        o("deportivo", "Deportivo: práctico y activo"),
                        o("urbano", "Urbano: con actitud y calle"),
                        o("clasico", "Clásico: atemporal y pulido"),
                        o("minimalista", "Minimalista: simple y esencial"),
                        o("formal", "Formal: estructurado y profesional"),
                        o("romantico", "Romántico: suave y con detalles"),
                        o("bohemio", "Bohemio: libre, texturas y capas"))));
        lista.add(radio("ocasion", "¿Qué buscas al vestirte?",
                "Tu respuesta se suma a los estilos que elegiste.",
                opciones(
                        o("comodidad", "Comodidad ante todo", "casual", 1, "deportivo", 1),
                        o("sofisticacion", "Verme sofisticado", "elegante", 1, "formal", 1),
                        o("personalidad", "Expresar mi personalidad", "urbano", 1, "bohemio", 1),
                        o("armonia", "Una armonía sencilla", "minimalista", 1, "clasico", 1),
                        o("detalle", "Suavidad y detalles", "romantico", 1, "elegante", 1))));
        return lista;
    }

    private static List<Opcion> opciones(Opcion... opciones) {
        List<Opcion> lista = new ArrayList<Opcion>();
        for (Opcion o : opciones) {
            lista.add(o);
        }
        return lista;
    }

    public static String nombreRostro(String clave) {
        switch (clave) {
            case "ovalado": return "Óvalado";
            case "redondo": return "Redondo";
            case "cuadrado": return "Cuadrado";
            case "rectangular": return "Rectangular";
            case "corazon": return "Corazón";
            case "diamante": return "Diamante";
            case "triangular": return "Triangular";
            case "oblongo": return "Oblongo";
            default: return clave;
        }
    }

    public static String nombreSubtono(String clave) {
        if ("calido".equals(clave)) return "Cálido";
        if ("frio".equals(clave)) return "Frío";
        if ("neutro".equals(clave)) return "Neutro";
        return clave;
    }

    public static String nombreEstacion(String clave) {
        if ("primavera".equals(clave)) return "Primavera";
        if ("verano".equals(clave)) return "Verano";
        if ("otono".equals(clave)) return "Otoño";
        if ("invierno".equals(clave)) return "Invierno";
        return clave;
    }

    public static String nombreCuerpo(String clave) {
        switch (clave) {
            case "reloj": return "Reloj de arena";
            case "triangulo": return "Triángulo";
            case "triangulo_invertido": return "Triángulo invertido";
            case "rectangulo": return "Rectángulo";
            case "ovalado": return "Ovalado";
            case "trapecio": return "Trapecio";
            default: return clave;
        }
    }

    public static String nombreEstilo(String clave) {
        if ("casual".equals(clave)) return "Casual";
        if ("elegante".equals(clave)) return "Elegante";
        if ("deportivo".equals(clave)) return "Deportivo";
        if ("urbano".equals(clave)) return "Urbano";
        if ("clasico".equals(clave)) return "Clásico";
        if ("minimalista".equals(clave)) return "Minimalista";
        if ("formal".equals(clave)) return "Formal";
        if ("romantico".equals(clave)) return "Romántico";
        if ("bohemio".equals(clave)) return "Bohemio";
        return clave;
    }
}
