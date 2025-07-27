package com.es2.bicicletario.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Devolucao")
@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class Devolucao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDevolucao;

    @Column(nullable = true)
    private Integer trancaFinal;

    @Column(nullable = true)
    private Integer idBicicleta;

    @Column(nullable = true)
    private LocalDateTime horaFim;

    @Column(nullable = true)
    private BigDecimal cobrancaExtra;
    
    @Column(nullable = true)
    private String numCartao; 

    @Column(nullable = true)
    private LocalDateTime horaCobranca;

    /**
     * Define um relacionamento One-to-One com a entidade Aluguel.
     * Cada devolução pertence a exatamente um aluguel.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "aluguel_id",
            referencedColumnName = "idAluguel",
            nullable = false,
            unique = true
    )
    private Aluguel aluguel;
}
