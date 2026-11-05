package com.stylovibes.repository;

import com.stylovibes.entity.Articulo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Comparator;

@Repository
public class ArticuloRepository extends InMemoryRepository<Articulo> {

    public ArticuloRepository() {
        super(Articulo::getId, Articulo::setId);
    }

    public List<Articulo> findAllByOrderByFechaCreacionDesc() {
        return findAll().stream().sorted(porFechaDesc()).toList();
    }

    public List<Articulo> findByPublicadoTrueOrderByFechaCreacionDesc() {
        return publicados().toList();
    }

    public List<Articulo> findByPublicadoTrueAndCategoriaIdOrderByFechaCreacionDesc(Long categoriaId) {
        return publicados().filter(articulo -> articulo.getCategoria() != null
                && articulo.getCategoria().getId().equals(categoriaId)).toList();
    }

    public List<Articulo> findTop3ByPublicadoTrueOrderByFechaCreacionDesc() {
        return publicados().limit(3).toList();
    }

    public Optional<Articulo> findBySlugAndPublicadoTrue(String slug) {
        return findAll().stream().filter(articulo -> articulo.isPublicado() && articulo.getSlug().equals(slug)).findFirst();
    }

    public boolean existsBySlug(String slug) {
        return findAll().stream().anyMatch(articulo -> articulo.getSlug().equals(slug));
    }

    private java.util.stream.Stream<Articulo> publicados() {
        return findAll().stream().filter(Articulo::isPublicado).sorted(porFechaDesc());
    }

    private Comparator<Articulo> porFechaDesc() {
        return Comparator.comparing(Articulo::getFechaCreacion, Comparator.nullsLast(Comparator.reverseOrder()));
    }
}
