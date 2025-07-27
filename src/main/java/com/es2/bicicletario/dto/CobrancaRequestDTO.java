package com.es2.bicicletario.dto;

import java.math.BigDecimal;

public class CobrancaRequestDTO {

    private BigDecimal valor;
    private Integer ciclista;

    public CobrancaRequestDTO() {
    }

    public CobrancaRequestDTO(BigDecimal valor, Integer ciclista) {
        this.valor = valor;
        this.ciclista = ciclista;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getCiclista() {
        return ciclista;
    }

    public void setCiclista(Integer ciclista) {
        this.ciclista = ciclista;
    }

    @Override
    public String toString() {
        return "AluguelRequestDTO{" +
                "valor=" + valor +
                ", ciclista=" + ciclista +
                '}';
    }
}
