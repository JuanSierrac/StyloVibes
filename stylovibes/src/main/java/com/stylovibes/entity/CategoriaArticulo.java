package com.stylovibes.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaArticulo extends BaseAuditEntity {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80, message = "El nombre admite máximo 80 caracteres")
    private String nombre;

    @Size(max = 300)
    private String descripcion;

    @Builder.Default
    private boolean activo = true;

    @Builder.Default
    private List<Articulo> articulos = new ArrayList<>();
}
