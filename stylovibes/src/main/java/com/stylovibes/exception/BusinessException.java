package com.stylovibes.exception;

/** Violación de una regla de negocio (mensaje apto para el usuario). */
public class BusinessException extends RuntimeException {
    public BusinessException(String mensaje) { super(mensaje); }
}
