package com.es2.bicicletario.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Simulando integração
public class Tranca {
    private Integer idTranca; 
    private Integer idBicicleta;
    private static final Logger logger = LoggerFactory.getLogger(Tranca.class);

    public Tranca(Integer id){
        this.idTranca = id;
        this.idBicicleta = 1;
    }

    public static void destravaTranca() {
        logger.warn("AVISO: Integração com API externa ainda não implementada. Usando comportamento FALSO de SUCESSO");
    }

    public static void travarTranca() {
        logger.warn("AVISO: Integração com API externa ainda não implementada. Usando comportamento FALSO de SUCESSO");
    }

    public Integer getIdTranca() {
        return idTranca;
    }

    public Integer getIdBicicleta() {
        return idBicicleta;
    }
}
