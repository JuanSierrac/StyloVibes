package com.stylovibes.service.impl;

import com.stylovibes.entity.TipoCorporal;
import com.stylovibes.entity.enums.Genero;
import com.stylovibes.exception.ResourceNotFoundException;
import com.stylovibes.repository.TipoCorporalRepository;
import com.stylovibes.service.TipoCorporalService;
import lombok.RequiredArgsConstructor;
import com.stylovibes.repository.Repositorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoCorporalServiceImpl extends AbstractCatalogoService<TipoCorporal> implements TipoCorporalService {

    private final TipoCorporalRepository repository;

    @Override
    protected Repositorio<TipoCorporal> repo() { return repository; }

    @Override
    protected String nombreEntidad() { return "tipo corporal"; }

    @Override
    protected List<TipoCorporal> consultarActivos() { return repository.findByActivoTrueOrderByIdAsc(); }

    @Override
    public List<TipoCorporal> listarPorGenero(Genero genero) {
        return repository.findByGeneroAndActivoTrueOrderByIdAsc(genero);
    }
}
