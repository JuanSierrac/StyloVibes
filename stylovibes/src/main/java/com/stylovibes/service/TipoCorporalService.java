package com.stylovibes.service;

import com.stylovibes.entity.TipoCorporal;
import com.stylovibes.entity.enums.Genero;

import java.util.List;

public interface TipoCorporalService extends CatalogoService<TipoCorporal> {
    List<TipoCorporal> listarPorGenero(Genero genero);
}
