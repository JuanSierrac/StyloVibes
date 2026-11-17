package co.stylovibes.dominio;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Long> {
    Optional<Caracteristica> findByUsuario(Usuario usuario);
}
