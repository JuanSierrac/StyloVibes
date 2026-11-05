package com.stylovibes.controller.admin;

import com.stylovibes.entity.*;
import com.stylovibes.exception.ResourceNotFoundException;
import com.stylovibes.service.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.stylovibes.controller.admin.FieldSpec.*;

/** Registro de los catálogos que comparten el CRUD genérico (/admin/catalogo/{slug}). */
@Component
public class AdminResourceRegistry {

    private static final String LISTA = "Separa los ítems con ; (punto y coma)";
    private static final String PALETA = "Formato Nombre:#HEX separados por ; — ej. Coral:#FF7F6B;Durazno:#FFCBA4";

    private final Map<String, AdminResource> recursos = new LinkedHashMap<>();

    public AdminResourceRegistry(TipoCorporalService tipoCorporalService, TipoRostroService tipoRostroService,
                                 ColorimetriaService colorimetriaService, EstiloService estiloService,
                                 CategoriaArticuloService categoriaService) {

        registrar(new AdminResource("tipos-corporales", "Tipos corporales", "tipo corporal", TipoCorporal.class,
                cast(tipoCorporalService),
                List.of(text("nombre", "Nombre", true),
                        select("genero", "Género", List.of("FEMENINO", "MASCULINO")),
                        area("descripcion", "Descripción", true, null),
                        area("fortalezas", "Fortalezas", false, LISTA),
                        area("recomendacionesVestuario", "Recomendaciones de vestuario", false, LISTA),
                        area("prendasRecomendadas", "Prendas recomendadas", false, LISTA),
                        area("prendasEvitar", "Prendas a evitar", false, LISTA),
                        text("imagenUrl", "URL de imagen", false),
                        check("activo", "Activo")),
                List.of(text("id", "ID", false), text("nombre", "Nombre", false),
                        text("genero", "Género", false), check("activo", "Activo"))));

        registrar(new AdminResource("tipos-rostro", "Tipos de rostro", "tipo de rostro", TipoRostro.class,
                cast(tipoRostroService),
                List.of(text("nombre", "Nombre", true),
                        area("caracteristicas", "Características", true, null),
                        area("peinadosRecomendados", "Peinados recomendados", false, LISTA),
                        area("accesoriosRecomendados", "Accesorios recomendados", false, LISTA),
                        area("gafasRecomendadas", "Gafas recomendadas", false, LISTA),
                        area("recomendacionesGenerales", "Recomendaciones estéticas", false, LISTA),
                        text("imagenUrl", "URL de imagen", false),
                        check("activo", "Activo")),
                List.of(text("id", "ID", false), text("nombre", "Nombre", false), check("activo", "Activo"))));

        registrar(new AdminResource("colorimetrias", "Estaciones de color", "estación de color", Colorimetria.class,
                cast(colorimetriaService),
                List.of(text("nombre", "Nombre", true),
                        area("descripcion", "Descripción", true, null),
                        area("paletaColores", "Paleta de colores", false, PALETA),
                        area("coloresIdeales", "Colores ideales", false, PALETA),
                        area("coloresEvitar", "Colores a evitar", false, PALETA),
                        area("ejemplos", "Ejemplos de looks", false, LISTA),
                        check("activo", "Activo")),
                List.of(text("id", "ID", false), text("nombre", "Nombre", false), check("activo", "Activo"))));

        registrar(new AdminResource("estilos", "Estilos", "estilo", Estilo.class,
                cast(estiloService),
                List.of(text("nombre", "Nombre", true),
                        area("descripcion", "Descripción", true, null),
                        area("caracteristicas", "Características", false, LISTA),
                        area("ejemplos", "Ejemplos", false, LISTA),
                        area("prendasRepresentativas", "Prendas representativas", false, LISTA),
                        area("inspiracionVisual", "Inspiración visual", false, null),
                        color("colorPrincipal", "Color principal"),
                        text("imagenUrl", "URL de imagen", false),
                        check("activo", "Activo")),
                List.of(text("id", "ID", false), text("nombre", "Nombre", false),
                        text("colorPrincipal", "Color", false), check("activo", "Activo"))));

        registrar(new AdminResource("categorias", "Categorías del blog", "categoría", CategoriaArticulo.class,
                cast(categoriaService),
                List.of(text("nombre", "Nombre", true),
                        text("descripcion", "Descripción", false),
                        check("activo", "Activa")),
                List.of(text("id", "ID", false), text("nombre", "Nombre", false), check("activo", "Activa"))));
    }

    public AdminResource obtener(String slug) {
        AdminResource r = recursos.get(slug);
        if (r == null) throw new ResourceNotFoundException("Recurso administrable no encontrado: " + slug);
        return r;
    }

    public List<AdminResource> todos() {
        return new ArrayList<>(recursos.values());
    }

    private void registrar(AdminResource r) {
        recursos.put(r.slug(), r);
    }

    @SuppressWarnings("unchecked")
    private <T> CatalogoService<Object> cast(CatalogoService<T> s) {
        return (CatalogoService<Object>) (CatalogoService<?>) s;
    }
}
