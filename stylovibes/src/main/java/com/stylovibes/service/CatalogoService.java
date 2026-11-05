package com.stylovibes.service;

import java.util.List;

/** Contrato CRUD genérico para catálogos administrables. */
public interface CatalogoService<T> {
    List<T> listar();
    List<T> listarActivos();
    T obtener(Long id);
    T crear(T entidad);
    T actualizar(Long id, T datos);
    void eliminar(Long id);
}
