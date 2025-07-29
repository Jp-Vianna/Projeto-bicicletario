package com.es2.bicicletario.EntityTests;

import org.junit.jupiter.api.Test;

import com.es2.bicicletario.entity.Email;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void isValido_DeveRetornarTrue_ParaEmailValido() {
        assertTrue(Email.isValido("teste@exemplo.com"));
        assertTrue(Email.isValido("usuario.123@provedor.co.uk"));
    }

    @Test
    void isValido_DeveRetornarFalse_ParaEmailInvalido() {
        assertFalse(Email.isValido(null)); // Email nulo
        assertFalse(Email.isValido("")); // Email vazio
        assertFalse(Email.isValido("emailinvalido")); // Sem @
        assertFalse(Email.isValido("teste@.com")); // Sem domínio
        assertFalse(Email.isValido("@dominio.com")); // Sem parte local
    }

    @Test
    void construtorEToString() {
        String enderecoEmail = "contato@meusite.com";
        Email email = new Email(enderecoEmail);
        assertEquals(enderecoEmail, email.getEndereco());
        assertEquals(enderecoEmail, email.toString());
    }
}
