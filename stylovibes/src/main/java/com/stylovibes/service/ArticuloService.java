package com.stylovibes.service;

import com.stylovibes.dto.ArticuloForm;
import com.stylovibes.entity.Articulo;

import java.util.List;

public interface ArticuloService {
    List<Articulo> listarPublicados(Long categoriaId);
    List<Articulo> ultimosPublicados();
    Articulo obtenerPublicadoPorSlug(String slug);
    List<Articulo> listarTodos();
    ArticuloForm aFormulario(Long id);
    Articulo crear(ArticuloForm form, String emailAutor);
    Articulo actualizar(Long id, ArticuloForm form);
    void eliminar(Long id);
}
