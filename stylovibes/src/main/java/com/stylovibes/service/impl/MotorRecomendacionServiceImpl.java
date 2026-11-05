package com.stylovibes.service.impl;

import com.stylovibes.dto.PerfilCompletoResponse;
import com.stylovibes.entity.*;
import com.stylovibes.repository.RecomendacionRepository;
import com.stylovibes.service.MotorRecomendacionService;
import com.stylovibes.service.PerfilService;
import com.stylovibes.util.TextoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Motor basado en reglas: une el contenido curado de cada dimensión del perfil
 * (cuerpo, rostro, color, estilo) y genera combinaciones y consejos.
 */
@Service
@RequiredArgsConstructor
public class MotorRecomendacionServiceImpl implements MotorRecomendacionService {

    private static final int MAX_COMBINACIONES = 4;

    private final PerfilService perfilService;
    private final RecomendacionRepository recomendacionRepository;

    @Override
    public PerfilCompletoResponse generar(String emailUsuario) {
        PerfilUsuario perfil = perfilService.obtenerPorEmail(emailUsuario);
        TipoCorporal cuerpo = perfil.getTipoCorporal();
        TipoRostro rostro = perfil.getTipoRostro();
        Colorimetria color = perfil.getColorimetria();
        Estilo estilo = perfil.getEstiloPreferido();

        return PerfilCompletoResponse.builder()
                .nombreUsuario(perfil.getUsuario().getNombre())
                .tipoCorporal(cuerpo)
                .tipoRostro(rostro)
                .colorimetria(color)
                .estilo(estilo)
                .recomendaciones(recomendacionesDe(cuerpo, rostro, color, estilo))
                .combinaciones(combinaciones(cuerpo, color, estilo))
                .consejos(consejos(cuerpo, rostro, color, estilo))
                .completitud(perfil.getCompletitud())
                .build();
    }

    private List<Recomendacion> recomendacionesDe(TipoCorporal c, TipoRostro r, Colorimetria col, Estilo e) {
        Set<Recomendacion> set = new LinkedHashSet<>();
        if (c != null) set.addAll(recomendacionRepository.findByTipoCorporalIdAndActivoTrue(c.getId()));
        if (r != null) set.addAll(recomendacionRepository.findByTipoRostroIdAndActivoTrue(r.getId()));
        if (col != null) set.addAll(recomendacionRepository.findByColorimetriaIdAndActivoTrue(col.getId()));
        if (e != null) set.addAll(recomendacionRepository.findByEstiloIdAndActivoTrue(e.getId()));
        return new ArrayList<>(set);
    }

    /** Cruza prendas del cuerpo + colores ideales + prendas del estilo. */
    private List<String> combinaciones(TipoCorporal c, Colorimetria col, Estilo e) {
        List<String> prendas = TextoUtil.partir(c != null ? c.getPrendasRecomendadas() : null);
        List<String> colores = TextoUtil.nombresDeColor(col != null ? col.getColoresIdeales() : null);
        List<String> piezasEstilo = TextoUtil.partir(e != null ? e.getPrendasRepresentativas() : null);

        int n = Math.min(MAX_COMBINACIONES, Math.max(prendas.size(), Math.max(colores.size(), piezasEstilo.size())));
        List<String> resultado = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String prenda = TextoUtil.elemento(prendas, i);
            String tono = TextoUtil.elemento(colores, i);
            String pieza = TextoUtil.elemento(piezasEstilo, i + 1);
            List<String> partes = new ArrayList<>();
            if (prenda != null) partes.add(prenda + (tono != null ? " en tono " + tono : ""));
            else if (tono != null) partes.add("Prenda principal en tono " + tono);
            if (pieza != null) partes.add("combinada con " + pieza.toLowerCase());
            String texto = String.join(" ", partes);
            if (e != null) texto += " · estilo " + e.getNombre();
            resultado.add("Look " + (i + 1) + ": " + texto);
        }
        return resultado;
    }

    private List<String> consejos(TipoCorporal c, TipoRostro r, Colorimetria col, Estilo e) {
        List<String> consejos = new ArrayList<>();
        if (c != null) {
            agregar(consejos, "Vestuario", TextoUtil.elemento(TextoUtil.partir(c.getRecomendacionesVestuario()), 0));
        }
        if (r != null) {
            agregar(consejos, "Peinado", TextoUtil.elemento(TextoUtil.partir(r.getPeinadosRecomendados()), 0));
            agregar(consejos, "Gafas", TextoUtil.elemento(TextoUtil.partir(r.getGafasRecomendadas()), 0));
            agregar(consejos, "Accesorios", TextoUtil.elemento(TextoUtil.partir(r.getAccesoriosRecomendados()), 0));
        }
        if (col != null) {
            List<String> ideales = TextoUtil.nombresDeColor(col.getColoresIdeales());
            if (!ideales.isEmpty()) {
                consejos.add("Color: tu estación es " + col.getNombre() + "; prioriza "
                        + String.join(", ", ideales.subList(0, Math.min(3, ideales.size()))) + " cerca del rostro.");
            }
        }
        if (e != null) {
            agregar(consejos, "Inspiración " + e.getNombre(), e.getInspiracionVisual());
        }
        consejos.add("Imagen: la prenda mejor elegida es la que te hace sentir cómodo y seguro, adapta cada tendencia a tu personalidad.");
        return consejos;
    }

    private void agregar(List<String> lista, String etiqueta, String valor) {
        if (valor != null && !valor.isBlank()) lista.add(etiqueta + ": " + valor);
    }
}
