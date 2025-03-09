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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;

import javax.sql.DataSource;

@Configuration @RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final DataSource dataSource;

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        var jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT email AS username, senha AS password, ativo AS enabled FROM usuario WHERE email = ?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT usuario.email as username, permissao.role as authority " +
                        "FROM usuario usuario " +
                        "JOIN tb_permissao permissao ON permissao.usuario_id = usuario.id " +
                        "WHERE usuario.email = ?");
        return jdbcUserDetailsManager;
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
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrfConfig -> csrfConfig.disable())
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.GET, "/usuarios/**").hasAnyRole("ADMIN, ORGANIZADOR")
                        .requestMatchers(HttpMethod.GET, "/usuarios").hasAnyRole("ADMIN, ORGANIZADOR")
                        .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/**").hasAnyRole("ADMIN, ORGANIZADOR")
                        .requestMatchers(HttpMethod.GET,"/eventos/**").hasAnyRole("ADMIN", "ORGANIZADOR", "PARTICIPANTE")
                        .requestMatchers(HttpMethod.GET,"/eventos").hasAnyRole("ADMIN", "ORGANIZADOR", "PARTICIPANTE")
                        .requestMatchers(HttpMethod.POST,"/eventos").hasAnyRole("ADMIN", "ORGANIZADOR")
                        .requestMatchers(HttpMethod.DELETE,"/eventos/**").hasAnyRole("ADMIN", "ORGANIZADOR")
                        .requestMatchers(HttpMethod.GET,"/inscricoes/**").hasAnyRole("ADMIN", "ORGANIZADOR", "PARTICIPANTE")
                        .requestMatchers(HttpMethod.GET,"/inscricoes/").hasAnyRole("ADMIN", "ORGANIZADOR", "PARTICIPANTE")
                        .requestMatchers(HttpMethod.POST,"/inscricoes").hasAnyRole("ADMIN", "PARTICIPANTE")
                        .requestMatchers(HttpMethod.DELETE,"/inscricoes/**").hasAnyRole("ADMIN", "PARTICIPANTE")
                        .requestMatchers(HttpMethod.GET, "/pagamentos/**").hasAnyRole("ADMIN", "ORGANIZADOR", "PARTICIPANTE")
                        .requestMatchers(HttpMethod.GET, "/pagamentos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/pagamentos").hasAnyRole("ADMIN", "ORGANIZADOR")
                        .requestMatchers(HttpMethod.DELETE, "/pagamentos").hasRole("ADMIN")
                        .anyRequest().authenticated()
                );

        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }
}