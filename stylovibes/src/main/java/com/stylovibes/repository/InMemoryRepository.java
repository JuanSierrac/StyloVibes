package com.stylovibes.repository;

import com.stylovibes.entity.BaseAuditEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class InMemoryRepository<T extends BaseAuditEntity> implements Repositorio<T> {

    private final Map<Long, T> entities = new HashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);
    private final Function<T, Long> idGetter;
    private final BiConsumer<T, Long> idSetter;

    protected InMemoryRepository(Function<T, Long> idGetter, BiConsumer<T, Long> idSetter) {
        this.idGetter = idGetter;
        this.idSetter = idSetter;
    }

    public synchronized T save(T entity) {
        LocalDateTime now = LocalDateTime.now();
        Long id = idGetter.apply(entity);
        if (id == null) {
            id = nextId.getAndIncrement();
            idSetter.accept(entity, id);
            entity.setFechaCreacion(now);
        } else {
            nextId.accumulateAndGet(id + 1, Math::max);
            if (entity.getFechaCreacion() == null) entity.setFechaCreacion(now);
        }
        entity.setFechaActualizacion(now);
        entities.put(id, entity);
        return entity;
    }

    public synchronized List<T> saveAll(Iterable<T> values) {
        List<T> saved = new ArrayList<>();
        values.forEach(value -> saved.add(save(value)));
        return saved;
    }

    public synchronized Optional<T> findById(Long id) {
        return Optional.ofNullable(entities.get(id));
    }

    public synchronized List<T> findAll() {
        return entities.values().stream()
                .sorted(Comparator.comparing(idGetter, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }

    public synchronized long count() {
        return entities.size();
    }

    public synchronized void delete(T entity) {
        Long id = idGetter.apply(entity);
        if (id != null) entities.remove(id);
    }

    public synchronized void deleteById(Long id) {
        entities.remove(id);
    }
}