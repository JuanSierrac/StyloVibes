package com.stylovibes.repository;

import com.stylovibes.entity.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RolRepository extends InMemoryRepository<Rol> {

    public RolRepository() {
        super(Rol::getId, Rol::setId);
    }

    public Optional<Rol> findByNombre(String nombre) {
        return findAll().stream().filter(rol -> rol.getNombre().equals(nombre)).findFirst();
    }
}
