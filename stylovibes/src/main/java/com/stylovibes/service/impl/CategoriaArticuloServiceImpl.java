package com.stylovibes.service.impl;

import com.stylovibes.entity.CategoriaArticulo;
import com.stylovibes.entity.enums.Genero;
import com.stylovibes.exception.ResourceNotFoundException;
import com.stylovibes.repository.CategoriaArticuloRepository;
import com.stylovibes.service.CategoriaArticuloService;
import lombok.RequiredArgsConstructor;
import com.stylovibes.repository.Repositorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaArticuloServiceImpl extends AbstractCatalogoService<CategoriaArticulo> implements CategoriaArticuloService {

    private final CategoriaArticuloRepository repository;

    @Override
    protected Repositorio<CategoriaArticulo> repo() { return repository; }

    @Override
    protected String nombreEntidad() { return "categoría"; }

    @Override
    protected List<CategoriaArticulo> consultarActivos() { return repository.findByActivoTrueOrderByIdAsc(); }

}
