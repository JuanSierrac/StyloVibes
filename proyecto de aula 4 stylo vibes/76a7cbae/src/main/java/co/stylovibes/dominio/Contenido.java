package co.stylovibes.dominio;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "contenidos")
public class Contenido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contenido")
    private Long id;

    @NotBlank(message = "La clave es obligatoria")
    @Size(max = 80, message = "Máximo 80 caracteres")
    @Column(nullable = false, unique = true, length = 80)
    private String clave;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "Máximo 150 caracteres")
    @Column(nullable = false, length = 150)
    private String titulo;

    @NotBlank(message = "El texto es obligatorio")
    @Lob
    @Column(nullable = false)
    private String texto;

    @Size(max = 300, message = "Máximo 300 caracteres")
    @Column(length = 300)
    private String imagen;

    @NotNull(message = "Selecciona una sección")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SeccionContenido seccion = SeccionContenido.INICIO;

    @Column(nullable = false)
    private Integer orden = 0;

    @Column(nullable = false)
    private boolean publicado = true;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public SeccionContenido getSeccion() { return seccion; }
    public void setSeccion(SeccionContenido seccion) { this.seccion = seccion; }
    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }
    public boolean isPublicado() { return publicado; }
    public void setPublicado(boolean publicado) { this.publicado = publicado; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public String getFechaActualizacionTexto() {
        if (fechaActualizacion == null) {
            return "";
        }
        return fechaActualizacion.format(java.time.format.DateTimeFormatter
                .ofPattern("dd/MM/yyyy HH:mm", new java.util.Locale("es", "CO")));
    }
}
