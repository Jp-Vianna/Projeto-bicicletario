package com.es2.bicicletario.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private EmailService() {
        throw new IllegalStateException("Classe de utilidade não deve ser instanciada");
    }

    public static boolean enviarEmail() {
        logger.warn("AVISO: Integração com API externa ainda não implementada. Usando comportamento FALSO de SUCESSO");
        
        return true;
    }
}
