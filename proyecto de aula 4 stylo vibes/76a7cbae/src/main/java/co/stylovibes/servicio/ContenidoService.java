package co.stylovibes.servicio;

import co.stylovibes.dominio.Contenido;
import co.stylovibes.dominio.ContenidoRepository;
import co.stylovibes.dominio.SeccionContenido;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContenidoService {

    private final ContenidoRepository contenidoRepository;

    public ContenidoService(ContenidoRepository contenidoRepository) {
        this.contenidoRepository = contenidoRepository;
    }

    public Contenido porClave(String clave) {
        return contenidoRepository.findByClave(clave).orElse(null);
    }

    public Contenido porId(Long id) {
        return contenidoRepository.findById(id).orElse(null);
    }

    public String texto(String clave) {
        Contenido c = porClave(clave);
        return c == null ? "" : c.getTexto();
    }

    public List<Contenido> porSeccion(SeccionContenido seccion) {
        return contenidoRepository.findBySeccionAndPublicadoTrueOrderByOrdenAsc(seccion);
    }

    public List<Contenido> todos() {
        return contenidoRepository.findAllByOrderBySeccionAscOrdenAsc();
    }

    @Transactional
    public Contenido guardar(Contenido contenido) {
        contenido.setFechaActualizacion(LocalDateTime.now());
        return contenidoRepository.save(contenido);
    }

    @Transactional
    public void eliminar(Long id) {
        contenidoRepository.deleteById(id);
    }

    public boolean claveDisponible(String clave, Long idActual) {
        Contenido existente = porClave(clave);
        return existente == null || existente.getId().equals(idActual);
    }
}
