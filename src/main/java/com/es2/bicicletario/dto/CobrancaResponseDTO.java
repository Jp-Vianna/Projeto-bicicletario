package com.es2.bicicletario.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CobrancaResponseDTO {

    private Integer id;
    private String status;
    private LocalDateTime horaSolicitacao;
    private LocalDateTime horaFinalizacao;
    private BigDecimal valor;
    private Integer ciclista;

    // Construtor padrão
    public CobrancaResponseDTO() {
    }
    
    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getHoraSolicitacao() {
        return horaSolicitacao;
    }

    public void setHoraSolicitacao(LocalDateTime horaSolicitacao) {
        this.horaSolicitacao = horaSolicitacao;
    }



    public LocalDateTime getHoraFinalizacao() {
        return horaFinalizacao;
    }

    public void setHoraFinalizacao(LocalDateTime horaFinalizacao) {
        this.horaFinalizacao = horaFinalizacao;
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
        return "AluguelResponseDTO{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", horaSolicitacao=" + horaSolicitacao +
                ", horaFinalizacao=" + horaFinalizacao +
                ", valor=" + valor +
                ", ciclista=" + ciclista +
                '}';
    }
}
