package com.stylovibes.service.impl;

import com.stylovibes.entity.Estilo;
import com.stylovibes.entity.enums.Genero;
import com.stylovibes.exception.ResourceNotFoundException;
import com.stylovibes.repository.EstiloRepository;
import com.stylovibes.service.EstiloService;
import lombok.RequiredArgsConstructor;
import com.stylovibes.repository.Repositorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstiloServiceImpl extends AbstractCatalogoService<Estilo> implements EstiloService {

    private final EstiloRepository repository;

    @Override
    protected Repositorio<Estilo> repo() { return repository; }

    @Override
    protected String nombreEntidad() { return "estilo"; }

    @Override
    protected List<Estilo> consultarActivos() { return repository.findByActivoTrueOrderByIdAsc(); }

}
