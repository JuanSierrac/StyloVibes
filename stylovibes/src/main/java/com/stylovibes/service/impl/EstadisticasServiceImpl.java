package com.stylovibes.service.impl;

import com.stylovibes.repository.*;
import com.stylovibes.service.EstadisticasService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EstadisticasServiceImpl implements EstadisticasService {

    private final UsuarioRepository usuarioRepository;
    private final TipoCorporalRepository tipoCorporalRepository;
    private final TipoRostroRepository tipoRostroRepository;
    private final ColorimetriaRepository colorimetriaRepository;
    private final EstiloRepository estiloRepository;
    private final ArticuloRepository articuloRepository;
    private final RecomendacionRepository recomendacionRepository;

    @Override
    public Map<String, Long> resumen() {
        Map<String, Long> m = new LinkedHashMap<>();
        m.put("Usuarios", usuarioRepository.count());
        m.put("Tipos corporales", tipoCorporalRepository.count());
        m.put("Tipos de rostro", tipoRostroRepository.count());
        m.put("Estaciones de color", colorimetriaRepository.count());
        m.put("Estilos", estiloRepository.count());
        m.put("Artículos", articuloRepository.count());
        m.put("Recomendaciones", recomendacionRepository.count());
        return m;
    }
}
