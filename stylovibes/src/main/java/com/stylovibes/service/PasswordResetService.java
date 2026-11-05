package com.stylovibes.service;

public interface PasswordResetService {
    /** Genera token y envía el enlace. No revela si el correo existe. */
    void solicitar(String email);
    boolean tokenValido(String token);
    void restablecer(String token, String nuevaPassword);
}
