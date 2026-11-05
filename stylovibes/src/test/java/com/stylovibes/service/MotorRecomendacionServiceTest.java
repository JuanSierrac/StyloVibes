package com.stylovibes.service;

import com.stylovibes.dto.PerfilCompletoResponse;
import com.stylovibes.entity.*;
import com.stylovibes.entity.enums.Genero;
import com.stylovibes.repository.RecomendacionRepository;
import com.stylovibes.service.impl.MotorRecomendacionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MotorRecomendacionServiceTest {

    @Mock
    private PerfilService perfilService;

    @Mock
    private RecomendacionRepository recomendacionRepository;

    @InjectMocks
    private MotorRecomendacionServiceImpl motorService;

    @Test
    void testGenerarPerfilCompleto() {
        Usuario u = new Usuario();
        u.setNombre("Ana");

        TipoCorporal tc = TipoCorporal.builder().id(1L).nombre("Reloj de arena").genero(Genero.FEMENINO)
                .prendasRecomendadas("Vestido cruzado;Blazer entallado")
                .recomendacionesVestuario("Marcar cintura").build();

        Colorimetria col = Colorimetria.builder().id(2L).nombre("Primavera")
                .coloresIdeales("Coral:#FF7F6B;Durazno:#FFCBA4").build();

        Estilo e = Estilo.builder().id(3L).nombre("Old Money")
                .prendasRepresentativas("Pantalón de vestir;Mocasines").build();

        PerfilUsuario perfil = new PerfilUsuario();
        perfil.setUsuario(u);
        perfil.setTipoCorporal(tc);
        perfil.setColorimetria(col);
        perfil.setEstiloPreferido(e);

        when(perfilService.obtenerPorEmail("ana@example.com")).thenReturn(perfil);
        when(recomendacionRepository.findByTipoCorporalIdAndActivoTrue(1L)).thenReturn(List.of());
        when(recomendacionRepository.findByColorimetriaIdAndActivoTrue(2L)).thenReturn(List.of());
        when(recomendacionRepository.findByEstiloIdAndActivoTrue(3L)).thenReturn(List.of());

        PerfilCompletoResponse res = motorService.generar("ana@example.com");

        assertNotNull(res);
        assertEquals("Ana", res.getNombreUsuario());
        assertEquals(75, res.getCompletitud());
        assertFalse(res.getCombinaciones().isEmpty());
        assertFalse(res.getConsejos().isEmpty());
    }
}

