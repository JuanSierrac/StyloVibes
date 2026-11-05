package com.stylovibes.controller.admin;

import java.util.List;

/** Describe un campo de formulario/columna del CRUD genérico de catálogos. */
public record FieldSpec(String name, String label, String type, boolean required, List<String> options, String help) {

    public static FieldSpec text(String name, String label, boolean required) {
        return new FieldSpec(name, label, "text", required, List.of(), null);
    }

    public static FieldSpec area(String name, String label, boolean required, String help) {
        return new FieldSpec(name, label, "textarea", required, List.of(), help);
    }

    public static FieldSpec select(String name, String label, List<String> options) {
        return new FieldSpec(name, label, "select", true, options, null);
    }

    public static FieldSpec check(String name, String label) {
        return new FieldSpec(name, label, "checkbox", false, List.of(), null);
    }

    public static FieldSpec color(String name, String label) {
        return new FieldSpec(name, label, "text", false, List.of(), "Color HEX, ej. #C9B79C");
    }

    public boolean esCheckbox() {
        return "checkbox".equals(type);
    }
}
