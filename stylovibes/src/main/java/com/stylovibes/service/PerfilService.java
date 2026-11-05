package com.stylovibes.service;

import com.stylovibes.dto.PerfilRequest;
import com.stylovibes.entity.PerfilUsuario;

public interface PerfilService {
    PerfilUsuario obtenerPorEmail(String email);
    void actualizar(String email, PerfilRequest request);
    void asignarTipoCorporal(String email, Long tipoCorporalId);
    void asignarTipoRostro(String email, Long tipoRostroId);
    void asignarColorimetria(String email, Long colorimetriaId);
    void asignarEstilo(String email, Long estiloId);
}
