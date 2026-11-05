package com.stylovibes;

import com.stylovibes.repository.TipoCorporalRepository;
import com.stylovibes.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ApplicationStartupTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TipoCorporalRepository tipoCorporalRepository;

    @Test
    void iniciaYCargaLosDatosEnMemoria() {
        assertTrue(usuarioRepository.count() > 0);
        assertTrue(tipoCorporalRepository.count() > 0);
    }
}