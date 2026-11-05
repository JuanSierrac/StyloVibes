package com.stylovibes.util;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Utilidades de texto: listas separadas por ';' y slugs. */
public final class TextoUtil {

    private TextoUtil() {}

    /** Divide "a;b;c" en lista limpia. Null o vacío → lista vacía. */
    public static List<String> partir(String texto) {
        List<String> resultado = new ArrayList<>();
        if (texto == null || texto.isBlank()) return resultado;
        for (String parte : texto.split(";")) {
            if (!parte.isBlank()) resultado.add(parte.trim());
        }
        return resultado;
    }

    /** "Coral:#FF7F6B;Durazno:#FFCBA4" → ["Coral","Durazno"]. */
    public static List<String> nombresDeColor(String paleta) {
        List<String> nombres = new ArrayList<>();
        if (paleta == null || paleta.isBlank()) return nombres;
        for (String item : partir(paleta)) {
            nombres.add(item.contains(":") ? item.substring(0, item.indexOf(':')).trim() : item);
        }
        return nombres;
    }

    /** Elemento circular de la lista; null si la lista está vacía o nula. */
    public static String elemento(List<String> lista, int indice) {
        if (lista == null || lista.isEmpty()) return null;
        int pos = Math.floorMod(indice, lista.size());
        return lista.get(pos);
    }

    public static String slugify(String texto) {
        if (texto == null || texto.isBlank()) return "";
        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return normalizado.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }
}
