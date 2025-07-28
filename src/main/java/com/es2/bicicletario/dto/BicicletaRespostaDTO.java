package com.es2.bicicletario.dto;

import com.es2.bicicletario.entity.StatusBicicleta;

public class BicicletaRespostaDTO {

    private Integer id;
    private String marca;
    private String modelo;
    private String ano;
    private Integer numero;
    private StatusBicicleta status;

    public BicicletaRespostaDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public StatusBicicleta getStatus() {
        return status;
    }

    public void setStatus(StatusBicicleta status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BicicletaRespostaDTO{" +
                "id=" + id +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", ano='" + ano + '\'' +
                ", numero=" + numero +
                ", status='" + status + '\'' +
                '}';
    }
}
