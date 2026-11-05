package com.stylovibes.entity;

import com.stylovibes.entity.enums.Genero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PerfilUsuario extends BaseAuditEntity {

    private Long id;

    private Usuario usuario;

    private Genero genero;

    private String bio;

    private TipoCorporal tipoCorporal;

    private TipoRostro tipoRostro;

    private Colorimetria colorimetria;

    private Estilo estiloPreferido;

    /** Porcentaje (0-100) de dimensiones del diagnóstico completadas. */
    public int getCompletitud() {
        int total = 0;
        if (tipoCorporal != null) total++;
        if (tipoRostro != null) total++;
        if (colorimetria != null) total++;
        if (estiloPreferido != null) total++;
        return total * 25;
    }
}
