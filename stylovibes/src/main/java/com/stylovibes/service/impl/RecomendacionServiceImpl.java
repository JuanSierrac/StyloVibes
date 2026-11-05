package com.stylovibes.service.impl;

import com.stylovibes.dto.RecomendacionForm;
import com.stylovibes.entity.Recomendacion;
import com.stylovibes.exception.BusinessException;
import com.stylovibes.exception.ResourceNotFoundException;
import com.stylovibes.repository.*;
import com.stylovibes.service.RecomendacionService;
import lombok.RequiredArgsConstructor;
import com.stylovibes.repository.Repositorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecomendacionServiceImpl implements RecomendacionService {

    private final RecomendacionRepository recomendacionRepository;
    private final TipoCorporalRepository tipoCorporalRepository;
    private final TipoRostroRepository tipoRostroRepository;
    private final ColorimetriaRepository colorimetriaRepository;
    private final EstiloRepository estiloRepository;

    @Override
    public List<Recomendacion> listar() {
        return recomendacionRepository.findAllByOrderByIdDesc();
    }

    @Override
    public RecomendacionForm aFormulario(Long id) {
        Recomendacion r = obtener(id);
        RecomendacionForm f = new RecomendacionForm();
        f.setId(r.getId());
        f.setTitulo(r.getTitulo());
        f.setContenido(r.getContenido());
        f.setActivo(r.isActivo());
        if (r.getTipoCorporal() != null) f.setTipoCorporalId(r.getTipoCorporal().getId());
        if (r.getTipoRostro() != null) f.setTipoRostroId(r.getTipoRostro().getId());
        if (r.getColorimetria() != null) f.setColorimetriaId(r.getColorimetria().getId());
        if (r.getEstilo() != null) f.setEstiloId(r.getEstilo().getId());
        return f;
    }

    @Override
    public Recomendacion crear(RecomendacionForm f) {
        return recomendacionRepository.save(aplicar(new Recomendacion(), f));
    }

    @Override
    public Recomendacion actualizar(Long id, RecomendacionForm f) {
        return recomendacionRepository.save(aplicar(obtener(id), f));
    }

    @Override
    public void eliminar(Long id) {
        recomendacionRepository.delete(obtener(id));
    }

    @Override
    public List<Recomendacion> porTipoCorporal(Long id) {
        return recomendacionRepository.findByTipoCorporalIdAndActivoTrue(id);
    }

    @Override
    public List<Recomendacion> porTipoRostro(Long id) {
        return recomendacionRepository.findByTipoRostroIdAndActivoTrue(id);
    }

    @Override
    public List<Recomendacion> porColorimetria(Long id) {
        return recomendacionRepository.findByColorimetriaIdAndActivoTrue(id);
    }

    @Override
    public List<Recomendacion> porEstilo(Long id) {
        return recomendacionRepository.findByEstiloIdAndActivoTrue(id);
    }

    // ----- helpers -----

    private Recomendacion obtener(Long id) {
        return recomendacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recomendación no encontrada (id " + id + ")"));
    }

    private Recomendacion aplicar(Recomendacion r, RecomendacionForm f) {
        if (f.getTipoCorporalId() == null && f.getTipoRostroId() == null
                && f.getColorimetriaId() == null && f.getEstiloId() == null) {
            throw new BusinessException("Asocia la recomendación al menos a una dimensión (cuerpo, rostro, color o estilo)");
        }
        r.setTitulo(f.getTitulo().trim());
        r.setContenido(f.getContenido());
        r.setActivo(f.isActivo());
        r.setTipoCorporal(buscar(f.getTipoCorporalId(), tipoCorporalRepository));
        r.setTipoRostro(buscar(f.getTipoRostroId(), tipoRostroRepository));
        r.setColorimetria(buscar(f.getColorimetriaId(), colorimetriaRepository));
        r.setEstilo(buscar(f.getEstiloId(), estiloRepository));
        return r;
    }

    private <T extends com.stylovibes.entity.BaseAuditEntity> T buscar(Long id, Repositorio<T> repo) {
        if (id == null) return null;
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Referencia no encontrada (id " + id + ")"));
    }
}
