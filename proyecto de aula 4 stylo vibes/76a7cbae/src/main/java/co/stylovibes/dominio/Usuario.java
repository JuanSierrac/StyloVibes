package co.stylovibes.dominio;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @NotBlank(message = "Escribe tu nombre")
    @Column(nullable = false, length = 80)
    private String nombre;

    @NotBlank(message = "Escribe tu apellido")
    @Column(nullable = false, length = 80)
    private String apellido;

    @NotNull(message = "Indica tu edad")
    @Min(value = 13, message = "Debes tener al menos 13 años")
    @Max(value = 99, message = "Revisa la edad ingresada")
    @Column(nullable = false)
    private Integer edad;

    @NotBlank(message = "Selecciona tu género")
    @Column(nullable = false, length = 30)
    private String genero;

    @NotBlank(message = "Escribe tu correo")
    @Email(message = "Escribe un correo válido")
    @Column(nullable = false, unique = true, length = 160)
    private String email;

    @Column(nullable = false, length = 100)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol = Rol.USUARIO;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Caracteristica caracteristica;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Catalogo catalogo;

    public boolean esAdministrador() {
        return rol == Rol.ADMINISTRADOR;
    }

    public String getNombreCompleto() {
        return (nombre + " " + apellido).trim();
    }

    public String getNombreCorto() {
        return nombre == null ? "Hola" : nombre;
    }

    public String getFechaRegistroTexto() {
        if (fechaRegistro == null) {
            return "";
        }
        return fechaRegistro.format(java.time.format.DateTimeFormatter
                .ofPattern("d 'de' MMMM 'de' yyyy", new java.util.Locale("es", "CO")));
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public Caracteristica getCaracteristica() { return caracteristica; }
    public void setCaracteristica(Caracteristica caracteristica) { this.caracteristica = caracteristica; }
    public Catalogo getCatalogo() { return catalogo; }
    public void setCatalogo(Catalogo catalogo) { this.catalogo = catalogo; }
}
