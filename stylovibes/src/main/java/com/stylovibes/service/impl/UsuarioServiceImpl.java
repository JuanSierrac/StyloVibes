package com.stylovibes.service.impl;

import com.stylovibes.dto.RegistroRequest;
import com.stylovibes.dto.UsuarioAdminForm;
import com.stylovibes.entity.PerfilUsuario;
import com.stylovibes.entity.Rol;
import com.stylovibes.entity.Usuario;
import com.stylovibes.exception.BusinessException;
import com.stylovibes.exception.DuplicateResourceException;
import com.stylovibes.exception.ResourceNotFoundException;
import com.stylovibes.repository.PasswordResetTokenRepository;
import com.stylovibes.repository.RolRepository;
import com.stylovibes.repository.UsuarioRepository;
import com.stylovibes.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Usuario registrar(RegistroRequest r) {
        if (!r.getPassword().equals(r.getConfirmarPassword())) {
            throw new BusinessException("Las contraseñas no coinciden");
        }
        validarEmailLibre(r.getEmail(), null);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol("USER"));
        return usuarioRepository.save(
                construir(r.getNombre(), r.getApellido(), r.getEmail(), r.getPassword(), true, roles));
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    @Override
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario obtener(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado (id " + id + ")"));
    }

    @Override
    public UsuarioAdminForm aFormulario(Long id) {
        Usuario u = obtener(id);
        UsuarioAdminForm f = new UsuarioAdminForm();
        f.setId(u.getId());
        f.setNombre(u.getNombre());
        f.setApellido(u.getApellido());
        f.setEmail(u.getEmail());
        f.setActivo(u.isActivo());
        u.getRoles().forEach(r -> f.getRoles().add(r.getNombre()));
        return f;
    }

    @Override
    public Usuario crearDesdeAdmin(UsuarioAdminForm f) {
        if (f.getPassword() == null || f.getPassword().length() < 8) {
            throw new BusinessException("La contraseña es obligatoria (mínimo 8 caracteres)");
        }
        validarEmailLibre(f.getEmail(), null);
        return usuarioRepository.save(construir(f.getNombre(), f.getApellido(), f.getEmail(),
                f.getPassword(), f.isActivo(), resolverRoles(f)));
    }

    @Override
    public Usuario actualizarDesdeAdmin(Long id, UsuarioAdminForm f) {
        Usuario u = obtener(id);
        validarEmailLibre(f.getEmail(), id);
        u.setNombre(f.getNombre());
        u.setApellido(f.getApellido());
        u.setEmail(f.getEmail().trim().toLowerCase());
        u.setActivo(f.isActivo());
        u.getRoles().clear();
        u.getRoles().addAll(resolverRoles(f));
        if (f.getPassword() != null && !f.getPassword().isBlank()) {
            if (f.getPassword().length() < 8) {
                throw new BusinessException("La contraseña debe tener mínimo 8 caracteres");
            }
            u.setPassword(passwordEncoder.encode(f.getPassword()));
        }
        return usuarioRepository.save(u);
    }

    @Override
    public void eliminar(Long id, String emailSolicitante) {
        Usuario u = obtener(id);
        if (u.getEmail().equalsIgnoreCase(emailSolicitante)) {
            throw new BusinessException("No puedes eliminar tu propia cuenta");
        }
        tokenRepository.deleteByUsuario(u);
        usuarioRepository.delete(u);
    }

    // ----- helpers -----

    private Usuario construir(String nombre, String apellido, String email, String password,
                              boolean activo, Set<Rol> roles) {
        Usuario u = new Usuario();
        u.setNombre(nombre.trim());
        u.setApellido(apellido.trim());
        u.setEmail(email.trim().toLowerCase());
        u.setPassword(passwordEncoder.encode(password));
        u.setActivo(activo);
        u.setRoles(roles);
        PerfilUsuario perfil = new PerfilUsuario();
        perfil.setUsuario(u);
        u.setPerfil(perfil);
        return u;
    }

    private Set<Rol> resolverRoles(UsuarioAdminForm f) {
        Set<Rol> roles = new HashSet<>();
        f.getRoles().forEach(n -> roles.add(rol(n)));
        return roles;
    }

    private Rol rol(String nombre) {
        return rolRepository.findByNombre(nombre)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + nombre));
    }

    private void validarEmailLibre(String email, Long idActual) {
        boolean existe = idActual == null
                ? usuarioRepository.existsByEmailIgnoreCase(email.trim())
                : usuarioRepository.existsByEmailIgnoreCaseAndIdNot(email.trim(), idActual);
        if (existe) {
            throw new DuplicateResourceException("El correo ya está registrado");
        }
    }
}
