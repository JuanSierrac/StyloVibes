package com.stylovibes.service;

import com.stylovibes.dto.PerfilCompletoResponse;

/** Sistema de recomendaciones: cruza cuerpo, rostro, color y estilo. */
public interface MotorRecomendacionService {
    PerfilCompletoResponse generar(String emailUsuario);
}
