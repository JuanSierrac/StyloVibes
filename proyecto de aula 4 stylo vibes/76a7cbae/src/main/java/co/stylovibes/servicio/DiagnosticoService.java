package co.stylovibes.servicio;

import co.stylovibes.dominio.Caracteristica;
import co.stylovibes.dominio.CaracteristicaRepository;
import co.stylovibes.dominio.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiagnosticoService {

    public static class Resultado {
        private final boolean ok;
        private final List<String> errores;
        private final Map<String, List<String>> respuestas;

        public Resultado(boolean ok, List<String> errores, Map<String, List<String>> respuestas) {
            this.ok = ok;
            this.errores = errores;
            this.respuestas = respuestas;
        }

        public boolean isOk() { return ok; }
        public List<String> getErrores() { return errores; }
        public Map<String, List<String>> getRespuestas() { return respuestas; }
    }

    private final CaracteristicaRepository caracteristicaRepository;
    private final CatalogoService catalogoService;

    public DiagnosticoService(CaracteristicaRepository caracteristicaRepository, CatalogoService catalogoService) {
        this.caracteristicaRepository = caracteristicaRepository;
        this.catalogoService = catalogoService;
    }

    public Cuestionario.Nivel nivel(int numero, Usuario usuario) {
        return Cuestionario.nivel(numero, usuario.getGenero());
    }

    public Caracteristica obtenerOCrear(Usuario usuario) {
        if (usuario.getCaracteristica() != null) {
            return usuario.getCaracteristica();
        }
        Caracteristica existente = caracteristicaRepository.findByUsuario(usuario).orElse(null);
        if (existente != null) {
            usuario.setCaracteristica(existente);
            return existente;
        }
        Caracteristica nueva = new Caracteristica();
        nueva.setUsuario(usuario);
        usuario.setCaracteristica(nueva);
        return caracteristicaRepository.save(nueva);
    }

    public Map<String, List<String>> valoresGuardados(int numero, Caracteristica c) {
        Map<String, List<String>> mapa = new LinkedHashMap<String, List<String>>();
        String crudo = c.respuestasNivel(numero);
        if (crudo == null || crudo.isEmpty()) {
            return mapa;
        }
        for (String par : crudo.split("\\|")) {
            int idx = par.indexOf('=');
            if (idx <= 0) {
                continue;
            }
            String clave = par.substring(0, idx);
            String valor = par.substring(idx + 1);
            mapa.put(clave, new ArrayList<String>(Arrays.asList(valor.split(","))));
        }
        return mapa;
    }

    @Transactional
    public Resultado procesar(int numero, Usuario usuario, Map<String, String[]> params) {
        Cuestionario.Nivel nivel = nivel(numero, usuario);
        Map<String, List<String>> respuestas = new LinkedHashMap<String, List<String>>();
        List<String> errores = new ArrayList<String>();

        for (Cuestionario.Pregunta pregunta : nivel.getPreguntas()) {
            String[] valores = params.get(pregunta.getId());
            List<String> seleccion = new ArrayList<String>();
            if (valores != null) {
                for (String valor : valores) {
                    if (valor != null && !valor.trim().isEmpty() && pregunta.opcion(valor) != null) {
                        seleccion.add(valor);
                    }
                }
            }
            if (seleccion.isEmpty()) {
                errores.add("Responde: " + pregunta.getEnunciado());
                continue;
            }
            if (pregunta.isMultiple()) {
                if (seleccion.size() > pregunta.getMaxSeleccion()) {
                    errores.add("En \"" + pregunta.getEnunciado() + "\" puedes elegir máximo "
                            + pregunta.getMaxSeleccion() + " opciones.");
                    continue;
                }
            } else if (seleccion.size() > 1) {
                errores.add("En \"" + pregunta.getEnunciado() + "\" elige solo una opción.");
                continue;
            }
            respuestas.put(pregunta.getId(), seleccion);
        }

        if (!errores.isEmpty()) {
            return new Resultado(false, errores, respuestas);
        }

        Caracteristica c = obtenerOCrear(usuario);
        switch (numero) {
            case 1:
                c.setTipoRostro(ganador(puntajes(nivel, respuestas), Arrays.asList(Cuestionario.ROSTROS)));
                break;
            case 2: {
                List<String> seleccion = new ArrayList<String>();
                for (Cuestionario.Pregunta p : nivel.getPreguntas()) {
                    seleccion.addAll(respuestas.get(p.getId()));
                }
                c.setFacciones(String.join(",", seleccion));
                break;
            }
            case 3: {
                Map<String, Integer> puntos = puntajes(nivel, respuestas);
                c.setSubtono(subtono(puntos));
                c.setEstacion(ganador(puntos, Arrays.asList("primavera", "verano", "otono", "invierno")));
                break;
            }
            case 4:
                c.setTipoCuerpo(ganador(puntajes(nivel, respuestas), ordenCuerpo(usuario.getGenero())));
                break;
            case 5: {
                Map<String, Integer> puntos = puntajes(nivel, respuestas);
                List<String> elegidos = new ArrayList<String>(respuestas.get("estilos"));
                final Map<String, Integer> puntajeFinal = puntos;
                elegidos.sort((a, b) -> Integer.compare(
                        puntajeFinal.getOrDefault(b, 0), puntajeFinal.getOrDefault(a, 0)));
                if (elegidos.size() > 3) {
                    elegidos = elegidos.subList(0, 3);
                }
                c.setEstilos(String.join(",", elegidos));
                break;
            }
            default:
                return new Resultado(false, Arrays.asList("Nivel no válido."), respuestas);
        }

        c.guardarRespuestas(numero, serializar(respuestas));
        caracteristicaRepository.save(c);

        if (c.todoCompletado()) {
            catalogoService.generar(usuario, c);
        }
        return new Resultado(true, errores, respuestas);
    }

    public String respuestasLegibles(int numero, Usuario usuario, Caracteristica c) {
        String crudo = c == null ? null : c.respuestasNivel(numero);
        if (crudo == null || crudo.isEmpty()) {
            return "";
        }
        Map<String, List<String>> guardadas = valoresGuardados(numero, c);
        Cuestionario.Nivel nivel = nivel(numero, usuario);
        StringBuilder sb = new StringBuilder();
        for (Cuestionario.Pregunta p : nivel.getPreguntas()) {
            List<String> valores = guardadas.get(p.getId());
            if (valores == null || valores.isEmpty()) {
                continue;
            }
            List<String> textos = new ArrayList<String>();
            for (String valor : valores) {
                Cuestionario.Opcion opcion = p.opcion(valor);
                if (opcion != null) {
                    textos.add(opcion.getTexto());
                }
            }
            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append(p.getEnunciado()).append(" ").append(String.join(" / ", textos));
        }
        return sb.toString();
    }

    private Map<String, Integer> puntajes(Cuestionario.Nivel nivel, Map<String, List<String>> respuestas) {
        Map<String, Integer> puntos = new LinkedHashMap<String, Integer>();
        for (Cuestionario.Pregunta p : nivel.getPreguntas()) {
            List<String> valores = respuestas.get(p.getId());
            if (valores == null) {
                continue;
            }
            for (String valor : valores) {
                Cuestionario.Opcion opcion = p.opcion(valor);
                if (opcion == null) {
                    continue;
                }
                for (Map.Entry<String, Integer> e : opcion.getPuntos().entrySet()) {
                    puntos.merge(e.getKey(), e.getValue(), Integer::sum);
                }
            }
        }
        return puntos;
    }

    private String ganador(Map<String, Integer> puntos, List<String> orden) {
        String mejor = orden.get(0);
        int max = -1;
        for (String clave : orden) {
            int valor = puntos.getOrDefault(clave, 0);
            if (valor > max) {
                max = valor;
                mejor = clave;
            }
        }
        return mejor;
    }

    private String subtono(Map<String, Integer> puntos) {
        int calido = puntos.getOrDefault("calido", 0);
        int frio = puntos.getOrDefault("frio", 0);
        int neutro = puntos.getOrDefault("neutro", 0);
        if (neutro >= calido && neutro >= frio) {
            return "neutro";
        }
        if (Math.abs(calido - frio) <= 1) {
            return "neutro";
        }
        return calido > frio ? "calido" : "frio";
    }

    private List<String> ordenCuerpo(String genero) {
        if ("masculino".equalsIgnoreCase(genero)) {
            return Arrays.asList("trapecio", "rectangulo", "triangulo", "triangulo_invertido", "ovalado");
        }
        return Arrays.asList("reloj", "triangulo", "triangulo_invertido", "rectangulo", "ovalado");
    }

    private String serializar(Map<String, List<String>> respuestas) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<String>> e : respuestas.entrySet()) {
            if (sb.length() > 0) {
                sb.append("|");
            }
            sb.append(e.getKey()).append("=").append(String.join(",", e.getValue()));
        }
        return sb.toString();
    }
}
