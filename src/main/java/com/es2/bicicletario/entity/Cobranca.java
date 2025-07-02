package com.es2.bicicletario.entity;

import java.math.BigDecimal;

public class Cobranca {
    private static final BigDecimal valorPadrao =  new BigDecimal("10.00");
    private Status status = Status.DISPONIVEL;

    public static void realizarCobrancaPadrao() {
        System.out.println("Valor inicial, R$" + valorPadrao + "cobrado com sucesso!");
    }

    public static String realizarCobrancaExtra() {
        System.out.println("Valor extra a pagar de R$20.54 cobrado com sucesso!");
        
        return "30.58";
    }

    public Status getStatus() {
        return status;
    }
}
