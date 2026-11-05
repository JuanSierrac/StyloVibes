package com.stylovibes.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PasswordResetToken extends BaseAuditEntity {

    private Long id;

    private String token;

    private Usuario usuario;

    private LocalDateTime expiracion;

    private boolean usado = false;

    public boolean estaVigente() {
        return !usado && expiracion.isAfter(LocalDateTime.now());
    }
}
