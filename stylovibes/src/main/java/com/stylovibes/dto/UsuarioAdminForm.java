package com.stylovibes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UsuarioAdminForm {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 80)
    private String apellido;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Correo electrónico inválido")
    private String email;

    /** Obligatoria al crear; opcional al editar (vacía = no cambia). */
    @Size(max = 60, message = "Máximo 60 caracteres")
    private String password;

    private boolean activo = true;

    @NotEmpty(message = "Selecciona al menos un rol")
    private List<String> roles = new ArrayList<>();
}
