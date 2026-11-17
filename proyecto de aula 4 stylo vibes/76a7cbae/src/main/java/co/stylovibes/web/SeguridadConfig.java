package co.stylovibes.web;

import co.stylovibes.servicio.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SeguridadConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain adminChain(HttpSecurity http, UsuarioService usuarioService) throws Exception {
        http.antMatcher("/admin/**")
                .authorizeRequests()
                .antMatchers("/admin/login", "/admin/configuracion-inicial").permitAll()
                .anyRequest().hasRole("ADMINISTRADOR")
                .and()
                .formLogin()
                .loginPage("/admin/login")
                .loginProcessingUrl("/admin/login")
                .defaultSuccessUrl("/admin", true)
                .failureUrl("/admin/login?error")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/admin/salir")
                .logoutSuccessUrl("/admin/login?salido")
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/admin/login?denegado")
                .and()
                .userDetailsService(usuarioService);
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain appChain(HttpSecurity http, UsuarioService usuarioService) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/registro", "/login", "/css/**", "/img/**", "/error", "/favicon.ico").permitAll()
                .antMatchers("/app/**").hasRole("USUARIO")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler((request, response, authentication) -> {
                    boolean admin = authentication.getAuthorities().stream()
                            .anyMatch(a -> "ROLE_ADMINISTRADOR".equals(a.getAuthority()));
                    response.sendRedirect(request.getContextPath() + (admin ? "/admin" : "/app"));
                })
                .failureUrl("/login?error")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/salir")
                .logoutSuccessUrl("/?salido")
                .permitAll()
                .and()
                .userDetailsService(usuarioService);
        return http.build();
    }
}
