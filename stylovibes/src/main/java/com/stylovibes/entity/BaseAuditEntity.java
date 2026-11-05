package com.stylovibes.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/** Superclase con auditoría de fechas. */
@Getter
@Setter
public abstract class BaseAuditEntity {

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;
}
