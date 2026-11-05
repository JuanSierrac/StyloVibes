package com.stylovibes.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Articulo extends BaseAuditEntity {

    private Long id;

    private String titulo;

    private String slug;

    private String resumen;

    private String contenido;

    private String imagenUrl;

    @Builder.Default
    private boolean publicado = true;

    private CategoriaArticulo categoria;

    private Usuario autor;

    public String getCategoriaNombre() {
        return categoria != null ? categoria.getNombre() : "";
    }

    public String getAutorNombre() {
        return autor != null ? autor.getNombreCompleto() : "Equipo StyloVibes";
    }
}
