package com.es2.bicicletario.EntityTests;

import org.junit.jupiter.api.Test;

import com.es2.bicicletario.entity.*;

import java.time.YearMonth;
import static org.junit.jupiter.api.Assertions.*;

class CartaoDeCreditoTest {

    @Test
    void deveInstanciarCartaoDeCreditoCorretamente() {
        // Arrange
        String numero = "1234567812345678";
        String nomeTitular = "Jose da Silva";
        YearMonth validade = YearMonth.of(2027, 10);
        String cvv = "123";

        // Act
        // Testa construtor com todos os argumentos
        CartaoDeCredito cartao1 = new CartaoDeCredito(numero, nomeTitular, validade, cvv);

        // Testa construtor vazio e setters
        CartaoDeCredito cartao2 = new CartaoDeCredito();
        cartao2.setNumeroCartao(numero);
        cartao2.setNomeNoCartao(nomeTitular);
        cartao2.setValidade(validade);
        cartao2.setCodigoSeguranca(cvv);

        // Assert para o primeiro objeto
        assertEquals(numero, cartao1.getNumeroCartao());
        assertEquals(nomeTitular, cartao1.getNomeNoCartao());
        assertEquals(validade, cartao1.getValidade());
        assertEquals(cvv, cartao1.getCodigoSeguranca());

        // Assert para o segundo objeto
        assertEquals(numero, cartao2.getNumeroCartao());
        assertEquals(nomeTitular, cartao2.getNomeNoCartao());
        assertEquals(validade, cartao2.getValidade());
        assertEquals(cvv, cartao2.getCodigoSeguranca());
    }

    @Test
    void verificaCartao_DeveRetornarTrue() {
        // Arrange & Act
        // Chama o método estático que simula a validação
        boolean resultado = CartaoDeCredito.verificaCartao();

        // Assert
        // O teste passará se o método retornar true, como esperado na implementação atual.
        assertTrue(resultado, "O método de verificação de cartão deve retornar true.");
    }
}