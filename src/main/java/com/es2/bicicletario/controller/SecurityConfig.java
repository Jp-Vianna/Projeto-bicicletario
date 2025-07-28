package com.es2.bicicletario.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain; // A importação que faltava

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desabilita a proteção CSRF, essencial para APIs REST
            .csrf(csrf -> csrf.disable())
            
            // Define as regras de autorização
            .authorizeHttpRequests(auth -> auth
                // Permite TODAS as requisições para QUALQUER endpoint sem autenticação
                .anyRequest().permitAll() 
            );

        return http.build();
    }
}
