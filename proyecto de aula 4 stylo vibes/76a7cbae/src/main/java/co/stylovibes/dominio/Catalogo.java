package co.stylovibes.dominio;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "catalogo")
public class Catalogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catalogo")
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime fechaGeneracion = LocalDateTime.now();

    @OneToMany(mappedBy = "catalogo", cascade = CascadeType.ALL, orphanRemoval = true,
            fetch = FetchType.EAGER)
    @OrderBy("seccion ASC, orden ASC")
    private List<CatalogoItem> items = new ArrayList<>();

    public void agregarItem(CatalogoItem item) {
        item.setCatalogo(this);
        items.add(item);
    }

    public List<CatalogoItem> itemsDe(Seccion seccion) {
        List<CatalogoItem> resultado = new ArrayList<>();
        for (CatalogoItem item : items) {
            if (item.getSeccion() == seccion) {
                resultado.add(item);
            }
        }
        return resultado;
    }

    public boolean usaImagenesDetalladas() {
        if (items.isEmpty()) {
            return false;
        }
        for (CatalogoItem item : items) {
            if (item.getImagen() == null || !item.getImagen().startsWith("/img/cat/")
                    || !item.getImagen().endsWith(".jpg")) {
                return false;
            }
        }
        return true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(LocalDateTime fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }
    public List<CatalogoItem> getItems() { return items; }
    public void setItems(List<CatalogoItem> items) { this.items = items; }
}
