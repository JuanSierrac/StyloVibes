package com.stylovibes.service.impl;

import com.stylovibes.entity.PasswordResetToken;
import com.stylovibes.entity.Usuario;
import com.stylovibes.exception.BusinessException;
import com.stylovibes.repository.PasswordResetTokenRepository;
import com.stylovibes.repository.UsuarioRepository;
import com.stylovibes.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectProvider<JavaMailSender> mailSender;

    @Value("${stylovibes.base-url}")
    private String baseUrl;

    @Value("${stylovibes.password-reset.token-minutes}")
    private long minutosVigencia;

    @Value("${spring.mail.username:}")
    private String remitente;

    @Override
    public void solicitar(String email) {
        usuarioRepository.findByEmailIgnoreCase(email.trim()).ifPresent(usuario -> {
            tokenRepository.deleteByUsuario(usuario);
            PasswordResetToken token = new PasswordResetToken();
            token.setToken(UUID.randomUUID().toString().replace("-", ""));
            token.setUsuario(usuario);
            token.setExpiracion(LocalDateTime.now().plusMinutes(minutosVigencia));
            tokenRepository.save(token);
            enviar(usuario, baseUrl + "/reset?token=" + token.getToken());
        });
    }

    @Override
    public boolean tokenValido(String token) {
        return tokenRepository.findByToken(token).map(PasswordResetToken::estaVigente).orElse(false);
    }

    @Override
    public void restablecer(String token, String nuevaPassword) {
        PasswordResetToken t = tokenRepository.findByToken(token)
                .filter(PasswordResetToken::estaVigente)
                .orElseThrow(() -> new BusinessException("El enlace es inválido o ya expiró"));
        Usuario u = t.getUsuario();
        u.setPassword(passwordEncoder.encode(nuevaPassword));
        t.setUsado(true);
    }

    private void enviar(Usuario usuario, String enlace) {
        // Siempre se registra en consola: útil en desarrollo sin SMTP configurado.
        log.info("Enlace de recuperación para {}: {}", usuario.getEmail(), enlace);
        JavaMailSender sender = mailSender.getIfAvailable();
        if (sender == null || remitente == null || remitente.isBlank()) {
            return;
        }
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(remitente);
            msg.setTo(usuario.getEmail());
            msg.setSubject("StyloVibes · Recupera tu contraseña");
            msg.setText("Hola " + usuario.getNombre() + ",\n\nUsa este enlace para crear una nueva contraseña "
                    + "(vence en " + minutosVigencia + " minutos):\n" + enlace
                    + "\n\nSi no fuiste tú, ignora este mensaje.");
            sender.send(msg);
        } catch (Exception ex) {
            log.warn("No se pudo enviar el correo de recuperación: {}", ex.getMessage());
        }
    }
}
