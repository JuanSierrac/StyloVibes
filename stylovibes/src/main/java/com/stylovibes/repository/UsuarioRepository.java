package com.stylovibes.repository;

import com.stylovibes.entity.Usuario;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UsuarioRepository extends InMemoryRepository<Usuario> {

    private final PerfilUsuarioRepository perfilRepository;

    public UsuarioRepository(PerfilUsuarioRepository perfilRepository) {
        super(Usuario::getId, Usuario::setId);
        this.perfilRepository = perfilRepository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        Usuario guardado = super.save(usuario);
        if (guardado.getPerfil() != null) {
            guardado.getPerfil().setUsuario(guardado);
            perfilRepository.save(guardado.getPerfil());
        }
        return guardado;
    }

    @Override
    public void delete(Usuario usuario) {
        perfilRepository.deleteByUsuario(usuario);
        super.delete(usuario);
    }

    public Optional<Usuario> findByEmailIgnoreCase(String email) {
        return findAll().stream().filter(usuario -> usuario.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    public boolean existsByEmailIgnoreCase(String email) {
        return findByEmailIgnoreCase(email).isPresent();
    }

    public boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id) {
        return findAll().stream().anyMatch(usuario -> !usuario.getId().equals(id)
                && usuario.getEmail().equalsIgnoreCase(email));
    }
}
