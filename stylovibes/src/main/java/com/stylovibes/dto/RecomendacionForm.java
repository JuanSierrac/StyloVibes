package com.stylovibes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RecomendacionForm {

    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "Máximo 150 caracteres")
    private String titulo;

    @NotBlank(message = "El contenido es obligatorio")
    private String contenido;

    private Long tipoCorporalId;
    private Long tipoRostroId;
    private Long colorimetriaId;
    private Long estiloId;

    private boolean activo = true;
}
