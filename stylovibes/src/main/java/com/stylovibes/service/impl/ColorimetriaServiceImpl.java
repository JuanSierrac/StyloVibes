package com.stylovibes.service.impl;

import com.stylovibes.entity.Colorimetria;
import com.stylovibes.entity.enums.Genero;
import com.stylovibes.exception.ResourceNotFoundException;
import com.stylovibes.repository.ColorimetriaRepository;
import com.stylovibes.service.ColorimetriaService;
import lombok.RequiredArgsConstructor;
import com.stylovibes.repository.Repositorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorimetriaServiceImpl extends AbstractCatalogoService<Colorimetria> implements ColorimetriaService {

    private final ColorimetriaRepository repository;

    @Override
    protected Repositorio<Colorimetria> repo() { return repository; }

    @Override
    protected String nombreEntidad() { return "estación de color"; }

    @Override
    protected List<Colorimetria> consultarActivos() { return repository.findByActivoTrueOrderByIdAsc(); }

    @Override
    public Colorimetria obtenerPorNombre(String nombre) {
        return repository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new ResourceNotFoundException("Estación no encontrada: " + nombre));
    }
}
