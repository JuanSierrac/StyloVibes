package com.stylovibes.repository;

import com.stylovibes.entity.BaseAuditEntity;

import java.util.List;
import java.util.Optional;

public interface Repositorio<T extends BaseAuditEntity> {
    T save(T entity);
    List<T> saveAll(Iterable<T> values);
    Optional<T> findById(Long id);
    List<T> findAll();
    long count();
    void delete(T entity);
    void deleteById(Long id);
}