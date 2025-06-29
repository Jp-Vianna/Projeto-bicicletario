package com.es2.bicicletario.entity;

import java.math.BigDecimal;

public class Cobranca {
    private final BigDecimal valor =  new BigDecimal("22.15");

    public BigDecimal getValor() {
        return valor;
    }
}
