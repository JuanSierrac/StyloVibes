package com.stylovibes.seeder;

import com.stylovibes.entity.PerfilUsuario;
import com.stylovibes.entity.Rol;
import com.stylovibes.entity.Usuario;
import com.stylovibes.repository.RolRepository;
import com.stylovibes.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/** Crea roles USER/ADMIN y el usuario administrador inicial (idempotente). */
@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
@ConditionalOnProperty(name = "stylovibes.seed.enabled", havingValue = "true", matchIfMissing = true)
public class SeguridadSeeder implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${stylovibes.seed.admin.email}")
    private String adminEmail;

    @Value("${stylovibes.seed.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        Rol rolUser = rolRepository.findByNombre("USER").orElseGet(() -> rolRepository.save(new Rol("USER")));
        Rol rolAdmin = rolRepository.findByNombre("ADMIN").orElseGet(() -> rolRepository.save(new Rol("ADMIN")));

        if (usuarioRepository.existsByEmailIgnoreCase(adminEmail)) {
            return;
        }
        Usuario admin = new Usuario();
        admin.setNombre("Admin");
        admin.setApellido("StyloVibes");
        admin.setEmail(adminEmail.toLowerCase());
        admin.setPassword(passwordEncoder.encode(adminPassword));
        Set<Rol> roles = new HashSet<>();
        roles.add(rolAdmin);
        roles.add(rolUser);
        admin.setRoles(roles);
        PerfilUsuario perfil = new PerfilUsuario();
        perfil.setUsuario(admin);
        admin.setPerfil(perfil);
        usuarioRepository.save(admin);
        log.info("Usuario administrador creado: {} (cambia la contraseña en producción)", adminEmail);
    }
}
