package co.stylovibes.servicio;

import co.stylovibes.dominio.Rol;
import co.stylovibes.dominio.Usuario;
import co.stylovibes.dominio.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService implements UserDetailsService {

    public static class CorreoDuplicadoException extends RuntimeException {
        public CorreoDuplicadoException(String mensaje) {
            super(mensaje);
        }
    }

    public static class AdministradorExistenteException extends RuntimeException {
        public AdministradorExistenteException(String mensaje) {
            super(mensaje);
        }
    }

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email.trim())
                .orElseThrow(() -> new UsernameNotFoundException("No existe una cuenta con ese correo"));
        return User.withUsername(usuario.getEmail())
                .password(usuario.getPasswordHash())
                .roles(usuario.getRol().name())
                .build();
    }

    @Transactional
    public Usuario registrar(String nombre, String apellido, Integer edad, String genero, String email,
                             String password) {
        if (usuarioRepository.existsByEmailIgnoreCase(email.trim())) {
            throw new CorreoDuplicadoException("Este correo ya tiene una cuenta.");
        }
        Usuario u = new Usuario();
        u.setNombre(nombre.trim());
        u.setApellido(apellido.trim());
        u.setEdad(edad);
        u.setGenero(genero);
        u.setEmail(email.trim().toLowerCase());
        u.setPasswordHash(passwordEncoder.encode(password));
        u.setRol(Rol.USUARIO);
        return usuarioRepository.save(u);
    }

    public boolean existeAdministrador() {
        return usuarioRepository.countByRol(Rol.ADMINISTRADOR) > 0;
    }

    @Transactional
    public Usuario registrarAdministrador(String nombre, String apellido, String email, String password) {
        if (existeAdministrador()) {
            throw new AdministradorExistenteException("Ya existe una cuenta administradora. Inicia sesión.");
        }
        if (usuarioRepository.existsByEmailIgnoreCase(email.trim())) {
            throw new CorreoDuplicadoException("Este correo ya tiene una cuenta.");
        }
        Usuario u = new Usuario();
        u.setNombre(nombre.trim());
        u.setApellido(apellido.trim());
        u.setEdad(30);
        u.setGenero("Prefiero no decirlo");
        u.setEmail(email.trim().toLowerCase());
        u.setPasswordHash(passwordEncoder.encode(password));
        u.setRol(Rol.ADMINISTRADOR);
        return usuarioRepository.save(u);
    }

    public Usuario porEmail(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email).orElse(null);
    }

    public Usuario porId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }
}
