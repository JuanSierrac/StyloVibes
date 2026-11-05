package com.stylovibes.dto;

import com.stylovibes.entity.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/** Resultado del motor de recomendaciones para un usuario. */
@Getter
@Builder
public class PerfilCompletoResponse {
    private final String nombreUsuario;
    private final TipoCorporal tipoCorporal;
    private final TipoRostro tipoRostro;
    private final Colorimetria colorimetria;
    private final Estilo estilo;
    private final List<Recomendacion> recomendaciones;
    private final List<String> combinaciones;
    private final List<String> consejos;
    private final int completitud;
}
