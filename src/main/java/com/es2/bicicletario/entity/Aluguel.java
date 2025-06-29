package com.es2.bicicletario.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "alugueis")
@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class Aluguel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAluguel;

    @Column(nullable = false) 
    private Integer idBicicleta;// Não integrado

    @Column(nullable = false)
    private LocalDateTime horaInicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ciclista_id", nullable = false)
    private Ciclista ciclista;

    @Column(nullable = false)
    private Integer trancaInicial;// Não integrado

    @Enumerated(EnumType.STRING)
    private Status status;

}
