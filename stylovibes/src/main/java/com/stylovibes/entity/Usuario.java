package com.stylovibes.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class Usuario extends BaseAuditEntity {

    private Long id;

    private String nombre;

    private String apellido;

    private String email;

    private String password;

    private boolean activo = true;

    private Set<Rol> roles = new HashSet<>();

    private PerfilUsuario perfil;

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public boolean esAdmin() {
        return roles.stream().anyMatch(r -> "ADMIN".equals(r.getNombre()));
    }
}
