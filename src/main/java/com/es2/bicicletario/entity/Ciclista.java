package com.es2.bicicletario.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ciclistas")
@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class Ciclista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false, length = 100)
    private String nomeCiclista;

    @Embedded
    @Column(unique = true, nullable = true)
    private Cpf cpf;
    
    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Nacionalidade nacionalidade;
    
    @Embedded
    @Column(nullable = false, unique = true)
    private Email email;

    @Column(name = "foto_documento", nullable = false)
    private String fotoDocumento;

    @Embedded
    @Column(nullable = false)
    private CartaoDeCredito cartao;

    @Embedded
    @Column(nullable = true)
    private Passaporte passaporte;

}