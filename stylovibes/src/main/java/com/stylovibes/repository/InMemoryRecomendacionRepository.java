package com.stylovibes.repository;

import com.stylovibes.entity.Recomendacion;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InMemoryRecomendacionRepository extends InMemoryRepository<Recomendacion> implements RecomendacionRepository {

    public InMemoryRecomendacionRepository() {
        super(Recomendacion::getId, Recomendacion::setId);
    }

    @Override
    public List<Recomendacion> findAllByOrderByIdDesc() {
        return findAll().stream().sorted((left, right) -> right.getId().compareTo(left.getId())).toList();
    }

    @Override
    public List<Recomendacion> findByTipoCorporalIdAndActivoTrue(Long id) {
        return findAll().stream().filter(Recomendacion::isActivo)
                .filter(rec -> rec.getTipoCorporal() != null && rec.getTipoCorporal().getId().equals(id)).toList();
    }

    @Override
    public List<Recomendacion> findByTipoRostroIdAndActivoTrue(Long id) {
        return findAll().stream().filter(Recomendacion::isActivo)
                .filter(rec -> rec.getTipoRostro() != null && rec.getTipoRostro().getId().equals(id)).toList();
    }

    @Override
    public List<Recomendacion> findByColorimetriaIdAndActivoTrue(Long id) {
        return findAll().stream().filter(Recomendacion::isActivo)
                .filter(rec -> rec.getColorimetria() != null && rec.getColorimetria().getId().equals(id)).toList();
    }

    @Override
    public List<Recomendacion> findByEstiloIdAndActivoTrue(Long id) {
        return findAll().stream().filter(Recomendacion::isActivo)
                .filter(rec -> rec.getEstilo() != null && rec.getEstilo().getId().equals(id)).toList();
    }
}