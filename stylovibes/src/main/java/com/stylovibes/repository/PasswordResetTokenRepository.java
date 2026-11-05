package com.stylovibes.repository;

import com.stylovibes.entity.PasswordResetToken;
import com.stylovibes.entity.Usuario;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PasswordResetTokenRepository extends InMemoryRepository<PasswordResetToken> {

    public PasswordResetTokenRepository() {
        super(PasswordResetToken::getId, PasswordResetToken::setId);
    }

    public Optional<PasswordResetToken> findByToken(String token) {
        return findAll().stream().filter(resetToken -> resetToken.getToken().equals(token)).findFirst();
    }

    public void deleteByUsuario(Usuario usuario) {
        findAll().stream().filter(resetToken -> resetToken.getUsuario() == usuario).forEach(this::delete);
    }
}
