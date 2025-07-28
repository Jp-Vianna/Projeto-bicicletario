package com.es2.bicicletario.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @Qualifier("apiExterno")
    public WebClient webClientParaServicoExterno(@Value("${microservico.externo.url}") String urlBase) {
        
        return WebClient.builder()
            .baseUrl(urlBase) 
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) 
            .build();
    }

    @Bean
    @Qualifier("apiEquipamentos") 
    public WebClient webClientParaServicoEquipamentos(@Value("${microservico.equipamento.url}") String urlBase) { 
        
        return WebClient.builder()
            .baseUrl(urlBase) 
            .build();
    }
}
