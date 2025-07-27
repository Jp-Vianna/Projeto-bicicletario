package com.es2.bicicletario.dto;
import java.time.YearMonth;

public class CartaoDeCreditoRequestDTO {

    private String nomeTitular;
    private String numero;
    private YearMonth validade;
    private String cvv;

    public CartaoDeCreditoRequestDTO() {
    }

    public CartaoDeCreditoRequestDTO(String nomeTitular, String numero, YearMonth validade, String cvv) {
        this.nomeTitular = nomeTitular;
        this.numero = numero;
        this.validade = validade;
        this.cvv = cvv;
    }

    public String getNomeTitular() {
        return nomeTitular;
    }

    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public YearMonth getValidade() {
        return validade;
    }

    public void setValidade(YearMonth validade) {
        this.validade = validade;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    @Override
    public String toString() {
        return "CartaoDeCreditoRequestDTO{" +
                "nomeTitular='" + nomeTitular + '\'' +
                ", numero='" + numero + '\'' +
                ", validade=" + validade +
                ", cvv='" + cvv + '\'' +
                '}';
    }
}
