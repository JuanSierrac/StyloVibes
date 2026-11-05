package com.stylovibes.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/** Recomendación asociada a una o varias dimensiones del diagnóstico. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recomendacion extends BaseAuditEntity {

    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150)
    private String titulo;

    @NotBlank(message = "El contenido es obligatorio")
    private String contenido;

    private TipoCorporal tipoCorporal;

    private TipoRostro tipoRostro;

    private Colorimetria colorimetria;

    private Estilo estilo;

    @Builder.Default
    private boolean activo = true;

    /** Texto descriptivo de a qué dimensiones aplica (solo lectura, no persistido). */
    public String getContexto() {
        List<String> partes = new ArrayList<>();
        if (tipoCorporal != null) partes.add("Cuerpo: " + tipoCorporal.getNombre());
        if (tipoRostro != null) partes.add("Rostro: " + tipoRostro.getNombre());
        if (colorimetria != null) partes.add("Color: " + colorimetria.getNombre());
        if (estilo != null) partes.add("Estilo: " + estilo.getNombre());
        return String.join(" · ", partes);
    }
}
