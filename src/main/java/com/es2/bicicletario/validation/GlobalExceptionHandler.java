package com.es2.bicicletario.validation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura exceções de regras de negócio (ex: CPF inválido, e-mail já existe)
     * e retorna um status 400 Bad Request com a mensagem de erro.
     */
    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<String> handleRegraDeNegocioException(RegraDeNegocioException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    /**
     * Captura exceções de recursos não encontrados (ex: ciclista não existe)
     * e retorna um status 404 Not Found com a mensagem de erro.
     * Nota: Seria ainda melhor criar uma exceção customizada (ex: RecursoNaoEncontradoException)
     * para não capturar todas as RuntimeExceptions genéricas.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        if (ex.getMessage().toLowerCase().contains("não encontrado")) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
