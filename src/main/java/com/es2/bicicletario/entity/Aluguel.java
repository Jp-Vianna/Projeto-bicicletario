package com.es2.bicicletario.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class Aluguel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAluguel;

    @Column(nullable = false)
    private Integer idBicicleta;

    @Column(nullable = false)
    private String horaInicio;

    @Column(nullable = false)
    private Integer idCiclista;

    @Column(nullable = false)
    private Integer trancaInicial;

    @Column(nullable = true)
    private Integer TrancaFinal;

    @Column(nullable = true)
    private String horaFim;

    @Column(nullable = true)
    private BigDecimal cobranca;

    @Enumerated(EnumType.STRING)
    private Status status;

}
