package com.es2.bicicletario.EntityTests;

import org.junit.jupiter.api.Test;

import com.es2.bicicletario.entity.*;

import static org.junit.jupiter.api.Assertions.*;

class FuncionarioTest {

    @Test
    void deveInstanciarFuncionarioCorretamenteComSetters() {

        Email email = new Email("funcionario@empresa.com");
        Cpf cpf = new Cpf("11122233344");
        final String senhaTeste = "senha123";

        Funcionario funcionario = new Funcionario();
        funcionario.setMatricula("F001");
        funcionario.setNome("Felipe Machado");
        funcionario.setSenha(senhaTeste);
        funcionario.setConfirmacaoSenha(senhaTeste); 
        funcionario.setEmail(email);
        funcionario.setIdade(30);
        funcionario.setFuncao("Operador");
        funcionario.setCpf(cpf);

        assertNotNull(funcionario);
        assertEquals("F001", funcionario.getMatricula());
        assertEquals("Felipe Machado", funcionario.getNome());
        assertEquals(senhaTeste, funcionario.getSenha());
        assertEquals(senhaTeste, funcionario.getConfirmacaoSenha());
        assertEquals("funcionario@empresa.com", funcionario.getEmail().getEndereco());
        assertEquals(30, funcionario.getIdade());
        assertEquals("Operador", funcionario.getFuncao());
        assertEquals("11122233344", funcionario.getCpf().getNumero());
    }

    @Test
    void deveInstanciarFuncionarioCorretamenteComConstrutorCompleto() {

        Email email = new Email("admin@empresa.com");
        Cpf cpf = new Cpf("44455566677");
        final String senhaTeste = "senha123";

        Funcionario funcionario = new Funcionario(
                "F002",
                senhaTeste,
                senhaTeste,
                email,
                "Márcio Braga",
                42,
                "Mecânico",
                cpf
        );

        assertNotNull(funcionario);
        assertEquals("F002", funcionario.getMatricula());
        assertEquals("Márcio Braga", funcionario.getNome());
        assertEquals(senhaTeste, funcionario.getSenha());
        assertEquals("admin@empresa.com", funcionario.getEmail().getEndereco());
        assertEquals(42, funcionario.getIdade());
        assertEquals("Mecânico", funcionario.getFuncao());
        assertEquals("44455566677",  funcionario.getCpf().getNumero());
    }
}