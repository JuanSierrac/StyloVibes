package com.stylovibes.dto;

import com.stylovibes.entity.enums.Genero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PerfilRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 80)
    private String apellido;

    private Genero genero;

    @Size(max = 500, message = "Máximo 500 caracteres")
    private String bio;

    private Long tipoCorporalId;
    private Long tipoRostroId;
    private Long colorimetriaId;
    private Long estiloId;
}
