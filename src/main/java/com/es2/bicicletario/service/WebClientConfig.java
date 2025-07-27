package com.es2.bicicletario.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClientParaServicoExterno(@Value("${microservico.externo.url}") String urlBase) {
        
        return WebClient.builder()
            .baseUrl(urlBase) // URL base para todas as chamadas
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) // Header padrão
            // .defaultHeader("X-API-KEY", "sua-chave-de-api-secreta") // Exemplo de header de autenticação
            .build();
    }
}
