package com.stylovibes.entity;

import com.stylovibes.entity.enums.Genero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/** Tipología corporal. Los campos de lista usan ';' como separador. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoCorporal extends BaseAuditEntity {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80, message = "El nombre admite máximo 80 caracteres")
    private String nombre;

    @NotNull(message = "El género es obligatorio")
    private Genero genero;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    private String fortalezas;

    private String recomendacionesVestuario;

    private String prendasRecomendadas;

    private String prendasEvitar;

    @Size(max = 500)
    private String imagenUrl;

    @Builder.Default
    private boolean activo = true;
}
