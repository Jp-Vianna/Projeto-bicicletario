package com.es2.bicicletario.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

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

    public static String verificaCartao() { // Não integrado
        return "Cartão validado com sucesso.";
    }
}