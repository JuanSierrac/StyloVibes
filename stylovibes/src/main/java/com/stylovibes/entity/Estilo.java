package com.stylovibes.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estilo extends BaseAuditEntity {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 60, message = "El nombre admite máximo 60 caracteres")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    private String caracteristicas;

    private String ejemplos;

    private String prendasRepresentativas;

    private String inspiracionVisual;

    /** Color HEX representativo (ej. #C9B79C) usado en la UI. */
    @Size(max = 9)
    private String colorPrincipal;

    @Size(max = 500)
    private String imagenUrl;

    @Builder.Default
    private boolean activo = true;
}
