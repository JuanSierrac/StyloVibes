package com.stylovibes.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoRostro extends BaseAuditEntity {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80, message = "El nombre admite máximo 80 caracteres")
    private String nombre;

    @NotBlank(message = "Las características son obligatorias")
    private String caracteristicas;

    private String peinadosRecomendados;

    private String accesoriosRecomendados;

    private String gafasRecomendadas;

    private String recomendacionesGenerales;

    @Size(max = 500)
    private String imagenUrl;

    @Builder.Default
    private boolean activo = true;
}
