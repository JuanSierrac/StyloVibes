package com.stylovibes.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Manejo centralizado de excepciones de dominio → vista "error". */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String noEncontrado(ResourceNotFoundException ex, Model model) {
        model.addAttribute("status", 404);
        model.addAttribute("mensaje", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String negocio(BusinessException ex, Model model) {
        log.warn("Regla de negocio: {}", ex.getMessage());
        model.addAttribute("status", 400);
        model.addAttribute("mensaje", ex.getMessage());
        return "error";
    }
}
