package com.stylovibes.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Rol de seguridad: USER, ADMIN (authority = "ROLE_" + nombre). */
@Getter
@Setter
@NoArgsConstructor
public class Rol extends BaseAuditEntity {

    private Long id;

    private String nombre;

    public Rol(String nombre) {
        this.nombre = nombre;
    }
}
