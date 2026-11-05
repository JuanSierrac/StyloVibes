package com.stylovibes.repository;

import com.stylovibes.entity.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TipoRostroRepository extends InMemoryRepository<TipoRostro> {

    public TipoRostroRepository() {
        super(TipoRostro::getId, TipoRostro::setId);
    }

    public List<TipoRostro> findByActivoTrueOrderByIdAsc() {
        return findAll().stream().filter(TipoRostro::isActivo).toList();
    }
}
