package com.es2.bicicletario.EntityTests;

import org.junit.jupiter.api.Test;

import com.es2.bicicletario.entity.*;

import java.time.YearMonth;
import static org.junit.jupiter.api.Assertions.*;

class CartaoDeCreditoTest {

    @Test
    void deveInstanciarCartaoDeCreditoCorretamente() {

        String numero = "1234567812345678";
        String nomeTitular = "Jose da Silva";
        YearMonth validade = YearMonth.of(2027, 10);
        String cvv = "123";

        CartaoDeCredito cartao1 = new CartaoDeCredito(numero, nomeTitular, validade, cvv);

        CartaoDeCredito cartao2 = new CartaoDeCredito();
        cartao2.setNumeroCartao(numero);
        cartao2.setNomeNoCartao(nomeTitular);
        cartao2.setValidade(validade);
        cartao2.setCodigoSeguranca(cvv);

        assertEquals(numero, cartao1.getNumeroCartao());
        assertEquals(nomeTitular, cartao1.getNomeNoCartao());
        assertEquals(validade, cartao1.getValidade());
        assertEquals(cvv, cartao1.getCodigoSeguranca());

        assertEquals(numero, cartao2.getNumeroCartao());
        assertEquals(nomeTitular, cartao2.getNomeNoCartao());
        assertEquals(validade, cartao2.getValidade());
        assertEquals(cvv, cartao2.getCodigoSeguranca());
    }

    @Test
    void verificaCartao_DeveRetornarTrue() {

        boolean resultado = CartaoDeCredito.verificaCartao();

        assertTrue(resultado, "O método de verificação de cartão deve retornar true.");
    }
}