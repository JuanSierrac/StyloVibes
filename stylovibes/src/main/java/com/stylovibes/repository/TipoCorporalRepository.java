package com.stylovibes.repository;

import com.stylovibes.entity.*;
import com.stylovibes.entity.enums.Genero;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TipoCorporalRepository extends InMemoryRepository<TipoCorporal> {

    public TipoCorporalRepository() {
        super(TipoCorporal::getId, TipoCorporal::setId);
    }

    public List<TipoCorporal> findByActivoTrueOrderByIdAsc() {
        return findAll().stream().filter(TipoCorporal::isActivo).toList();
    }

    public List<TipoCorporal> findByGeneroAndActivoTrueOrderByIdAsc(Genero genero) {
        return findAll().stream().filter(TipoCorporal::isActivo)
                .filter(tipo -> tipo.getGenero() == genero).toList();
    }
}
