package com.es2.bicicletario.EntityTests;

import org.junit.jupiter.api.Test;

import com.es2.bicicletario.entity.Cpf;

import static org.junit.jupiter.api.Assertions.*;

class CpfTest {

    @Test
    void validarCpf_DeveRetornarTrue_ParaCpfValido() {
        assertTrue(Cpf.validarCpf("12345678909")); 
        assertTrue(Cpf.validarCpf("111.222.333-96"));
    }

    @Test
    void validarCpf_DeveRetornarFalse_ParaCpfInvalido() {
        assertFalse(Cpf.validarCpf(null)); // CPF nulo
        assertFalse(Cpf.validarCpf("1234567890")); // Menos de 11 dígitos
        assertFalse(Cpf.validarCpf("11111111111")); // Todos os dígitos iguais
        assertFalse(Cpf.validarCpf("abcdefghijk")); // Não numérico
    }

    @Test
    void construtorEGetters() {
        String numeroCpf = "12345678909";
        Cpf cpf = new Cpf(numeroCpf);
        assertEquals(numeroCpf, cpf.getNumero());
    }
}
