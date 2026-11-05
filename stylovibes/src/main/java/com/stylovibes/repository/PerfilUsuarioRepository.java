package com.stylovibes.repository;

import com.stylovibes.entity.PerfilUsuario;
import com.stylovibes.entity.Usuario;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PerfilUsuarioRepository extends InMemoryRepository<PerfilUsuario> {

    public PerfilUsuarioRepository() {
        super(PerfilUsuario::getId, PerfilUsuario::setId);
    }

    public Optional<PerfilUsuario> findByUsuarioEmailIgnoreCase(String email) {
        return findAll().stream().filter(perfil -> perfil.getUsuario() != null
                && perfil.getUsuario().getEmail().equalsIgnoreCase(email)).findFirst();
    }

    public void deleteByUsuario(Usuario usuario) {
        findAll().stream().filter(perfil -> perfil.getUsuario() != null
                && perfil.getUsuario().getId().equals(usuario.getId())).forEach(this::delete);
    }
}
