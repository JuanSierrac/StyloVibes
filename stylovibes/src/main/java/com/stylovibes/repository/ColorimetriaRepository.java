package com.stylovibes.repository;

import com.stylovibes.entity.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ColorimetriaRepository extends InMemoryRepository<Colorimetria> {

    public ColorimetriaRepository() {
        super(Colorimetria::getId, Colorimetria::setId);
    }

    public List<Colorimetria> findByActivoTrueOrderByIdAsc() {
        return findAll().stream().filter(Colorimetria::isActivo).toList();
    }

    public Optional<Colorimetria> findByNombreIgnoreCase(String nombre) {
        return findAll().stream().filter(colorimetria -> colorimetria.getNombre().equalsIgnoreCase(nombre)).findFirst();
    }
}
