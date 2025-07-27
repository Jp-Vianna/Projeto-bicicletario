package com.es2.bicicletario.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.es2.bicicletario.dto.EmailRequestDTO;
import com.es2.bicicletario.dto.EmailResponseDTO;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class EmailService {

    private final WebClient webClient;

    public boolean enviaEmail(String email, String assunto, String mensagem){

        EmailRequestDTO emailRequest = new EmailRequestDTO(email, assunto, mensagem);

        try {
            webClient
                .post()
                .uri("/email/enviarEmail")
                .bodyValue(emailRequest)
                .retrieve()
                .onStatus(
                    status -> status.isError(),
                    response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new RuntimeException("Falha na API. Status: " + response.statusCode() + ". Resposta: " + errorBody)))
                )
                .bodyToMono(EmailResponseDTO.class)
                .block();
            
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
