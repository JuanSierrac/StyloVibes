package com.stylovibes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotBlank
    private String token;

    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,60}$",
            message = "Mínimo 8 caracteres, con letras y números")
    private String password;

    @NotBlank(message = "Confirma tu contraseña")
    private String confirmarPassword;
}
