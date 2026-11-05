package com.stylovibes.service.impl;

import com.stylovibes.dto.ArticuloForm;
import com.stylovibes.entity.Articulo;
import com.stylovibes.entity.CategoriaArticulo;
import com.stylovibes.exception.ResourceNotFoundException;
import com.stylovibes.repository.ArticuloRepository;
import com.stylovibes.repository.CategoriaArticuloRepository;
import com.stylovibes.repository.UsuarioRepository;
import com.stylovibes.service.ArticuloService;
import com.stylovibes.util.TextoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticuloServiceImpl implements ArticuloService {

    private final ArticuloRepository articuloRepository;
    private final CategoriaArticuloRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<Articulo> listarPublicados(Long categoriaId) {
        return categoriaId == null
                ? articuloRepository.findByPublicadoTrueOrderByFechaCreacionDesc()
                : articuloRepository.findByPublicadoTrueAndCategoriaIdOrderByFechaCreacionDesc(categoriaId);
    }

    @Override
    public List<Articulo> ultimosPublicados() {
        return articuloRepository.findTop3ByPublicadoTrueOrderByFechaCreacionDesc();
    }

    @Override
    public Articulo obtenerPublicadoPorSlug(String slug) {
        return articuloRepository.findBySlugAndPublicadoTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Artículo no encontrado"));
    }

    @Override
    public List<Articulo> listarTodos() {
        return articuloRepository.findAllByOrderByFechaCreacionDesc();
    }

    @Override
    public ArticuloForm aFormulario(Long id) {
        Articulo a = obtener(id);
        ArticuloForm f = new ArticuloForm();
        f.setId(a.getId());
        f.setTitulo(a.getTitulo());
        f.setResumen(a.getResumen());
        f.setContenido(a.getContenido());
        f.setImagenUrl(a.getImagenUrl());
        f.setCategoriaId(a.getCategoria().getId());
        f.setPublicado(a.isPublicado());
        return f;
    }

    @Override
    public Articulo crear(ArticuloForm f, String emailAutor) {
        Articulo a = Articulo.builder()
                .titulo(f.getTitulo().trim())
                .slug(generarSlug(f.getTitulo()))
                .resumen(f.getResumen().trim())
                .contenido(f.getContenido())
                .imagenUrl(vacioANull(f.getImagenUrl()))
                .publicado(f.isPublicado())
                .categoria(categoria(f.getCategoriaId()))
                .autor(usuarioRepository.findByEmailIgnoreCase(emailAutor).orElse(null))
                .build();
        return articuloRepository.save(a);
    }

    @Override
    public Articulo actualizar(Long id, ArticuloForm f) {
        Articulo a = obtener(id);
        a.setTitulo(f.getTitulo().trim());
        a.setResumen(f.getResumen().trim());
        a.setContenido(f.getContenido());
        a.setImagenUrl(vacioANull(f.getImagenUrl()));
        a.setPublicado(f.isPublicado());
        a.setCategoria(categoria(f.getCategoriaId()));
        return articuloRepository.save(a); // el slug se conserva para no romper enlaces
    }

    @Override
    public void eliminar(Long id) {
        articuloRepository.delete(obtener(id));
    }

    // ----- helpers -----

    private Articulo obtener(Long id) {
        return articuloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artículo no encontrado (id " + id + ")"));
    }

    private CategoriaArticulo categoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
    }

    private String generarSlug(String titulo) {
        String base = TextoUtil.slugify(titulo);
        if (base.isBlank()) base = "articulo";
        String candidato = base;
        int n = 2;
        while (articuloRepository.existsBySlug(candidato)) {
            candidato = base + "-" + n++;
        }
        return candidato;
    }

    private String vacioANull(String s) {
        return s == null || s.isBlank() ? null : s.trim();
    }
}
