package com.es2.bicicletario.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    private Date validade; 
    private String codigoSeguranca; 

}