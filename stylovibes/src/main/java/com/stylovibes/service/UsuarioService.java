package com.stylovibes.service;

import com.stylovibes.dto.RegistroRequest;
import com.stylovibes.dto.UsuarioAdminForm;
import com.stylovibes.entity.Usuario;

import java.util.List;

public interface UsuarioService {
    Usuario registrar(RegistroRequest request);
    Usuario buscarPorEmail(String email);
    List<Usuario> listar();
    Usuario obtener(Long id);
    UsuarioAdminForm aFormulario(Long id);
    Usuario crearDesdeAdmin(UsuarioAdminForm form);
    Usuario actualizarDesdeAdmin(Long id, UsuarioAdminForm form);
    void eliminar(Long id, String emailSolicitante);
}
