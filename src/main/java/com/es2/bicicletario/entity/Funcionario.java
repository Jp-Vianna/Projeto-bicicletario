package com.es2.bicicletario.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "funcionarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario {

    @Id
    @Column(name = "matricula", nullable = false, unique = true)
    private String matricula;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Transient
    private String confirmacaoSenha;

    @Embedded
    @Column(name = "email", nullable = false, unique = true)
    private Email email;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "idade")
    private Integer idade;

    @Column(name = "funcao", nullable = false)
    private String funcao;

    @Embedded
    @Column(name = "cpf", nullable = false)
    private Cpf cpf;

}
