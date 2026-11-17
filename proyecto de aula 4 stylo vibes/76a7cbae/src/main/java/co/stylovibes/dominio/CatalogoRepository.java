package co.stylovibes.dominio;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CatalogoRepository extends JpaRepository<Catalogo, Long> {
    Optional<Catalogo> findByUsuario(Usuario usuario);
    void deleteByUsuario(Usuario usuario);
}
