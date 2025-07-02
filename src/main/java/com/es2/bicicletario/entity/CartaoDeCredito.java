package com.es2.bicicletario.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.Embeddable;

/**
 * Value Object para representar um Cartão de Crédito.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CartaoDeCredito {

    private String numeroCartao;
    private String nomeNoCartao; 
    private YearMonth validade; 
    private String codigoSeguranca; 
    private static final Logger logger = LoggerFactory.getLogger(CartaoDeCredito.class);

    public static boolean verificaCartao() { // Não integrado
        
        logger.warn("AVISO: Integração com API externa ainda não implementada. Usando comportamento FALSO de SUCESSO");
        
        return true;
    }
}