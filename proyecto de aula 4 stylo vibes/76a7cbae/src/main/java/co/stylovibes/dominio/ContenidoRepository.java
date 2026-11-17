package co.stylovibes.dominio;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ContenidoRepository extends JpaRepository<Contenido, Long> {
    Optional<Contenido> findByClave(String clave);
    List<Contenido> findAllByOrderBySeccionAscOrdenAsc();
    List<Contenido> findByPublicadoTrueOrderBySeccionAscOrdenAsc();
    List<Contenido> findBySeccionAndPublicadoTrueOrderByOrdenAsc(SeccionContenido seccion);
}
