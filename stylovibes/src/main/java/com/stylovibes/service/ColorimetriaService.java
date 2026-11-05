package com.stylovibes.service;

import com.stylovibes.entity.Colorimetria;

public interface ColorimetriaService extends CatalogoService<Colorimetria> {
    Colorimetria obtenerPorNombre(String nombre);
}
