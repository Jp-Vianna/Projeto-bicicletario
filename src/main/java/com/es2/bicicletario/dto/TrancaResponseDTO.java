package com.es2.bicicletario.dto;


public class TrancaResponseDTO {

    private Integer id;
    private Integer bicicleta; // Usando Long para o ID da bicicleta
    private Integer numero;
    private String localizacao;
    private String anoDeFabricacao;
    private String modelo;
    private String status;

    // Construtor padrão (importante para desserialização)
    public TrancaResponseDTO() {
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBicicleta() {
        return bicicleta;
    }

    public void setBicicleta(Integer bicicleta) {
        this.bicicleta = bicicleta;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getAnoDeFabricacao() {
        return anoDeFabricacao;
    }

    public void setAnoDeFabricacao(String anoDeFabricacao) {
        this.anoDeFabricacao = anoDeFabricacao;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TrancaResponseDTO{" +
                "id=" + id +
                ", bicicleta=" + bicicleta +
                ", numero=" + numero +
                ", localizacao='" + localizacao + '\'' +
                ", anoDeFabricacao='" + anoDeFabricacao + '\'' +
                ", modelo='" + modelo + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
