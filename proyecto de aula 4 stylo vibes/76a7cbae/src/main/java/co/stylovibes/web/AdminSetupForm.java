package co.stylovibes.web;

import javax.validation.constraints.*;

public class AdminSetupForm {

    @NotBlank(message = "Escribe tu nombre")
    @Size(max = 80, message = "Máximo 80 caracteres")
    private String nombre;

    @NotBlank(message = "Escribe tu apellido")
    @Size(max = 80, message = "Máximo 80 caracteres")
    private String apellido;

    @NotBlank(message = "Escribe tu correo")
    @Email(message = "Escribe un correo válido")
    @Size(max = 160, message = "Máximo 160 caracteres")
    private String email;

    @NotBlank(message = "Crea una contraseña")
    @Size(min = 10, max = 72, message = "La contraseña debe tener entre 10 y 72 caracteres")
    private String password;

    @NotBlank(message = "Repite la contraseña")
    private String confirmarPassword;

    @AssertTrue(message = "Debes autorizar el tratamiento de tus datos para crear la cuenta administrativa")
    private boolean autorizacion;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getConfirmarPassword() { return confirmarPassword; }
    public void setConfirmarPassword(String confirmarPassword) { this.confirmarPassword = confirmarPassword; }
    public boolean isAutorizacion() { return autorizacion; }
    public void setAutorizacion(boolean autorizacion) { this.autorizacion = autorizacion; }
}