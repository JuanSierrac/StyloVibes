package com.stylovibes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ArticuloForm {

    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "Máximo 200 caracteres")
    private String titulo;

    @NotBlank(message = "El resumen es obligatorio")
    @Size(max = 300, message = "Máximo 300 caracteres")
    private String resumen;

    @NotBlank(message = "El contenido es obligatorio")
    private String contenido;

    @Size(max = 500)
    private String imagenUrl;

    @NotNull(message = "Selecciona una categoría")
    private Long categoriaId;

    private boolean publicado = true;
}
