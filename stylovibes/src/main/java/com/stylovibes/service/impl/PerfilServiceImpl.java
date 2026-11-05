package com.stylovibes.service.impl;

import com.stylovibes.dto.PerfilRequest;
import com.stylovibes.entity.PerfilUsuario;
import com.stylovibes.entity.TipoCorporal;
import com.stylovibes.entity.Usuario;
import com.stylovibes.exception.BusinessException;
import com.stylovibes.exception.ResourceNotFoundException;
import com.stylovibes.repository.*;
import com.stylovibes.service.PerfilService;
import lombok.RequiredArgsConstructor;
import com.stylovibes.repository.Repositorio;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerfilServiceImpl implements PerfilService {

    private final PerfilUsuarioRepository perfilRepository;
    private final TipoCorporalRepository tipoCorporalRepository;
    private final TipoRostroRepository tipoRostroRepository;
    private final ColorimetriaRepository colorimetriaRepository;
    private final EstiloRepository estiloRepository;

    @Override
    public PerfilUsuario obtenerPorEmail(String email) {
        return perfilRepository.findByUsuarioEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil no encontrado"));
    }

    @Override
    public void actualizar(String email, PerfilRequest r) {
        PerfilUsuario perfil = obtenerPorEmail(email);
        Usuario usuario = perfil.getUsuario();
        usuario.setNombre(r.getNombre().trim());
        usuario.setApellido(r.getApellido().trim());

        TipoCorporal cuerpo = buscar(r.getTipoCorporalId(), tipoCorporalRepository, "Tipo corporal");
        if (cuerpo != null && r.getGenero() != null && cuerpo.getGenero() != r.getGenero()) {
            throw new BusinessException("El tipo corporal elegido no corresponde al género seleccionado");
        }
        perfil.setGenero(r.getGenero() != null ? r.getGenero() : (cuerpo != null ? cuerpo.getGenero() : null));
        perfil.setBio(r.getBio());
        perfil.setTipoCorporal(cuerpo);
        perfil.setTipoRostro(buscar(r.getTipoRostroId(), tipoRostroRepository, "Tipo de rostro"));
        perfil.setColorimetria(buscar(r.getColorimetriaId(), colorimetriaRepository, "Estación de color"));
        perfil.setEstiloPreferido(buscar(r.getEstiloId(), estiloRepository, "Estilo"));
    }

    @Override
    public void asignarTipoCorporal(String email, Long id) {
        PerfilUsuario perfil = obtenerPorEmail(email);
        TipoCorporal tipo = buscar(id, tipoCorporalRepository, "Tipo corporal");
        perfil.setTipoCorporal(tipo);
        if (tipo != null) perfil.setGenero(tipo.getGenero());
    }

    @Override
    public void asignarTipoRostro(String email, Long id) {
        obtenerPorEmail(email).setTipoRostro(buscar(id, tipoRostroRepository, "Tipo de rostro"));
    }

    @Override
    public void asignarColorimetria(String email, Long id) {
        obtenerPorEmail(email).setColorimetria(buscar(id, colorimetriaRepository, "Estación de color"));
    }

    @Override
    public void asignarEstilo(String email, Long id) {
        obtenerPorEmail(email).setEstiloPreferido(buscar(id, estiloRepository, "Estilo"));
    }

    private <T extends com.stylovibes.entity.BaseAuditEntity> T buscar(Long id, Repositorio<T> repo, String nombre) {
        if (id == null) return null;
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException(nombre + " no encontrado"));
    }
}
