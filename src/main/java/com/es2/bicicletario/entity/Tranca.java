package com.es2.bicicletario.entity;

// Simulando integração
public class Tranca {
    private Integer idTranca; 
    private Integer idBicicleta;

    public Tranca(Integer id){
        this.idTranca = id;
        this.idBicicleta = 1;
    }

    public static void destravaTranca() {
        System.out.println("Tranca destravada com sucesso!");
    }

    public static void travarTranca() {
        System.out.println("Tranca travada com sucesso!");
    }

    public Integer getIdTranca() {
        return idTranca;
    }

    public Integer getIdBicicleta() {
        return idBicicleta;
    }
}
