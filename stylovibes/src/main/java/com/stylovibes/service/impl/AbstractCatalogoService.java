package com.stylovibes.service.impl;

import com.stylovibes.exception.ResourceNotFoundException;
import com.stylovibes.entity.BaseAuditEntity;
import com.stylovibes.service.CatalogoService;
import com.stylovibes.repository.Repositorio;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Plantilla CRUD reutilizable (Template Method). Las subclases solo aportan
 * repositorio, nombre legible y la consulta de registros activos.
 */
public abstract class AbstractCatalogoService<T extends BaseAuditEntity> implements CatalogoService<T> {

    protected abstract Repositorio<T> repo();

    protected abstract String nombreEntidad();

    protected abstract List<T> consultarActivos();

    /** Propiedades que nunca se copian al actualizar. */
    protected String[] propiedadesIgnoradas() {
        return new String[]{"id", "fechaCreacion", "fechaActualizacion", "articulos"};
    }

    @Override
    public List<T> listar() {
        return repo().findAll();
    }

    @Override
    public List<T> listarActivos() {
        return consultarActivos();
    }

    @Override
    public T obtener(Long id) {
        return repo().findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(nombreEntidad() + " no encontrado (id " + id + ")"));
    }

    @Override
    public T crear(T entidad) {
        return repo().save(entidad);
    }

    @Override
    public T actualizar(Long id, T datos) {
        T actual = obtener(id);
        BeanUtils.copyProperties(datos, actual, propiedadesIgnoradas());
        return repo().save(actual);
    }

    @Override
    public void eliminar(Long id) {
        T entidad = obtener(id);
        repo().delete(entidad);
    }
}
