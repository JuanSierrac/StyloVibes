package com.stylovibes.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TextoUtilTest {

    @Test
    void testPartir() {
        assertEquals(List.of("a", "b", "c"), TextoUtil.partir("a; b ; c "));
        assertTrue(TextoUtil.partir(null).isEmpty());
        assertTrue(TextoUtil.partir("   ").isEmpty());
    }

    @Test
    void testNombresDeColor() {
        assertEquals(List.of("Coral", "Durazno"), TextoUtil.nombresDeColor("Coral:#FF7F6B;Durazno:#FFCBA4"));
        assertTrue(TextoUtil.nombresDeColor(null).isEmpty());
    }

    @Test
    void testElemento() {
        List<String> items = List.of("x", "y");
        assertEquals("x", TextoUtil.elemento(items, 0));
        assertEquals("y", TextoUtil.elemento(items, 1));
        assertEquals("x", TextoUtil.elemento(items, 2));
        assertNull(TextoUtil.elemento(List.of(), 0));
        assertNull(TextoUtil.elemento(null, 0));
    }

    @Test
    void testSlugify() {
        assertEquals("hola-mundo", TextoUtil.slugify("¡Hola, Mundo!"));
        assertEquals("", TextoUtil.slugify(null));
        assertEquals("categoria-1", TextoUtil.slugify("Categoría 1"));
    }
}

