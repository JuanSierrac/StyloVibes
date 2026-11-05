package com.stylovibes.service.impl;

import com.stylovibes.entity.TipoRostro;
import com.stylovibes.entity.enums.Genero;
import com.stylovibes.exception.ResourceNotFoundException;
import com.stylovibes.repository.TipoRostroRepository;
import com.stylovibes.service.TipoRostroService;
import lombok.RequiredArgsConstructor;
import com.stylovibes.repository.Repositorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoRostroServiceImpl extends AbstractCatalogoService<TipoRostro> implements TipoRostroService {

    private final TipoRostroRepository repository;

    @Override
    protected Repositorio<TipoRostro> repo() { return repository; }

    @Override
    protected String nombreEntidad() { return "tipo de rostro"; }

    @Override
    protected List<TipoRostro> consultarActivos() { return repository.findByActivoTrueOrderByIdAsc(); }

}
