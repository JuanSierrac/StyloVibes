package com.stylovibes.service;

import com.stylovibes.dto.RecomendacionForm;
import com.stylovibes.entity.Recomendacion;

import java.util.List;

public interface RecomendacionService {
    List<Recomendacion> listar();
    RecomendacionForm aFormulario(Long id);
    Recomendacion crear(RecomendacionForm form);
    Recomendacion actualizar(Long id, RecomendacionForm form);
    void eliminar(Long id);
    List<Recomendacion> porTipoCorporal(Long id);
    List<Recomendacion> porTipoRostro(Long id);
    List<Recomendacion> porColorimetria(Long id);
    List<Recomendacion> porEstilo(Long id);
}
