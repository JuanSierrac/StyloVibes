package com.stylovibes.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Estación de color. Los colores se guardan como "Nombre:#HEX;Nombre:#HEX".
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Colorimetria extends BaseAuditEntity {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 40, message = "El nombre admite máximo 40 caracteres")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    private String paletaColores;

    private String coloresIdeales;

    private String coloresEvitar;

    private String ejemplos;

    @Builder.Default
    private boolean activo = true;
}
