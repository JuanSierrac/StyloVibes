package com.stylovibes.repository;

import com.stylovibes.entity.PerfilUsuario;
import com.stylovibes.entity.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryRepositoryTest {

    @Test
    void guardaYEliminaElPerfilJuntoConElUsuario() {
        PerfilUsuarioRepository perfiles = new PerfilUsuarioRepository();
        UsuarioRepository usuarios = new UsuarioRepository(perfiles);
        Usuario usuario = new Usuario();
        usuario.setEmail("ana@example.com");
        PerfilUsuario perfil = new PerfilUsuario();
        perfil.setUsuario(usuario);
        usuario.setPerfil(perfil);

        usuarios.save(usuario);

        assertNotNull(usuario.getId());
        assertNotNull(perfil.getId());
        assertEquals(usuario, perfiles.findByUsuarioEmailIgnoreCase("ANA@example.com").orElseThrow().getUsuario());

        usuarios.delete(usuario);

        assertFalse(perfiles.findByUsuarioEmailIgnoreCase("ana@example.com").isPresent());
        assertTrue(usuarios.findByEmailIgnoreCase("ana@example.com").isEmpty());
    }
}