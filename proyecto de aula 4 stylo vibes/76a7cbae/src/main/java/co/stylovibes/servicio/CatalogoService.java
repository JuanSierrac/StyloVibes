package co.stylovibes.servicio;

import co.stylovibes.dominio.Caracteristica;
import co.stylovibes.dominio.Catalogo;
import co.stylovibes.dominio.CatalogoItem;
import co.stylovibes.dominio.CatalogoRepository;
import co.stylovibes.dominio.Seccion;
import co.stylovibes.dominio.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CatalogoService {

    private final CatalogoRepository catalogoRepository;

    public CatalogoService(CatalogoRepository catalogoRepository) {
        this.catalogoRepository = catalogoRepository;
    }

    @Transactional
    public Catalogo generar(Usuario usuario, Caracteristica c) {
        Catalogo catalogo = catalogoRepository.findByUsuario(usuario).orElse(null);
        if (catalogo == null) {
            catalogo = new Catalogo();
            catalogo.setUsuario(usuario);
        } else {
            catalogo.getItems().clear();
        }
        catalogo.setFechaGeneracion(LocalDateTime.now());

        Recomendaciones.DatosRostro rostro = Recomendaciones.rostro(c.getTipoRostro());
        Recomendaciones.DatosColor color = Recomendaciones.color(c.getEstacion());
        Recomendaciones.DatosCuerpo cuerpo = Recomendaciones.cuerpo(usuario.getGenero(), c.getTipoCuerpo());
        List<Recomendaciones.DatosFaccion> facciones = Recomendaciones.facciones(c.getFacciones());
        List<Recomendaciones.DatosEstilo> estilos = Recomendaciones.estilos(c.getEstilos());

        String genero = generoImagen(usuario);
        String rostroKey = claveRostro(c.getTipoRostro());

        int orden = 1;
        orden = rostro(catalogo, rostro, genero, rostroKey, orden);
        orden = facciones(catalogo, facciones, genero, orden);
        orden = colorimetria(catalogo, color, c, genero, orden);
        orden = cuerpo(catalogo, cuerpo, genero, c.getTipoCuerpo(), orden);
        int ordenEstilos = estilos(catalogo, estilos, genero, orden);
        orden = outfits(catalogo, estilos, genero, ordenEstilos);
        orden = accesorios(catalogo, color, estilos, genero, orden);
        consejos(catalogo, orden);

        Catalogo guardado = catalogoRepository.save(catalogo);
        usuario.setCatalogo(guardado);
        return guardado;
    }

    private String generoImagen(Usuario usuario) {
        return "masculino".equalsIgnoreCase(usuario.getGenero()) ? "masculino" : "femenino";
    }

    private CatalogoItem item(Catalogo cat, Seccion seccion, String titulo, String descripcion, String imagen, int orden) {
        CatalogoItem item = new CatalogoItem();
        item.setSeccion(seccion);
        item.setTitulo(titulo);
        item.setDescripcion(descripcion);
        item.setImagen(imagen);
        item.setOrden(orden);
        cat.agregarItem(item);
        return item;
    }

    private int rostro(Catalogo cat, Recomendaciones.DatosRostro datos, String genero, String rostroKey, int orden) {
        item(cat, Seccion.ROSTRO_Y_CORTES, "Corte: " + datos.getTitulo(),
                datos.getDescripcion() + " " + datos.getCorte() + " " + datos.getEvitar(),
                img("rostro", genero, rostroKey), orden++);
        item(cat, Seccion.ROSTRO_Y_CORTES, "Flequillo ideal",
                datos.getFlequillo(),
                img("flequillo", genero, String.valueOf(indiceFlequillo(genero, rostroKey))), orden++);
        item(cat, Seccion.ROSTRO_Y_CORTES, "Gafas para tu rostro",
                datos.getGafas(),
                img("gafas", genero, String.valueOf(indiceGafas(genero, rostroKey))), orden++);
        return orden;
    }

    private int facciones(Catalogo cat, List<Recomendaciones.DatosFaccion> facciones, String genero, int orden) {
        if (facciones.isEmpty()) {
            item(cat, Seccion.FACCIONES_Y_CUIDADO, "Rutina facial base",
                    "Limpieza suave, hidratación y protector solar diario para cuidar tu piel.",
                    img("consejo", "3"), orden++);
            return orden;
        }
        String[] cuidados = {"Cuidado: ", "En tu rutina: ", "Para protegerlo: ",
                "Tip de belleza: ", "Rutina sugerida: ", "Ten en cuenta: "};
        int i = 0;
        for (Recomendaciones.DatosFaccion f : facciones) {
            String maquillaje = String.join(" ", f.getMaquillaje());
            item(cat, Seccion.FACCIONES_Y_CUIDADO, f.getTitulo(),
                    f.getDescripcion() + " " + maquillaje + " " + cuidados[i % cuidados.length] + f.getCuidado(),
                    img("faccion", genero, prefijoFaccion(f.getTitulo())), orden++);
            i++;
        }
        return orden;
    }

    private int colorimetria(Catalogo cat, Recomendaciones.DatosColor color, Caracteristica c, String genero, int orden) {
        item(cat, Seccion.COLORIMETRIA_Y_PALETAS, "Paleta de ropa · " + color.getTitulo(),
                color.getDescripcion() + " Colores: " + nombres(color.getRopa()) + ".",
                img("color", genero, claveEstacion(c.getEstacion())), orden++);
        item(cat, Seccion.COLORIMETRIA_Y_PALETAS,
                "masculino".equals(genero) ? "Grooming en armonía" : "Maquillaje en armonía",
                "Subtono " + Cuestionario.nombreSubtono(c.getSubtono()) + ". Tonos recomendados: "
                        + nombres(color.getMaquillaje()) + ".",
                img("detalle", genero, "masculino".equals(genero) ? "grooming" : "maquillaje"), orden++);
        item(cat, Seccion.COLORIMETRIA_Y_PALETAS, "Cabello y accesorios",
                "Cabello: " + nombres(color.getCabello()) + ". Metales y accesorios que armonizan: "
                        + nombres(color.getAccesorios()) + ".",
                img("detalle", genero, "cabello"), orden++);
        return orden;
    }

    private int cuerpo(Catalogo cat, Recomendaciones.DatosCuerpo cuerpo, String genero,
                       String tipoCuerpo, int orden) {
        String clave = claveCuerpo(genero, tipoCuerpo);
        item(cat, Seccion.CUERPO_Y_VESTUARIO, cuerpo.getTitulo() + ": prendas que te favorecen",
                cuerpo.getDescripcion() + " " + String.join(" ", cuerpo.getPrendas()),
                img("cuerpo", genero, clave + "-a"), orden++);
        item(cat, Seccion.CUERPO_Y_VESTUARIO, "Consejos de proporción",
                "Con respeto y sin reglas rígidas, evita: " + String.join(" ", cuerpo.getEvitar()),
                img("cuerpo", genero, clave + "-b"), orden++);
        return orden;
    }

    private int estilos(Catalogo cat, List<Recomendaciones.DatosEstilo> estilos, String genero, int orden) {
        if (estilos.isEmpty()) {
            item(cat, Seccion.ESTILO_PERSONAL, "Encuentra tu estilo",
                    "Explora distintos estilos y quédate con lo que te haga sentir bien.",
                    img("estilo", genero, "casual"), orden++);
            return orden;
        }
        String[] intro = {"Claves: ", "A tener en cuenta: ", "En la práctica: "};
        int i = 0;
        for (Recomendaciones.DatosEstilo e : estilos) {
            item(cat, Seccion.ESTILO_PERSONAL, "Estilo " + e.getTitulo(),
                    e.getDescripcion() + " " + intro[i % intro.length] + String.join(" ", e.getClaves()),
                    img("estilo", genero, claveEstilo(e.getTitulo())), orden++);
            i++;
        }
        return orden;
    }

    private int outfits(Catalogo cat, List<Recomendaciones.DatosEstilo> estilos, String genero, int orden) {
        if (estilos.isEmpty()) {
            item(cat, Seccion.OUTFITS, "Combinaciones versátiles",
                    "Mezcla básicos neutros con una prenda protagonista y zapatillas cómodas.",
                    img("outfit", genero, "casual"), orden++);
            return orden;
        }
        String[] titulos = {"Look %s", "Outfit %s", "Idea de estilo: %s"};
        int i = 0;
        for (Recomendaciones.DatosEstilo e : estilos) {
            String prendas = lista(e.getPrendas(), ", ").toLowerCase();
            String accesorios = lista(e.getAccesorios(), ", ").toLowerCase();
            String descripcion;
            switch (i % 3) {
                case 1:
                    descripcion = "La base: " + prendas + ". El toque final: " + accesorios + ".";
                    break;
                case 2:
                    descripcion = "Prueba " + prendas + " con " + accesorios + ".";
                    break;
                default:
                    descripcion = "Combina " + prendas + ". Suma " + accesorios + ".";
            }
            item(cat, Seccion.OUTFITS, String.format(titulos[i % titulos.length], e.getTitulo()), descripcion,
                    img("outfit", genero, claveEstilo(e.getTitulo())), orden++);
            i++;
        }
        return orden;
    }

    private int accesorios(Catalogo cat, Recomendaciones.DatosColor color,
                           List<Recomendaciones.DatosEstilo> estilos, String genero, int orden) {
        StringBuilder sb = new StringBuilder();
        sb.append("El cierre está en los detalles: joyas en ")
                .append(nombres(color.getAccesorios()).toLowerCase()).append(". ");
        if (!estilos.isEmpty()) {
            sb.append("Suma ").append(lista(estilos.get(0).getAccesorios(), ", ").toLowerCase()).append(". ");
        }
        sb.append("Mantén las monturas que ya te recomendamos para tu rostro.");
        item(cat, Seccion.ACCESORIOS, "Accesorios que completan tu look", sb.toString(),
                img("detalle", genero, "joyeria"), orden++);
        return orden;
    }

    private int consejos(Catalogo cat, int orden) {
        item(cat, Seccion.CONSEJOS, "Tu esencia es lo primero",
                "La ropa y el maquillaje están para acompañarte, no para cambiarte. Usa lo que te haga sentir cómodo y seguro.",
                img("consejo", "1"), orden++);
        item(cat, Seccion.CONSEJOS, "Diversidad y respeto",
                "Cada cuerpo, cada rostro y cada estilo es válido. Estas recomendaciones son una guía, no una regla.",
                img("consejo", "2"), orden++);
        item(cat, Seccion.CONSEJOS, "Rutina de cuidado sencilla",
                "Limpieza, hidratación y protector solar. Duerme bien, hidrátate y elige textiles cómodos.",
                img("consejo", "3"), orden++);
        return orden;
    }

    private String img(String... partes) {
        return "/img/cat/" + String.join("-", partes) + ".jpg";
    }

    private String claveRostro(String tipo) {
        String clave = tipo == null ? "" : sinAcentos(tipo).toLowerCase();
        switch (clave) {
            case "redondo":
            case "cuadrado":
            case "rectangular":
            case "corazon":
            case "diamante":
            case "triangular":
            case "oblongo":
                return clave;
            default:
                return "ovalado";
        }
    }

    private String claveCuerpo(String genero, String tipo) {
        String clave = tipo == null ? "" : sinAcentos(tipo).toLowerCase();
        if ("masculino".equals(genero)) {
            switch (clave) {
                case "trapecio":
                case "triangulo":
                case "triangulo_invertido":
                case "ovalado":
                    return clave;
                default:
                    return "rectangulo";
            }
        }
        switch (clave) {
            case "reloj":
            case "triangulo":
            case "triangulo_invertido":
            case "ovalado":
                return clave;
            default:
                return "rectangulo";
        }
    }

    private String claveEstacion(String estacion) {
        String clave = estacion == null ? "" : sinAcentos(estacion).toLowerCase();
        switch (clave) {
            case "primavera":
            case "otono":
            case "invierno":
                return clave;
            default:
                return "verano";
        }
    }

    private int indiceFlequillo(String genero, String rostro) {
        if ("masculino".equals(genero)) {
            switch (rostro) {
                case "redondo": return 2;
                case "cuadrado": return 3;
                case "rectangular": return 5;
                case "corazon": return 6;
                case "diamante": return 2;
                case "triangular": return 4;
                case "oblongo": return 5;
                default: return 1;
            }
        }
        switch (rostro) {
            case "redondo": return 4;
            case "cuadrado": return 2;
            case "rectangular": return 3;
            case "corazon": return 6;
            case "diamante": return 5;
            case "triangular": return 2;
            case "oblongo": return 3;
            default: return 1;
        }
    }

    private int indiceGafas(String genero, String rostro) {
        if ("masculino".equals(genero)) {
            switch (rostro) {
                case "redondo": return 6;
                case "cuadrado": return 2;
                case "rectangular": return 4;
                case "corazon": return 5;
                case "diamante": return 6;
                case "triangular": return 1;
                case "oblongo": return 5;
                default: return 1;
            }
        }
        switch (rostro) {
            case "redondo": return 6;
            case "cuadrado": return 2;
            case "rectangular": return 4;
            case "corazon": return 5;
            case "diamante": return 3;
            case "triangular": return 1;
            case "oblongo": return 5;
            default: return 1;
        }
    }

    private String prefijoFaccion(String titulo) {
        String clave = sinAcentos(titulo).toLowerCase();
        if (clave.startsWith("ojos")) return "ojos";
        if (clave.startsWith("cejas")) return "cejas";
        if (clave.startsWith("nariz")) return "nariz";
        if (clave.startsWith("labios")) return "labios";
        if (clave.startsWith("pomulos")) return "pomulos";
        if (clave.startsWith("mandibula")) return "mandibula";
        return "ojos";
    }

    private String claveEstilo(String titulo) {
        String clave = sinAcentos(titulo).toLowerCase();
        switch (clave) {
            case "casual":
            case "elegante":
            case "deportivo":
            case "urbano":
            case "clasico":
            case "minimalista":
            case "formal":
            case "romantico":
            case "bohemio":
                return clave;
            default:
                return "casual";
        }
    }

    private String sinAcentos(String texto) {
        if (texto == null) {
            return "";
        }
        return Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    }

    private String lista(List<String> valores, String separador) {
        StringBuilder sb = new StringBuilder();
        for (String valor : valores) {
            if (valor == null || valor.trim().isEmpty()) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(separador);
            }
            sb.append(valor.trim().replaceAll("\\.$", ""));
        }
        return sb.toString();
    }

    private String nombres(List<Recomendaciones.Color> colores) {
        StringBuilder sb = new StringBuilder();
        for (Recomendaciones.Color c : colores) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(c.getNombre());
        }
        return sb.toString();
    }
}
