package com.enterprise.gestaoeventos.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final DataSource dataSource;

    @Bean
    public UserDetailsManager userDetailsManager() {
        var jdbcManager = new JdbcUserDetailsManager(dataSource);
        jdbcManager.setUsersByUsernameQuery("SELECT email AS username, senha AS password, ativo AS enabled FROM usuario WHERE email = ?");
        jdbcManager.setAuthoritiesByUsernameQuery("""
                SELECT usuario.email as username, permissao.role as authority 
                FROM usuario usuario 
                JOIN tb_permissao permissao ON permissao.usuario_id = usuario.id 
                WHERE usuario.email = ?
                """);
        return jdbcManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        List<String> adminOrOrganizador = List.of("ADMIN", "ORGANIZADOR");
        List<String> allRoles = List.of("ADMIN", "ORGANIZADOR", "PARTICIPANTE");

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/usuarios/**", "/usuarios").hasAnyRole(adminOrOrganizador.toArray(String[]::new))
                        .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/**").hasAnyRole(adminOrOrganizador.toArray(String[]::new))

                        .requestMatchers(HttpMethod.GET, "/eventos/**", "/eventos").hasAnyRole(allRoles.toArray(String[]::new))
                        .requestMatchers(HttpMethod.POST, "/eventos", "/pagamentos").hasAnyRole("ADMIN", "ORGANIZADOR")
                        .requestMatchers(HttpMethod.DELETE, "/eventos/**", "/pagamentos").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/inscricoes/**", "/inscricoes").hasAnyRole(allRoles.toArray(String[]::new))
                        .requestMatchers(HttpMethod.POST, "/inscricoes").hasAnyRole("ADMIN", "PARTICIPANTE")
                        .requestMatchers(HttpMethod.DELETE, "/inscricoes/**").hasAnyRole("ADMIN", "PARTICIPANTE")

                        .requestMatchers(HttpMethod.GET, "/pagamentos/**").hasAnyRole(allRoles.toArray(String[]::new))
                        .requestMatchers(HttpMethod.GET, "/pagamentos").hasAnyRole(allRoles.toArray(String[]::new))
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
