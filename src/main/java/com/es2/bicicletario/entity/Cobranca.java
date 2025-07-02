package com.es2.bicicletario.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cobranca {
    private Status status = Status.DISPONIVEL;
    private static final Logger logger = LoggerFactory.getLogger(Cobranca.class);
    

    public static void realizarCobrancaPadrao() {
        logger.warn("AVISO: Integração com API externa ainda não implementada. Usando comportamento FALSO de SUCESSO");
    }

    public static String realizarCobrancaExtra() {
        logger.warn("AVISO: Integração com API externa ainda não implementada. Usando comportamento FALSO de SUCESSO");
        
        return "28.90";
    }

    public Status getStatus() {
        return status;
    }
}
