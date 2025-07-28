package com.es2.bicicletario.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.es2.bicicletario.dto.EmailRequestDTO;

import reactor.core.publisher.Mono;

@Service
public class EmailService {

    private final WebClient apiExterno;

    public EmailService(@Qualifier("apiExterno") WebClient apiExterno) {
        this.apiExterno = apiExterno;
    }

    public void enviaEmail(String email, String assunto, String mensagem) {

        EmailRequestDTO emailRequest = new EmailRequestDTO(email, assunto, mensagem);

        try {
            apiExterno
                .post()
                .uri("/email/enviarEmail")
                .bodyValue(emailRequest)
                .retrieve()
                .onStatus(
                    status -> status.isError(),
                    response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new RuntimeException("Falha na API: " + errorBody)))
                )
                .bodyToMono(Void.class)
                .block();

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível enviar o e-mail para " + email, e);
        }
    }
}