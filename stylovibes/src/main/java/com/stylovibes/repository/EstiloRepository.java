package com.stylovibes.repository;

import com.stylovibes.entity.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EstiloRepository extends InMemoryRepository<Estilo> {

    public EstiloRepository() {
        super(Estilo::getId, Estilo::setId);
    }

    public List<Estilo> findByActivoTrueOrderByIdAsc() {
        return findAll().stream().filter(Estilo::isActivo).toList();
    }
}
