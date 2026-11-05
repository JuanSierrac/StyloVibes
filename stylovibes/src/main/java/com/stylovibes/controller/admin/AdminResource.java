package com.stylovibes.controller.admin;

import com.stylovibes.service.CatalogoService;

import java.util.List;

/** Definición de un catálogo administrable (slug, clase, servicio y campos). */
public record AdminResource(String slug, String titulo, String singular, Class<?> tipo,
                            CatalogoService<Object> service,
                            List<FieldSpec> campos, List<FieldSpec> columnas) {

    public Object nuevaInstancia() {
        try {
            return tipo.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("No se pudo instanciar " + tipo.getSimpleName(), ex);
        }
    }
}
