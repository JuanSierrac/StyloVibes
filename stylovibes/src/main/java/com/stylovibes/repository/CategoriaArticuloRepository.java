package com.stylovibes.repository;

import com.stylovibes.entity.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoriaArticuloRepository extends InMemoryRepository<CategoriaArticulo> {

    public CategoriaArticuloRepository() {
        super(CategoriaArticulo::getId, CategoriaArticulo::setId);
    }

    public List<CategoriaArticulo> findByActivoTrueOrderByIdAsc() {
        return findAll().stream().filter(CategoriaArticulo::isActivo).toList();
    }
}
