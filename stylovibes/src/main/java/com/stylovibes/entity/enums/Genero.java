package com.stylovibes.entity.enums;

public enum Genero {
    MASCULINO("Masculino"),
    FEMENINO("Femenino");

    private final String etiqueta;

    Genero(String etiqueta) { this.etiqueta = etiqueta; }

    public String getEtiqueta() { return etiqueta; }
}
