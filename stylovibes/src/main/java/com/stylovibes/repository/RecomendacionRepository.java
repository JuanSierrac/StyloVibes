package com.stylovibes.repository;

import com.stylovibes.entity.Recomendacion;

import java.util.List;

public interface RecomendacionRepository extends Repositorio<Recomendacion> {
    List<Recomendacion> findAllByOrderByIdDesc();
    List<Recomendacion> findByTipoCorporalIdAndActivoTrue(Long id);
    List<Recomendacion> findByTipoRostroIdAndActivoTrue(Long id);
    List<Recomendacion> findByColorimetriaIdAndActivoTrue(Long id);
    List<Recomendacion> findByEstiloIdAndActivoTrue(Long id);
}
